/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import dao.AlimentoDAO;
import dao.Conexao;
import dao.PedidoDAO;

import Model.Alimento;
import Model.Bebida;
import Model.Comida;
import Model.Pedido;
import Model.Usuario;

import View.BuscaAlimento;
import View.GerenciarPedido;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.sql.Date;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ControleBuscaAlimento {

    private final BuscaAlimento tela6;
    private final Usuario usuarioLogado;

    public ControleBuscaAlimento(BuscaAlimento tela6, Usuario usuarioLogado) {
        this.tela6 = tela6;
        this.usuarioLogado = usuarioLogado;
    
        this.tela6.getBtBuscar().addActionListener(e -> buscar());
        this.tela6.getBtFazerPedido().addActionListener(e -> fazerPedido());
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
                a.getTipo(),
                (a instanceof Model.Comida) 
                ? ((Model.Comida)a).isVegetariano() ? "Sim" : "Não"
                : "-",
            a.isZero() ? "Sim" : "Não"
        });
        }
    }
    
    private void fazerPedido() {
    if (usuarioLogado == null) {
        JOptionPane.showMessageDialog(
            tela6,
            "Usuário não informado. Não é possível fazer o pedido.",
            "Erro",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    int[] linhas = tela6.getTbResultado().getSelectedRows();
    if (linhas == null || linhas.length == 0) {
        JOptionPane.showMessageDialog(
            tela6,
            "Selecione pelo menos um alimento na tabela para fazer o pedido.",
            "Aviso",
            JOptionPane.INFORMATION_MESSAGE
        );
        return;
    }

    DefaultTableModel model =
            (DefaultTableModel) tela6.getTbResultado().getModel();

    List<Alimento> itensSelecionados = new ArrayList<>();

    for (int linha : linhas) {
        Object valId    = model.getValueAt(linha, 0);
        Object valNome  = model.getValueAt(linha, 1);
        Object valPreco = model.getValueAt(linha, 2);
        Object valTipo  = model.getValueAt(linha, 3);

        int id          = (valId    instanceof Integer) ? (Integer) valId : Integer.parseInt(valId.toString());
        String nome     = (valNome  != null) ? valNome.toString() : "";
        double preco    = (valPreco instanceof Double) ? (Double) valPreco : Double.parseDouble(valPreco.toString());
        String tipo     = (valTipo  != null) ? valTipo.toString() : "";

        Alimento a;
        if (tipo.toLowerCase().contains("bebida")) {
            Bebida b = new Bebida();
            b.setId(id);
            b.setNome(nome);
            b.setPreco(preco);
            b.setTipo(tipo);
            b.setAlcoolica("bebida alcoólica".equalsIgnoreCase(tipo));
            a = b;
        } else {
            Comida c = new Comida();
            c.setId(id);
            c.setNome(nome);
            c.setPreco(preco);
            c.setTipo(tipo);
            a = c;
        }

        itensSelecionados.add(a);
    }

    // >>> em vez de gravar no banco, abrimos a tela GerenciarPedido <<<
    View.GerenciarPedido telaGerenciar = new View.GerenciarPedido();
    new Controller.ControlePedido(telaGerenciar, usuarioLogado, 
            itensSelecionados);

    telaGerenciar.setLocationRelativeTo(tela6);
    telaGerenciar.setVisible(true);

    // se quiser fechar a tela de busca ao ir pra gerenciar:
    tela6.dispose();
}
}
    













    
   
    
