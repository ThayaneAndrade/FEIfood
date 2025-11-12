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

//Define a classe de controle para a tela GerenciarPedido.
public class ControlePedido {
    //Guarda a referência da tela (View) que este controller gerencia.
    private final GerenciarPedido tela;
    //Guarda o objeto do usuário que está logado.
    private final Usuario usuarioLogado;
    //Guarda a lista de itens *neste* pedido (uma cópia do carrinho).
    private final List<Alimento> itensPedido;
    //Guarda a referência do carrinho original (da tela BuscaAlimento).
    private final List<Alimento> carrinhoOriginal;
    //Guarda a instância do pedido que está sendo criado ou editado.
    private Pedido pedidoAtual = new Pedido();

    //Construtor 1: Chamado ao criar um NOVO pedido (vindo da BuscaAlimento).
    public ControlePedido(GerenciarPedido tela, Usuario usuarioLogado,
            List<Alimento> carrinho) {
        this.tela = tela;
        this.usuarioLogado = usuarioLogado;
        //Armazena a referência do carrinho original (para poder limpá-lo ao salvar).
        this.carrinhoOriginal = carrinho;
        //Cria uma *nova* lista (cópia) baseada no carrinho para esta tela.
        this.itensPedido = new ArrayList<>(this.carrinhoOriginal); 
        
        //Define os dados iniciais do objeto Pedido.
        this.pedidoAtual.setUsuario(usuarioLogado);
        this.pedidoAtual.setItens(this.itensPedido);

        //Configura Interface para um novo pedido.
        this.tela.getLblUsuario().setText(usuarioLogado.getNome());
        //Define a data atual no campo de data.
        this.tela.getTxtData().setText(new Date
        (System.currentTimeMillis()).toString());
        //Trava o campo de data para não ser editável.
        this.tela.getTxtData().setEditable(false);
        
        //Define o texto da label de ID para "Novo Pedido".
        this.tela.getLblIdPedido().setText("Novo Pedido");
        //Esconde o botão "Excluir", pois o pedido ainda não existe.
        this.tela.getBtExcluir().setVisible(false);
        //Mostra o botão "Salvar".
        this.tela.getBtSalvar().setVisible(true);

        //Chama os métodos para preencher a tabela e ligar os botões.
        atualizarTabela();
        configurarListeners();
        atualizarPrecoTotal();
    }

    //Construtor 2: Chamado ao carregar um pedido EXISTENTE (vindo de MeusPedidos).
    public ControlePedido(GerenciarPedido tela, Usuario usuarioLogado, Pedido 
            pedidoExistente, List<Alimento> itensDoPedido) {
        this.tela = tela;
        this.usuarioLogado = usuarioLogado;
        //Armazena o pedido carregado do banco
        this.pedidoAtual = pedidoExistente;
        //Define o carrinho original como não aplicavel nessa situação
        this.carrinhoOriginal = null; 
        //Cria a lista de itens com base nos itens passados
        this.itensPedido = new ArrayList<>(itensDoPedido);
        this.pedidoAtual.setItens(this.itensPedido);

        //Configura a interface para um pedido existente.
        this.tela.getLblUsuario().setText(usuarioLogado.getNome());
        //Define o ID na label.
        this.tela.getLblIdPedido().setText(String.valueOf(pedidoAtual.getId()));
        //Define a data do pedido (vinda do banco).
        this.tela.getTxtData().setText(pedidoAtual.getDataPed().toString());
        //Trava o campo de data.
        this.tela.getTxtData().setEditable(false);
        
        //Esconde o botão "Salvar".
        this.tela.getBtSalvar().setVisible(false);
        //Mostra o botão "Excluir" 
        this.tela.getBtExcluir().setVisible(true);
        
        // ESCONDE O BOTÃO REMOVER ITEM
        if (this.tela.getBtRemoverItem() != null) {
        this.tela.getBtRemoverItem().setVisible(false); 
    }

        //Chama os métodos para preencher a tabela e ligar os botões.
        atualizarTabela(); 
        configurarListeners();
        atualizarPrecoTotal();
    }
    
