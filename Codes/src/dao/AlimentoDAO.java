/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Alimento;
import Model.Comida;
import Model.Bebida;
/**
 *
 * @author thaya
 */
//Define a classe DAO (Objeto de Acesso a Dados) para Alimentos.
public class AlimentoDAO {
    private Connection conn;

    public AlimentoDAO(Connection conn) {
        this.conn = conn;
    }

    //Método que busca alimentos no banco por nome (com 'LIKE').
    public List<Alimento> buscarPorNome(String nome) throws SQLException {
        
        //Define a string SQL para a consulta.
        //Seleciona todas as colunas necessárias da tabela de alimentos.
        String sql = "SELECT id_ali, nome_ali, preco_ali, tipo_ali, vegetariano, zero " +
                       "FROM tbalimentos " +
                       //Usa ILIKE para busca 'case-insensitive' (ignora maiúsculas/minúsculas).
                       "WHERE nome_ali ILIKE ? " +
                       //Ordena os resultados alfabeticamente pelo nome.
                       "ORDER BY nome_ali";

        //Cria uma lista vazia para armazenar os alimentos encontrados.
        List<Alimento> lista = new ArrayList<>();
        //Prepara o termo de busca (se for nulo, busca tudo).
        String termo = (nome == null) ? "" : nome.trim();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            //Define o parâmetro ? da query (ex: "salgado" vira "%salgado%").
            ps.setString(1, "%" + termo + "%");

            //Usa 'try-with-resources' para garantir que o ResultSet seja fechado.
            try (ResultSet rs = ps.executeQuery()) {
                //Itera sobre cada linha (alimento) que o banco retornou.
                while (rs.next()) {
     
                   //Pega o valor da coluna 'tipo_ali'.
                   String tipo = rs.getString("tipo_ali");
                   //Cria uma variável 'Alimento' (classe pai).
                    Alimento a;
                    
                    //Verifica o tipo para instanciar o objeto correto (Polimorfismo).
                    if (tipo != null && (tipo.toLowerCase().contains("bebida")))
                    {
                        //Se for bebida, cria um objeto Bebida.
                        Bebida b = new Bebida();
                        //Preenche (hidrata) o objeto com os dados do ResultSet.
                        b.setId(rs.getInt("id_ali"));
                        b.setNome(rs.getString("nome_ali"));
                        b.setPreco(rs.getDouble("preco_ali"));
                        b.setTipo(tipo);
                        b.setZero(rs.getBoolean("zero"));
                        
                        //Verifica se o tipo contém "alco" para definir se é alcoólica.
                        boolean ehAlcoolica = tipo != null &&
                                           tipo.toLowerCase().contains("alco");
                            b.setAlcoolica(ehAlcoolica);
                    
                        //Atribui o objeto Bebida (filho) à variável Alimento (pai).
                        a = b;
                        
                    } else {
                        
                        //Se não for bebida, cria um objeto Comida.
                        Comida c = new Comida();
                        //Preenche (hidrata) o objeto com os dados do ResultSet.
                        c.setId(rs.getInt("id_ali"));
                        c.setNome(rs.getString("nome_ali"));
                        c.setPreco(rs.getDouble("preco_ali"));
                        c.setTipo(tipo);
                        c.setVegetariano(rs.getBoolean("vegetariano"));
                        c.setZero(rs.getBoolean("zero"));
                        
                        //Atribui o objeto Comida (filho) à variável Alimento (pai).
                        a = c;
                    }

                    //Adiciona o objeto criado (Comida ou Bebida) à lista.
                    lista.add(a);
                }
            }
        }  
    //Retorna a lista preenchida (ou vazia, se nada for encontrado).
    return lista;
}
}