package Controller;

import View.GerenciarPedido;
import Model.Usuario;
import Model.Alimento;
import Model.ImpostoAlcool;
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

public class ControlePedido {
    private final GerenciarPedido tela;
    private final Usuario usuarioLogado;
    private final List<Alimento> itensPedido;
    private final List<Alimento> carrinhoOriginal;
    private Pedido pedidoAtual = new Pedido();

    public ControlePedido(GerenciarPedido tela, Usuario usuarioLogado, List<Alimento> carrinho) {
        this.tela = tela;
        this.usuarioLogado = usuarioLogado;
        this.carrinhoOriginal = carrinho;
        this.itensPedido = new ArrayList<>(this.carrinhoOriginal); 
        
        this.pedidoAtual.setUsuario(usuarioLogado);
        this.pedidoAtual.setItens(this.itensPedido);

        this.tela.getLblUsuario().setText(usuarioLogado.getNome());
        this.tela.getTxtData().setText(new Date(System.currentTimeMillis()).toString());
        this.tela.getTxtData().setEditable(false);
        
        this.tela.getLblIdPedido().setText("Novo");
        this.tela.getBtExcluir().setVisible(false);
        this.tela.getBtSalvar().setVisible(true);

        atualizarTabela();
        configurarListeners();
        atualizarPrecoTotal();
    }

    public ControlePedido(GerenciarPedido tela, Usuario usuarioLogado, Pedido pedidoExistente, List<Alimento> itensDoPedido) {
        this.tela = tela;
        this.usuarioLogado = usuarioLogado;
        this.pedidoAtual = pedidoExistente;
        this.carrinhoOriginal = null; // Não aplicável ao carregar pedido existente
        this.itensPedido = new ArrayList<>(itensDoPedido);
        this.pedidoAtual.setItens(this.itensPedido);

        this.tela.getLblUsuario().setText(usuarioLogado.getNome());
        this.tela.getLblIdPedido().setText(String.valueOf(pedidoAtual.getId()));
        this.tela.getTxtData().setText(pedidoAtual.getDataPed().toString());
        this.tela.getTxtData().setEditable(false);
        
        this.tela.getBtSalvar().setVisible(false);
        this.tela.getBtExcluir().setVisible(true);

        atualizarTabela(); 
        configurarListeners();
        atualizarPrecoTotal();
    }
    
    private void configurarListeners() {
        this.tela.getBtSalvar().addActionListener(e -> salvar());
        this.tela.getBtExcluir().addActionListener(e -> excluir());
        this.tela.getBtVoltar().addActionListener(e -> voltar());
        
        if (this.tela.getBtRemoverItem() != null) {
            this.tela.getBtRemoverItem().addActionListener(e -> removerItem());
        }
    }

    private void atualizarTabela() {
        DefaultTableModel model = (DefaultTableModel) tela.getTblItens().getModel();
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
    
    private void atualizarPrecoTotal() {
        double total = 0.0;
        for (Alimento a : itensPedido) {
            total += a.getPreco(); 
            
            if (a instanceof ImpostoAlcool) {
                ImpostoAlcool itemComImposto = (ImpostoAlcool) a;
                total += itemComImposto.calcularImpostoAlcool();
            }
        }
        String precoFormatado = String.format("Total (com impostos): R$ %.2f", total);
        if (tela.getLblPrecoTotal() != null) {
            tela.getLblPrecoTotal().setText(precoFormatado); 
        }
    }

    private Date lerDataDaTela() {
        String dataStr = tela.getTxtData().getText().trim();
        if (dataStr.isEmpty()) {
            JOptionPane.showMessageDialog(tela, "O campo Data não pode estar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        try {
            return Date.valueOf(dataStr); 
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(tela, "Data inválida. Use o formato AAAA-MM-DD.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void removerItem() {
        int linhaSelecionada = tela.getTblItens().getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(tela, "Selecione um item da tabela para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        itensPedido.remove(linhaSelecionada);
        atualizarTabela();
        atualizarPrecoTotal();
        JOptionPane.showMessageDialog(tela, "Item removido do pedido.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void salvar() {
        if (itensPedido.isEmpty()) {
            JOptionPane.showMessageDialog(tela, "Não é possível salvar um pedido vazio.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Date data = lerDataDaTela();
        if (data == null) {
            return; 
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
            dao.inserir(pedidoAtual);

            tela.getLblIdPedido().setText(String.valueOf(pedidoAtual.getId()));

            JOptionPane.showMessageDialog(tela, "Pedido salvo com sucesso! (ID: " + pedidoAtual.getId() + ")", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            if (carrinhoOriginal != null) {
                carrinhoOriginal.clear();
            }
            this.itensPedido.clear();
            atualizarTabela();
            
            tela.getBtSalvar().setVisible(false);
            tela.getBtExcluir().setVisible(true);
            
            tela.dispose(); // Fecha a tela

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(tela, "Erro ao salvar pedido.\n" + ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) {}
            }
        }
    }

    private void atualizar() {
        int id = pedidoAtual.getId();
        Date data = lerDataDaTela();
        if (data == null) {
            return;
        }
        if (itensPedido.isEmpty()) {
            JOptionPane.showMessageDialog(tela, "Não é possível atualizar um pedido para ficar vazio. Exclua o pedido se desejar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
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

            JOptionPane.showMessageDialog(tela, "Pedido " + id + " atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(tela, "Erro ao atualizar pedido.\n" + ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) {}
            }
        }
    }

    private void excluir() {
        int id = pedidoAtual.getId();
        int opc = JOptionPane.showConfirmDialog(
                tela,
                "Deseja realmente excluir o pedido " + id + "?\nEsta ação não pode ser desfeita.",
                "Confirmação",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
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

            JOptionPane.showMessageDialog(tela, "Pedido " + id + " excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            tela.dispose(); 

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(tela, "Erro ao excluir pedido.\n" + ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) {}
            }
        }
    }

    private void voltar() {
        tela.dispose();
    }
}