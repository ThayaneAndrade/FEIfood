/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import View.GerenciarPedido;
import Model.Usuario;
import Model.Alimento;
import Model.Pedido;
import dao.Conexao;
import dao.PedidoDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author thaya
 */
public class ControlePedido {
    private final GerenciarPedido tela;
    private final Usuario usuarioLogado;
    private final List<Alimento> itensPedido = new ArrayList<>();
    private Pedido pedidoAtual = new Pedido();

    public ControlePedido(GerenciarPedido tela, Usuario usuarioLogado, List<Alimento> itensSelecionados) {
        this.tela = tela;
        this.usuarioLogado = usuarioLogado;

        this.itensPedido.addAll(itensSelecionados);
        this.pedidoAtual.setUsuario(usuarioLogado);
        this.pedidoAtual.setItens(itensPedido);

        // mostrar usuário
        this.tela.getLblUsuario().setText(usuarioLogado.getNome());

        // colocar data de hoje como padrão
        this.tela.getTxtData().setText(new Date(System.currentTimeMillis()).
                toString());

        // preencher tabela
        atualizarTabela();

        // listeners
        this.tela.getBtSalvar().addActionListener(e -> salvar());
        this.tela.getBtAtualizar().addActionListener(e -> atualizar());
        this.tela.getBtExcluir().addActionListener(e -> excluir());
        this.tela.getBtVoltar().addActionListener(e -> voltar());
    }

    private void atualizarTabela() {
        DefaultTableModel model = (DefaultTableModel) tela.getTblItens().
                getModel();
        model.setRowCount(0);
        for (Alimento a : itensPedido) {
            model.addRow(new Object[] {
                a.getId(),
                a.getNome(),
                a.getPreco(),
                a.getTipo(),
              
});
        }
    }

    private Date lerDataDaTela() {
        String dataStr = tela.getTxtData().getText().trim();
        if (dataStr.isEmpty()) {
            return null;
        }
        try {
            return Date.valueOf(dataStr); // AAAA-MM-DD
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(tela,
                    "Data inválida. Use o formato AAAA-MM-DD.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // ============= SALVAR (NOVO PEDIDO) =============
    private void salvar() {
        if (itensPedido.isEmpty()) {
            JOptionPane.showMessageDialog(tela,
                    "Não há itens no pedido.",
                    "Aviso",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Date data = lerDataDaTela();
        if (data == null) {
            return; // mensagem já foi mostrada
        }

        pedidoAtual.setUsuario(usuarioLogado);
        pedidoAtual.setDataPed(data);
        pedidoAtual.setAvaliacaoPed(null);
        pedidoAtual.setItens(new ArrayList<>(itensPedido));

        Conexao conexao = new Conexao();
        Connection conn = null;

        try {
            conn = conexao.getConnection();
            PedidoDAO dao = new PedidoDAO(conn);
            dao.inserir(pedidoAtual); // preenche id no pedidoAtual

            tela.getTxtIdPedido().setText(String.valueOf(pedidoAtual.getId()));

            JOptionPane.showMessageDialog(tela,
                    "Pedido salvo com sucesso!",
                    "Aviso",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(tela,
                    "Erro ao salvar pedido.\n" + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) {}
            }
        }
    }

    // ============= ATUALIZAR PEDIDO EXISTENTE =============
    private void atualizar() {
        String idStr = tela.getTxtIdPedido().getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(tela,
                    "Informe o ID do pedido (ou salve primeiro).",
                    "Aviso",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = Integer.parseInt(idStr);
        Date data = lerDataDaTela();
        if (data == null) {
            return;
        }

        pedidoAtual.setId(id);
        pedidoAtual.setDataPed(data);
        pedidoAtual.setItens(new ArrayList<>(itensPedido));

        Conexao conexao = new Conexao();
        Connection conn = null;

        try {
            conn = conexao.getConnection();
            PedidoDAO dao = new PedidoDAO(conn);
            dao.atualizar(pedidoAtual);

            JOptionPane.showMessageDialog(tela,
                    "Pedido atualizado com sucesso!",
                    "Aviso",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(tela,
                    "Erro ao atualizar pedido.\n" + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) {}
            }
        }
    }

    // ============= EXCLUIR PEDIDO =============
    private void excluir() {
        String idStr = tela.getTxtIdPedido().getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(tela,
                    "Informe o ID do pedido para excluir.",
                    "Aviso",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int id = Integer.parseInt(idStr);

        int opc = JOptionPane.showConfirmDialog(
                tela,
                "Deseja realmente excluir o pedido " + id + "?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );
        if (opc != JOptionPane.YES_OPTION) {
            return;
        }

        pedidoAtual.setId(id);

        Conexao conexao = new Conexao();
        Connection conn = null;

        try {
            conn = conexao.getConnection();
            PedidoDAO dao = new PedidoDAO(conn);
            dao.remover(pedidoAtual);

            JOptionPane.showMessageDialog(tela,
                    "Pedido excluído com sucesso!",
                    "Aviso",
                    JOptionPane.INFORMATION_MESSAGE);

            // limpa tela
            tela.getTxtIdPedido().setText("");
            // mantém itens, se quiser limpar também:
            // itensPedido.clear();
            // atualizarTabela();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(tela,
                    "Erro ao excluir pedido.\n" + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) {}
            }
        }
    }

    // ============= VOLTAR =============
    private void voltar() {
        tela.dispose();
        // Se quiser reabrir a BuscaAlimento, pode fazer aqui.
    }
}
    

