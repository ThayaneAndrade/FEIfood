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
public class AlimentoDAO {
    private Connection conn;

    public AlimentoDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Alimento> buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT id_ali, nome_ali, preco_ali, tipo_ali ,vegetariano"
                + "zero"+
                     "FROM tbalimentos " +
                     "WHERE nome_ali ILIKE ? " +
                     "ORDER BY nome_ali";

        List<Alimento> lista = new ArrayList<>();
        String termo = (nome == null) ? "" : nome.trim();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + termo + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
     
                   String tipo = rs.getString("tipo_ali");
                    Alimento a;
                    
                    if (tipo != null && (tipo.toLowerCase().contains("bebida")))
                    {
                        Bebida b = new Bebida();
                        b.setId(rs.getInt("id_ali"));
                        b.setNome(rs.getString("nome_ali"));
                        b.setPreco(rs.getDouble("preco_ali"));
                        b.setTipo(tipo);
                        b.setZero(rs.getBoolean("zero"));
                        
                        boolean ehAlcoolica = tipo != null &&
                                           tipo.toLowerCase().contains("alco");
                            b.setAlcoolica(ehAlcoolica);
                    
                        a = b;
                        
                    } else {
                        
                        Comida c = new Comida();
                        c.setId(rs.getInt("id_ali"));
                        c.setNome(rs.getString("nome_ali"));
                        c.setPreco(rs.getDouble("preco_ali"));
                        c.setTipo(tipo);
                        c.setVegetariano(rs.getBoolean("vegetariano"));
                        c.setZero(rs.getBoolean("zero"));
                        
                        a = c;
                    }

                    lista.add(a);
                }
            }
        }  
    return lista;
}
}
  
