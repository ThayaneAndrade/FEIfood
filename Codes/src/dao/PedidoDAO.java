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

public class PedidoDAO {
    private Connection conn;

    public PedidoDAO(Connection conn) {
        this.conn = conn;
    }

    // Consulta um pedido pelo ID
    public ResultSet consultar(Pedido pedido) throws SQLException {
        String sql = "SELECT * FROM tbpedidos WHERE id_ped = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, pedido.getId());
        statement.execute();
        return statement.getResultSet();
    }

    // Consulta pedidos de um usuário
    public ResultSet consultarPorUsuario(Usuario usuario) throws SQLException {
        String sql = "SELECT * FROM tbpedidos WHERE usuario_id = ? ORDER BY "
                + "data_ped DESC";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, usuario.getId());
        statement.execute();
        return statement.getResultSet();
    }

    // INSERE pedido + itens
    public void inserir(Pedido pedido) throws SQLException {
        // NÃO passa id_ped, ele é gerado pelo banco (SERIAL/IDENTITY)
        String sqlPed = "INSERT INTO tbpedidos (usuario_id, data_ped, "
                + "avaliacao_ped) "
                      + "VALUES (?,?,?) RETURNING id_ped";

        try (PreparedStatement stPed = conn.prepareStatement(sqlPed)) {

            stPed.setInt(1, pedido.getUsuario().getId());
            stPed.setDate(2, pedido.getDataPed());

            if (pedido.getAvaliacaoPed() == null) {
                stPed.setNull(3, Types.INTEGER);
            } else {
                stPed.setInt(3, pedido.getAvaliacaoPed());
            }

            try (ResultSet rs = stPed.executeQuery()) {
                if (rs.next()) {
                    int idGerado = rs.getInt("id_ped");
                    pedido.setId(idGerado);
                } else {
                    throw new SQLException("Não foi possível obter o id do pedido.");
                }
            }
        }

        inserirItens(pedido);

        // Não feche a conexão aqui, o controller cuidará disso.
    }

    private void inserirItens(Pedido pedido) throws SQLException {
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            return;
        }

        String sqlItens = "INSERT INTO tbpedido_alimento (pedido_id, "
                + "alimento_id,quantidade) "
                        + "VALUES (?, ?,?)";

        try (PreparedStatement stItem = conn.prepareStatement(sqlItens)) {
            for (Alimento a : pedido.getItens()) {
                stItem.setInt(1, pedido.getId());
                stItem.setInt(2, a.getId());
                stItem.setInt(3, 1);
                stItem.addBatch();
            }
            stItem.executeBatch();
        }
    }

    public void atualizar(Pedido pedido) throws SQLException {
        String sqlPed = "UPDATE tbpedidos "
                      + "SET data_ped = ?, avaliacao_ped = ? "
                      + "WHERE id_ped = ?";

        try (PreparedStatement stPed = conn.prepareStatement(sqlPed)) {
            stPed.setDate(1, pedido.getDataPed());

            if (pedido.getAvaliacaoPed() == null) {
                stPed.setNull(2, Types.INTEGER);
            } else {
                stPed.setInt(2, pedido.getAvaliacaoPed());
            }

            stPed.setInt(3, pedido.getId());
            stPed.execute();
        }

        // Atualiza itens: remove todos e insere de novo (lógica simples)
        removerItens(pedido.getId());
        inserirItens(pedido);
    }

    private void removerItens(int idPedido) throws SQLException {
        String sql = "DELETE FROM tbpedido_alimento WHERE pedido_id = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idPedido);
            st.execute();
        }
    }

    public void remover(Pedido pedido) throws SQLException {
        // Primeiro remove os itens
        removerItens(pedido.getId());

        // Depois remove o pedido
        String sql = "DELETE FROM tbpedidos WHERE id_ped = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, pedido.getId());
            st.execute();
        }
    }

    // Consulta os itens (alimentos) de um pedido
    public ResultSet consultarItens(Pedido pedido) throws SQLException {
        
        // ESTE É O SQL CORRIGIDO (com vírgula)
        String sql = "SELECT a.id_ali, a.nome_ali, a.preco_ali, a.tipo_ali, "
                   + " a.vegetariano, a.zero, " // <-- A VÍRGULA ESTÁ AQUI
                   + " pa.quantidade "
                   + "FROM tbpedido_alimento pa "
                   + "JOIN tbalimentos a ON pa.alimento_id = a.id_ali "
                   + "WHERE pa.pedido_id = ?";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, pedido.getId());
        st.execute();
        return st.getResultSet();
    }
    
    /**
     * Atualiza SOMENTE a avaliação de um pedido no banco de dados.
     * Este método é usado pela tela 'MeusPedidos'.
     * @param idPedido O ID do pedido a ser atualizado.
     * @param avaliacao A nova nota (ou null).
     * @throws SQLException
     */
    public void atualizarAvaliacao(int idPedido, Integer avaliacao) throws SQLException {
        String sql = "UPDATE tbpedidos SET avaliacao_ped = ? WHERE id_ped = ?";
        
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            
            if (avaliacao == null) {
                st.setNull(1, java.sql.Types.INTEGER);
            } else {
                st.setInt(1, avaliacao);
            }
            st.setInt(2, idPedido);
            
            st.executeUpdate(); // Use executeUpdate para UPDATE
        }
        // Não feche a 'conn'. O try-with-resources do Controller cuida disso.
    }
}