    //Método privado que configura as ações de todos os botões da tela.
    private void configurarListeners() {
        //Define a ação do botão "Salvar".
        this.tela.getBtSalvar().addActionListener(e -> salvar());
        //Define a ação do botão "Excluir".
        this.tela.getBtExcluir().addActionListener(e -> excluir());
        //Define a ação do botão "Voltar".
        this.tela.getBtVoltar().addActionListener(e -> voltar());
        
        //Define a ação do botão "Remover Item", se ele existir.
        if (this.tela.getBtRemoverItem() != null) {
            this.tela.getBtRemoverItem().addActionListener(e -> removerItem());
        }
    }

    //Método privado para popular a JTable na View.
    private void atualizarTabela() {
        //Pega o "modelo" da tabela (a estrutura de dados dela).
        DefaultTableModel model = (DefaultTableModel) tela.getTblItens().
                getModel();
        //Limpa todas as linhas antigas da tabela.
        model.setRowCount(0); 
        //Itera sobre a lista de alimentos deste pedido.
        for (Alimento a : itensPedido) {
            //Adiciona uma nova linha na tabela com os dados do alimento.
            model.addRow(new Object[] {
                a.getId(),
                a.getNome(),
                a.getPreco(),
                a.getTipo(),
            });
        }
    }
    
    //Método que calcula o preço total
    private void atualizarPrecoTotal() {
        //Inicia o total como 0.
        double total = 0.0;
        //Itera sobre a lista de alimentos.
        for (Alimento a : itensPedido) {
            //Soma o preço base.
            total += a.getPreco(); 
            
            //Verifica se o objeto 'a' implementa a interface 'ImpostoAlcool'.
            if (a instanceof ImpostoAlcool) {
                //Se sim, faz o "cast" para a interface.
                ImpostoAlcool itemComImposto = (ImpostoAlcool) a;
                //Chama o método da interface para calcular o imposto adicional.
                total += itemComImposto.calcularImpostoAlcool();
            }
        }
        //Formata o preço final como R$ 0,00.
        String precoFormatado = String.format("Total (com impostos): R$ %.2f", 
                total);
        //Atualiza a label na tela, se ela existir.
        if (tela.getLblPrecoTotal() != null) {
            tela.getLblPrecoTotal().setText(precoFormatado); 
        }
    }

