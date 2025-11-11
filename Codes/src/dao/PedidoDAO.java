/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import Model.Pedido;
import Model.Usuario;
import Model.Alimento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

//Define a classe DAO (Objeto de Acesso a Dados) para Pedidos.
public class PedidoDAO {

    private Connection conn;

    public PedidoDAO(Connection conn) {
        this.conn = conn;
    }

    //Consulta um pedido específico pelo seu ID.
    public ResultSet consultar(Pedido pedido) throws SQLException {
        //Define a string SQL para a consulta.
        String sql = "SELECT * FROM tbpedidos WHERE id_ped = ?";
        //Cria o PreparedStatement (declaração SQL pré-compilada).
        PreparedStatement statement = conn.prepareStatement(sql);
        //Define o primeiro parâmetro (?) como o ID do pedido.
        statement.setInt(1, pedido.getId());
        //Executa a consulta.
        statement.execute();
        //Retorna o ResultSet (conjunto de resultados) para o Controller.
        return statement.getResultSet();
    }

    //Consulta todos os pedidos de um usuário específico.
    public ResultSet consultarPorUsuario(Usuario usuario) throws SQLException {
        //Define a string SQL, ordenando pela data mais recente.
        String sql = "SELECT * FROM tbpedidos WHERE usuario_id = ? ORDER BY "
                + "data_ped DESC";
        //Cria o PreparedStatement.
        PreparedStatement statement = conn.prepareStatement(sql);
        //Define o primeiro parâmetro (?) como o ID do usuário.
        statement.setInt(1, usuario.getId());
        //Executa a consulta.
        statement.execute();
        //Retorna o ResultSet.
        return statement.getResultSet();
    }

    //Insere um novo pedido (e seus itens) no banco de dados.
    public void inserir(Pedido pedido) throws SQLException {
        //Define o SQL para inserir na tabela 'tbpedidos'.
        //'RETURNING id_ped' pede ao PostgreSQL para retornar o ID que foi gerado.
        String sqlPed = "INSERT INTO tbpedidos (usuario_id, data_ped, "
                + "avaliacao_ped) "
                      + "VALUES (?,?,?) RETURNING id_ped";

        //Usa 'try-with-resources' para garantir que o PreparedStatement feche.
        try (PreparedStatement stPed = conn.prepareStatement(sqlPed)) {

            //Define os parâmetros do SQL.
            stPed.setInt(1, pedido.getUsuario().getId());
            stPed.setDate(2, pedido.getDataPed());

            //Verifica se a avaliação é nula (pedido novo).
            if (pedido.getAvaliacaoPed() == null) {
                //Define o parâmetro como NULL no banco.
                stPed.setNull(3, Types.INTEGER);
            } else {
                //Define o parâmetro com o valor da nota.
                stPed.setInt(3, pedido.getAvaliacaoPed());
            }

            //Usa 'try-with-resources' para o ResultSet que pegará o ID retornado.
            try (ResultSet rs = stPed.executeQuery()) {
                //Verifica se o banco realmente retornou um ID.
                if (rs.next()) {
                    //Pega o ID gerado (ex: 15).
                    int idGerado = rs.getInt("id_ped");
                    //Atualiza o objeto 'pedido' em memória com o ID do banco.
                    pedido.setId(idGerado);
                } else {
                    //Lança um erro se o ID não for retornado.
                    throw new SQLException("Não foi possível obter o id do pedido.");
                }
            }
        }

        //Chama o método privado para inserir os itens na tabela 'tbpedido_alimento'.
        inserirItens(pedido);
    }

    //Método privado para inserir os alimentos associados a um pedido.
    private void inserirItens(Pedido pedido) throws SQLException {
        //Verifica se a lista de itens não é nula ou vazia.
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            return; //Não faz nada se não houver itens.
        }

        //Define o SQL para a tabela de ligação 'tbpedido_alimento'.
        String sqlItens = "INSERT INTO tbpedido_alimento (pedido_id, "
                + "alimento_id,quantidade) "
                        + "VALUES (?, ?,?)";

