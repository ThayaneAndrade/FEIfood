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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

//Define a classe de controle para a tela principal de busca de alimentos.
public class ControleBuscaAlimento {

    //Guarda a referência da tela (View) que este controller gerencia.
    private final BuscaAlimento tela;
    //Guarda o objeto do usuário que está logado.
    private final Usuario usuarioLogado;
    
    //Define uma lista que funcionará como o "carrinho de compras" temporário.
    private List<Alimento> carrinhoDeItens; 

    //Construtor que inicializa o controller com a tela e o usuário.
    public ControleBuscaAlimento(BuscaAlimento tela, Usuario usuarioLogado) {
        this.tela = tela;
        this.usuarioLogado = usuarioLogado;
        //Inicializa o carrinho como uma nova lista vazia.
        this.carrinhoDeItens = new ArrayList<>();
  
    
        //Configura os "ouvintes" (listeners) para os botões da tela.
        //Define a ação do botão "Buscar".
        this.tela.getBtBuscar().addActionListener(e -> buscar());
        
        //Define a ação do botão "Adicionar".
        this.tela.getBtAdicionar().addActionListener(e -> adicionarItensAoCarrinho());
        //Define a ação do botão "Ver Pedido".
        this.tela.getBtVerPedido().addActionListener(e -> verPedido());
        
        //Define a ação do botão "Minha Conta", se ele existir.
        if (this.tela.getBtMinhaConta() != null) {
            this.tela.getBtMinhaConta().addActionListener(e -> abrirMinhaConta());
        }
        
        //Define a ação do botão "Meus Pedidos", se ele existir.
        if (this.tela.getBtMeusPedidos() != null) {
            this.tela.getBtMeusPedidos().addActionListener(e -> abrirMeusPedidos());
        }
    }
    
    
    //Método privado para abrir a tela de "Minha Conta".
    private void abrirMinhaConta() {
        //Cria uma nova instância da tela Logado, passando o usuário.
        Logado telaLogado = new Logado(usuarioLogado);
        //Centraliza a nova tela em relação à tela atual.
        telaLogado.setLocationRelativeTo(tela);
        //Define que fechar esta janela (Logado) apenas a removerá, sem fechar o app.
        telaLogado.setDefaultCloseOperation(javax.swing.WindowConstants.
                DISPOSE_ON_CLOSE);
        telaLogado.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                tela.setVisible(true);
            }
        });
        //Torna a tela visível.
        telaLogado.setVisible(true);
        tela.dispose();
    }
    
    //Método privado para abrir a tela de "Meus Pedidos".
    private void abrirMeusPedidos() {
           //Cria uma nova instância da tela MeusPedidos.
            MeusPedidos telaMeusPedidos = new MeusPedidos();
            //Cria o controller respectivo, passando a nova tela e o usuário.
            new ControleMeusPedidos(telaMeusPedidos, usuarioLogado);

            //Configura e exibe a tela de Meus Pedidos.
            telaMeusPedidos.setLocationRelativeTo(tela);
            telaMeusPedidos.setDefaultCloseOperation(javax.swing.WindowConstants
                    .DISPOSE_ON_CLOSE);
            telaMeusPedidos.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                tela.setVisible(true);
            }
        });
            telaMeusPedidos.setVisible(true);
            tela.dispose();
        }
    
    //Método privado chamado pelo botão "Buscar".
    private void buscar() {
        //Pega o texto da caixa de busca e remove espaços em branco.
        String termo = tela.getTxtBuscar().getText().trim();
       
        Connection conn = null;
        
        try {
            Conexao conexao = new Conexao();
            conn = conexao.getConnection();
            AlimentoDAO dao = new AlimentoDAO(conn);
            //Chama o método do DAO para buscar no banco, que retorna uma lista.
            List<Alimento> lista = dao.buscarPorNome(termo);
            //Chama o método para preencher a tabela da View com a lista.
            preencherTabela(lista);
            //Se a lista estiver vazia, avisa o usuário.
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(tela, 
                        "Nenhum alimento encontrado.", "Aviso", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
      
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(tela, "Erro ao buscar alimentos.\n" + 
                        ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        //Bloco 'finally' garante que a conexão será fechada, mesmo se der erro.
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) {}
            }
        }
    }

    //Método privado para popular a JTable na View.
    private void preencherTabela(List<Alimento> lista) {
        //Pega o "modelo" da tabela (a estrutura de dados dela).
        DefaultTableModel model = (DefaultTableModel) tela.getTbResultado().getModel();
        //Limpa todas as linhas antigas da tabela.
        model.setRowCount(0);
        //Itera sobre a lista de alimentos vinda do banco.
        for (Alimento a : lista) {
            //Adiciona uma nova linha na tabela com os dados do alimento.
            model.addRow(new Object[] {
                a.getId(),
                a.getNome(),
                a.getPreco(),
                a.getTipo(),
                //Checa se é 'Comida' para exibir "Sim/Não".
                (a instanceof Model.Comida) ? ((Model.Comida)a).isVegetariano() ? "Sim" : "Não" : "-",
                //Checa se o alimento é 'Zero'.
                a.isZero() ? "Sim" : "Não"
            });
        }
    }
    
    //Método chamado pelo botão "Adicionar ao Pedido".
    private void adicionarItensAoCarrinho() {
        //Pega todas as linhas que o usuário selecionou 
        int[] linhas = tela.getTbResultado().getSelectedRows();
        //Verifica se alguma linha foi realmente selecionada.
        if (linhas == null || linhas.length == 0) {
            JOptionPane.showMessageDialog(tela, "Selecione pelo menos um alimento na tabela para adicionar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        //Pega o modelo da tabela para ler os dados.
        DefaultTableModel model = (DefaultTableModel) tela.getTbResultado()
                .getModel();
        //Cria uma lista temporária para os itens recém-selecionados.
        List<Alimento> itensRecemSelecionados = new ArrayList<>();

        //Loop por cada linha selecionada.
        for (int linha : linhas) {
            //Lê os dados de cada coluna da linha selecionada.
            Object valId    = model.getValueAt(linha, 0);
            Object valNome  = model.getValueAt(linha, 1);
            Object valPreco = model.getValueAt(linha, 2);
            Object valTipo  = model.getValueAt(linha, 3);
            Object valVeg   = model.getValueAt(linha, 4);
            Object valZero  = model.getValueAt(linha, 5);

            //Converte os dados lidos para os tipos corretos.
            int id          = (valId    instanceof Integer) ? (Integer) valId : 
                    Integer.parseInt(valId.toString());
            String nome     = (valNome  != null) ? valNome.toString() : "";
            double preco    = (valPreco instanceof Double) ? (Double) valPreco :
                    Double.parseDouble(valPreco.toString());
            String tipo     = (valTipo  != null) ? valTipo.toString() : "";
            boolean isVeg = valVeg != null && valVeg.toString()
                    .equalsIgnoreCase("Sim");
            boolean isZero = valZero != null && valZero.toString()
                    .equalsIgnoreCase("Sim");

            //Recria o objeto Alimento (Bebida ou Comida) com base nos dados.
            Alimento a;
            if (tipo.toLowerCase().contains("bebida")) {
                Bebida b = new Bebida();
                b.setId(id);b.setNome(nome); b.setPreco(preco); b.setTipo(tipo);
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
            //Adiciona o item recriado na lista temporária.
            itensRecemSelecionados.add(a);
        }

        //1. Adiciona os itens da lista temporária no carrinho principal.
        this.carrinhoDeItens.addAll(itensRecemSelecionados);

        //2. Atualiza o texto do botão "Ver Pedido" com a nova quantidade de itens.
        tela.getBtVerPedido().setText("Ver Pedido (" + 
                                       this.carrinhoDeItens.size() + ")");
        
        //3. Avisa o usuário que os itens foram adicionados.
        JOptionPane.showMessageDialog(tela, itensRecemSelecionados.size() + " item(ns) adicionado(s) ao pedido!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    //Método chamado pelo botão "Ver Pedido".
    private void verPedido() {
        //Verifica se o carrinho está vazio.
        if (this.carrinhoDeItens.isEmpty()) {
            JOptionPane.showMessageDialog(tela, "Seu pedido está vazio. "
                    + "Adicione itens primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //Cria a tela GerenciarPedido.
        View.GerenciarPedido telaGerenciar = new View.GerenciarPedido();
        
        //Cria o controller de GerenciarPedido, passando a *referência* do carrinho.
        new Controller.ControlePedido(telaGerenciar, usuarioLogado, this.carrinhoDeItens);

        telaGerenciar.setLocationRelativeTo(tela);
        tela.dispose();
        telaGerenciar.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // Atualiza o contador do carrinho
                tela.getBtVerPedido().setText("Ver Pedido (" + carrinhoDeItens.size() + ")");
                tela.setVisible(true);
            }
        });
         telaGerenciar.setVisible(true);
         tela.setVisible(false);
         
        telaGerenciar.addWindowListener(new java.awt.event.WindowAdapter() {
            //Este método é chamado quando a tela GerenciarPedido é fechada.
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                //Atualiza o texto do botão (ex: se o carrinho foi limpo, volta a 0).
                tela.getBtVerPedido().setText("Ver Pedido (" + 
                        carrinhoDeItens.size() + ")");
            }
        });
    }
}