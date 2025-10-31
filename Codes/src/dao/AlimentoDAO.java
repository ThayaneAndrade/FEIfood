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
        String sql = "select id_ali, nome_ali, preco_ali, tipo_ali " +
        "from tbalimentos " +
        "where nome_ali ILIKE ? " +
        "order by nome_ali";
        List<Alimento> lista = new ArrayList<>();
        String termo = (nome == null) ? "" : nome.trim();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + termo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Alimento(
                        rs.getInt("id_ali"),
                        rs.getString("nome_ali"),
                        rs.getDouble("preco_ali"),
                        rs.getString("tipo_ali")
                    ));
                }
            }
        }
        return lista;
     }
   
}

  