        //Usa 'try-with-resources' para o PreparedStatement.
        try (PreparedStatement stItem = conn.prepareStatement(sqlItens)) {
            //Itera sobre a lista de objetos Alimento no pedido.
            for (Alimento a : pedido.getItens()) {
                //Define os parâmetros do SQL.
                stItem.setInt(1, pedido.getId()); //O ID do pedido (pai).
                stItem.setInt(2, a.getId()); //O ID do alimento (item).
                stItem.setInt(3, 1); //Define a quantidade como 1 (padrão).
                //Adiciona o comando a um "lote" (batch) para execução em massa.
                stItem.addBatch();
            }
            //Executa todos os comandos do lote de uma vez (mais eficiente).
            stItem.executeBatch();
        }
    }

    //Atualiza um pedido existente (data e avaliação).
    public void atualizar(Pedido pedido) throws SQLException {
        //Define o SQL para atualizar a tabela 'tbpedidos'.
        String sqlPed = "UPDATE tbpedidos "
                      + "SET data_ped = ?, avaliacao_ped = ? "
                      + "WHERE id_ped = ?";

        //Usa 'try-with-resources'.
        try (PreparedStatement stPed = conn.prepareStatement(sqlPed)) {
            //Define os parâmetros.
            stPed.setDate(1, pedido.getDataPed());

            if (pedido.getAvaliacaoPed() == null) {
                stPed.setNull(2, Types.INTEGER);
            } else {
                stPed.setInt(2, pedido.getAvaliacaoPed());
            }
            stPed.setInt(3, pedido.getId()); //Cláusula WHERE.
            //Executa o comando UPDATE.
            stPed.execute();
        }

        //Lógica simples para atualizar itens: apaga todos os antigos.
        removerItens(pedido.getId());
        //Insere todos os itens da lista atual do objeto 'pedido'.
        inserirItens(pedido);
    }

    //Método privado para apagar todos os itens de um pedido (tabela 'tbpedido_alimento').
    private void removerItens(int idPedido) throws SQLException {
        //Define o SQL de deleção.
        String sql = "DELETE FROM tbpedido_alimento WHERE pedido_id = ?";

        //Usa 'try-with-resources'.
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            //Define o ID do pedido a ter os itens removidos.
            st.setInt(1, idPedido);
            //Executa o comando DELETE.
            st.execute();
        }
    }

    //Remove um pedido completo (primeiro os itens, depois o pedido).
    public void remover(Pedido pedido) throws SQLException {
        //Chama o método para apagar os "filhos" (itens) primeiro.
        removerItens(pedido.getId());

        //Define o SQL para apagar o "pai" (o pedido em 'tbpedidos').
        String sql = "DELETE FROM tbpedidos WHERE id_ped = ?";

        //Usa 'try-with-resources'.
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            //Define o ID do pedido a ser removido.
            st.setInt(1, pedido.getId());
            //Executa o comando DELETE.
            st.execute();
        }
    }

    //Consulta os alimentos (itens) de um pedido específico.
    public ResultSet consultarItens(Pedido pedido) throws SQLException {
        
        //Define o SQL que junta 'tbpedido_alimento' com 'tbalimentos'.
        String sql = "SELECT a.id_ali, a.nome_ali, a.preco_ali, a.tipo_ali, "
                   + " a.vegetariano, a.zero, " //Vírgula corrigida aqui.
                   + " pa.quantidade "
                   + "FROM tbpedido_alimento pa "
                   //Faz o JOIN usando as chaves primária/estrangeira.
                   + "JOIN tbalimentos a ON pa.alimento_id = a.id_ali "
                   //Filtra pelo ID do pedido desejado.
                   + "WHERE pa.pedido_id = ?";

        //Cria o PreparedStatement.
        PreparedStatement st = conn.prepareStatement(sql);
        //Define o parâmetro (ID do pedido).
        st.setInt(1, pedido.getId());
        //Executa a consulta.
        st.execute();
        //Retorna o ResultSet com a lista de alimentos.
        return st.getResultSet();
    }
    
    //Atualiza SOMENTE a avaliação de um pedido (usado pela tela 'MeusPedidos').
    public void atualizarAvaliacao(int idPedido, Integer avaliacao) throws SQLException {
        //Define o SQL para atualizar apenas a coluna 'avaliacao_ped'.
        String sql = "UPDATE tbpedidos SET avaliacao_ped = ? WHERE id_ped = ?";
        
        //Usa 'try-with-resources'.
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            
            //Verifica se a avaliação é nula.
            if (avaliacao == null) {
                st.setNull(1, java.sql.Types.INTEGER);
            } else {
                //Define o parâmetro da nota.
                st.setInt(1, avaliacao);
            }
            //Define o parâmetro do ID (cláusula WHERE).
            st.setInt(2, idPedido);
            
            //Executa o comando (executeUpdate é usado para INSERT, UPDATE, DELETE).
            st.executeUpdate();
        }
    }
}