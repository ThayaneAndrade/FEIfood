package Controller;

import dao.AlimentoDAO;
import dao.Conexao;
import Model.Alimento;
import Model.Bebida;
import Model.Comida;
import Model.Usuario;
import View.BuscaAlimento;
import View.GerenciarPedido;
import View.Logado;
import View.MeusPedidos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ControleBuscaAlimento {

    private final BuscaAlimento tela;
    private final Usuario usuarioLogado;
    
    private List<Alimento> carrinhoDeItens; 

    public ControleBuscaAlimento(BuscaAlimento tela, Usuario usuarioLogado) {
        this.tela = tela;
        this.usuarioLogado = usuarioLogado;
        this.carrinhoDeItens = new ArrayList<>();
  
    
        // Configura os listeners
        this.tela.getBtBuscar().addActionListener(e -> buscar());
        
        // Botão principal: "Adicionar ao Pedido"
        this.tela.getBtAdicionar().addActionListener(e -> adicionarItensAoCarrinho());
        this.tela.getBtVerPedido().addActionListener(e -> verPedido());
        
        // Botão de navegação
        if (this.tela.getBtMinhaConta() != null) {
            this.tela.getBtMinhaConta().addActionListener(e -> abrirMinhaConta());
        }
        
        if (this.tela.getBtMeusPedidos() != null) {
            this.tela.getBtMeusPedidos().addActionListener(e -> abrirMeusPedidos());
        }
    }
    
    
    private void abrirMinhaConta() {
        Logado telaLogado = new Logado(usuarioLogado);
        telaLogado.setLocationRelativeTo(tela);
        telaLogado.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        telaLogado.setVisible(true);
    }
    
    private void abrirMeusPedidos() {
            MeusPedidos telaMeusPedidos = new MeusPedidos();
            new ControleMeusPedidos(telaMeusPedidos, usuarioLogado);

            telaMeusPedidos.setLocationRelativeTo(tela);
            telaMeusPedidos.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            telaMeusPedidos.setVisible(true);
        }
    private void buscar() {
        String termo = tela.getTxtBuscar().getText().trim();
        Connection conn = null;
        try {
            Conexao conexao = new Conexao();
            conn = conexao.getConnection();
            AlimentoDAO dao = new AlimentoDAO(conn);
            List<Alimento> lista = dao.buscarPorNome(termo);
            preencherTabela(lista);
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(tela, "Nenhum alimento encontrado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(tela, "Erro ao buscar alimentos.\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) {}
            }
        }
    }

    private void preencherTabela(List<Alimento> lista) {
        DefaultTableModel model = (DefaultTableModel) tela.getTbResultado().getModel();
        model.setRowCount(0);
        for (Alimento a : lista) {
            model.addRow(new Object[] {
                a.getId(),
                a.getNome(),
                a.getPreco(),
                a.getTipo(),
                (a instanceof Model.Comida) ? ((Model.Comida)a).isVegetariano() ? "Sim" : "Não" : "-",
                a.isZero() ? "Sim" : "Não"
            });
        }
    }
    
    /**
     * Pega os itens selecionados e ADICIONA na lista 'carrinhoDeItens'.
     * NÃO abre a tela de pedido.
     */
    private void adicionarItensAoCarrinho() {
        int[] linhas = tela.getTbResultado().getSelectedRows();
        if (linhas == null || linhas.length == 0) {
            JOptionPane.showMessageDialog(tela, "Selecione pelo menos um alimento na tabela para adicionar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tela.getTbResultado().getModel();
        List<Alimento> itensRecemSelecionados = new ArrayList<>();

        for (int linha : linhas) {
            Object valId    = model.getValueAt(linha, 0);
            Object valNome  = model.getValueAt(linha, 1);
            Object valPreco = model.getValueAt(linha, 2);
            Object valTipo  = model.getValueAt(linha, 3);
            Object valVeg   = model.getValueAt(linha, 4);
            Object valZero  = model.getValueAt(linha, 5);

            int id          = (valId    instanceof Integer) ? (Integer) valId : Integer.parseInt(valId.toString());
            String nome     = (valNome  != null) ? valNome.toString() : "";
            double preco    = (valPreco instanceof Double) ? (Double) valPreco : Double.parseDouble(valPreco.toString());
            String tipo     = (valTipo  != null) ? valTipo.toString() : "";

            boolean isVeg = valVeg != null && valVeg.toString().equalsIgnoreCase("Sim");
            boolean isZero = valZero != null && valZero.toString().equalsIgnoreCase("Sim");

            Alimento a;
            if (tipo.toLowerCase().contains("bebida")) {
                Bebida b = new Bebida();
                b.setId(id); b.setNome(nome); b.setPreco(preco); b.setTipo(tipo);
                b.setAlcoolica(tipo.toLowerCase().contains("alcoolica"));
                b.setZero(isZero); 
                a = b;
            } else {
                Comida c = new Comida();
                c.setId(id); c.setNome(nome); c.setPreco(preco); c.setTipo(tipo);
                c.setVegetariano(isVeg); 
                c.setZero(isZero); 
                a = c;
            }
            itensRecemSelecionados.add(a);
        }

        // --- AQUI ESTÁ A LÓGICA ---
        // 1. Adiciona os novos itens ao carrinho principal
        this.carrinhoDeItens.addAll(itensRecemSelecionados);

        // 2. Atualiza o texto do botão "Ver Pedido"
        tela.getBtVerPedido().setText("Ver Pedido (" + this.carrinhoDeItens.size() + ")");
        
        // 3. Avisa o usuário
        JOptionPane.showMessageDialog(tela, itensRecemSelecionados.size() + " item(ns) adicionado(s) ao pedido!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Abre a tela GerenciarPedido, passando a lista 'carrinhoDeItens'.
     */
    private void verPedido() {
        if (this.carrinhoDeItens.isEmpty()) {
            JOptionPane.showMessageDialog(tela, "Seu pedido está vazio. Adicione itens primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Abre a tela GerenciarPedido com a lista completa do carrinho
        View.GerenciarPedido telaGerenciar = new View.GerenciarPedido();
        
        // Usa o construtor de NOVO pedido, passando a lista do carrinho
        // **IMPORTANTE**: Precisamos de um jeito de limpar o carrinho.
        // Vamos passar a *referência* do carrinho e o ControlePedido vai limpar.
        new Controller.ControlePedido(telaGerenciar, usuarioLogado, this.carrinhoDeItens);

        telaGerenciar.setLocationRelativeTo(tela);
        telaGerenciar.setVisible(true);
        
        // Quando a tela de gerenciar fechar, atualizamos o botão do carrinho
        telaGerenciar.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // Atualiza o contador (se o carrinho foi limpo, voltará a 0)
                tela.getBtVerPedido().setText("Ver Pedido (" + carrinhoDeItens.size() + ")");
            }
        });
    }
}