    //Método que lê e valida a data do campo de texto.
    private Date lerDataDaTela() {
        //Pega o texto e remove espaços.
        String dataStr = tela.getTxtData().getText().trim();
        //Verifica se o campo está vazio.
        if (dataStr.isEmpty()) {
            JOptionPane.showMessageDialog(tela, "O campo Data não pode estar "
                    + "vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        //Tenta converter a String para um formato SQL Date (AAAA-MM-DD).
        try {
            return Date.valueOf(dataStr); 
        //Se o formato estiver errado , lança exceção.
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(tela, "Data inválida. Use o formato "
                    + "AAAA-MM-DD.", "Erro de Formato", 
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    //Método chamado pelo botão "Remover Item".
    private void removerItem() {
    int linhaSelecionada = tela.getTblItens().getSelectedRow();
    if (linhaSelecionada == -1) {
        JOptionPane.showMessageDialog(tela, "Selecione um item da tabela "
                + "para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // 1. Pega o objeto Alimento que o utilizador quer apagar (da cópia local)
    Alimento itemParaRemover = itensPedido.get(linhaSelecionada);
    
    // 2. Remove o item da cópia local (para atualizar a tabela visual)
    itensPedido.remove(linhaSelecionada);
    
    // 3. Remove o mesmo item do carrinho principal
    if (carrinhoOriginal != null) {
        carrinhoOriginal.remove(itemParaRemover);
    }
    
    // 4. Atualiza a interface
    atualizarTabela();
    atualizarPrecoTotal();
    JOptionPane.showMessageDialog(tela, "Item removido do pedido.", "Aviso", 
            JOptionPane.INFORMATION_MESSAGE);
}

    //Método chamado pelo botão "Salvar" (Fazer Pedido).
    private void salvar() {
        //Verifica se a lista de itens está vazia.
        if (itensPedido.isEmpty()) {
            JOptionPane.showMessageDialog(tela,
                    "Não é possível salvar um pedido vazio.", "Aviso", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        //Lê e valida a data da tela.
        Date data = lerDataDaTela();
        if (data == null) {
            return; 
        }

        //Preenche o objeto 'pedidoAtual' com os dados finais.
        pedidoAtual.setUsuario(usuarioLogado);
        pedidoAtual.setDataPed(data);
        pedidoAtual.setAvaliacaoPed(null); //Novo pedido, sem avaliação.
        pedidoAtual.setItens(new ArrayList<>(itensPedido)); //Define os itens.

        //Cria a conexão e usa try-with-resources.
        Conexao conexao = new Conexao();
        Connection conn = null;
        try {
            conn = conexao.getConnection();
            //Cria o DAO.
            PedidoDAO dao = new PedidoDAO(conn);
            //Chama o método 'inserir' do DAO (executa INSERT).
            dao.inserir(pedidoAtual);

            //Atualiza a label de ID na tela com o ID gerado pelo banco.
            tela.getLblIdPedido().setText(String.valueOf(pedidoAtual.getId()));

            //Mostra mensagem de sucesso.
            JOptionPane.showMessageDialog(tela, "Pedido salvo com sucesso! (ID: " 
                    + pedidoAtual.getId() + ")", "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            //Se o carrinho original (da tela BuscaAlimento) existir...
            if (carrinhoOriginal != null) {
                //...limpa o carrinho original.
                carrinhoOriginal.clear();
            }
            //Limpa também a lista local.
            this.itensPedido.clear();
            //Atualiza a tabela (agora vazia).
            atualizarTabela();
            
            //Esconde o botão "Salvar".
            tela.getBtSalvar().setVisible(false);
            //Mostra o botão "Excluir" (caso o usuário se arrependa).
            tela.getBtExcluir().setVisible(true);
            
            //Fecha a tela de GerenciarPedido automaticamente.
            tela.dispose(); 

        //Captura erros de SQL (ex: falha no INSERT).
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(tela, "Erro ao salvar pedido.\n" + 
                    ex.getMessage(), "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
        //Garante que a conexão seja fechada (necessário se não usar try-with-resources).
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) {}
            }
        }
    }

    //Método chamado pelo botão "Excluir".
    private void excluir() {
        //Pega o ID do pedido em memória.
        int id = pedidoAtual.getId();
        //Pede confirmação ao usuário.
        int opc = JOptionPane.showConfirmDialog(
                tela,
                "Deseja realmente excluir o pedido " + id + "?\nEsta ação "
                        + "não pode ser desfeita.",
                "Confirmação",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        //Se o usuário NÃO clicou "Sim" (YES_OPTION = 0), para o método.
        if (opc != JOptionPane.YES_OPTION) {
            return;
        }
        //Define o ID no objeto 
        pedidoAtual.setId(id);

        //Abre a conexão.
        Conexao conexao = new Conexao();
        Connection conn = null;
        try {
            conn = conexao.getConnection();
            //Cria o DAO.
            PedidoDAO dao = new PedidoDAO(conn);
            //Chama o método 'remover' do DAO (executa DELETE).
            dao.remover(pedidoAtual);

            //Mostra mensagem de sucesso.
            JOptionPane.showMessageDialog(tela, "Pedido " + id +
                    " excluído com sucesso!", "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            //Fecha a tela de GerenciarPedido.
            tela.dispose(); 

        //Captura erros de SQL.
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(tela, "Erro ao excluir pedido.\n" + 
                    ex.getMessage(), "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
        //Garante o fechamento da conexão.
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) {}
            }
        }
    }

    //Método chamado pelo botão "Voltar".
    private void voltar() {
        //Apenas fecha a tela atual.
        tela.dispose();
    }
}