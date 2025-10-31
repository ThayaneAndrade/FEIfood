/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import dao.AlimentoDAO;
import dao.Conexao;
import Model.Alimento;
import View.BuscaAlimento;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ControleBuscaAlimento {

    private final BuscaAlimento tela6;

    public ControleBuscaAlimento(BuscaAlimento tela6) {
        this.tela6 = tela6;
    
        this.tela6.getBtBuscar().addActionListener(e -> buscar());
    }

    private void buscar() {
        String termo = tela6.getTxtBuscar().getText().trim();

        Connection conn = null;
        try {
            Conexao conexao = new Conexao();
            conn = conexao.getConnection();

            AlimentoDAO dao = new AlimentoDAO(conn);
            List<Alimento> lista = dao.buscarPorNome(termo);

            preencherTabela(lista);

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(
                    tela6,
                    "Nenhum alimento encontrado.",
                    "Aviso",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                tela6,
                "Erro ao buscar alimentos.\n" + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) {}
            }
        }
    }

    private void preencherTabela(List<Alimento> lista) {
        DefaultTableModel model = 
                (DefaultTableModel) tela6.getTbResultado().getModel();
        model.setRowCount(0);

        for (Alimento a : lista) {
            model.addRow(new Object[] {
                a.getId(),
                a.getNome(),
                a.getPreco(),
                a.getTipo()
            });
        }
    }
    
}


















    
   
    
