/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import Model.Usuario;
import Model.Alimento;
import Model.Bebida;
import Model.Comida;
import Model.Pedido;
import Model.Usuario;
import View.GerenciarPedido;
import View.MeusPedidos; 
import dao.Conexao;
import dao.PedidoDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author thaya
 */
//Define a classe de controle para a tela "MeusPedidos".
public class ControleMeusPedidos {
  
    private final MeusPedidos tela;
    //Guarda o objeto do usuário que está logado.
    private final Usuario usuarioLogado;
    //Guarda uma lista dos pedidos carregados do banco para fácil acesso.
    private final List<Pedido> pedidosCarregados;
    
    //Construtor que inicializa o controller com a tela e o usuário.
    public ControleMeusPedidos(MeusPedidos tela, Usuario usuarioLogado) {
        this.tela = tela;
        this.usuarioLogado = usuarioLogado;
        //Inicializa a lista de pedidos.
        this.pedidosCarregados = new ArrayList<>();
       
        //Chama o método que configura os listeners dos botões.
        configurarListeners();
        
        this.tela.addWindowListener(new java.awt.event.WindowAdapter() {
            //Este método é chamado assim que a janela é aberta.
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                //Chama o método para carregar os pedidos do banco.
                carregarPedidos();
            }
            
        });
    }
    //Método privado para configurar as ações de todos os botões da tela.
    private void configurarListeners() {
            this.tela.getBtAvaliar().addActionListener(e -> avaliarPedido());
           
            this.tela.getBtExcluir().addActionListener(e -> excluirPedido());
        
            this.tela.getBtVerItens().addActionListener(e -> verItensDoPedido());
         
            this.tela.getBtVoltar().addActionListener(e -> tela.dispose());
        }
    //Método que busca os pedidos no banco e preenche a tabela.
    public void carregarPedidos() {
        //Pega o modelo de dados da tabela.
        DefaultTableModel model = (DefaultTableModel) tela.getTblPedidos().getModel();
        //Limpa a tabela de resultados anteriores.
        model.setRowCount(0);
        //Limpa a lista interna de pedidos.
        pedidosCarregados.clear();

        Conexao conexao = new Conexao();
        
        try (Connection conn = conexao.getConnection()) {
            
            PedidoDAO dao = new PedidoDAO(conn);
            
            //Busca no banco todos os pedidos do usuário logado.
            ResultSet rs = dao.consultarPorUsuario(usuarioLogado);

            //Itera sobre cada linha (pedido) que o banco retornou.
            while (rs.next()) {
                //Cria um novo objeto Pedido.
                Pedido p = new Pedido();
                //Define os dados do pedido com base nos resultados do banco.
                p.setId(rs.getInt("id_ped"));
                p.setDataPed(rs.getDate("data_ped"));
                
                //Pega a avaliação (pode ser nula no banco).
                Integer avaliacao = (Integer) rs.getObject("avaliacao_ped");
                p.setAvaliacaoPed(avaliacao);
                
                //Define o usuário do pedido.
                p.setUsuario(usuarioLogado); 
                //Adiciona o pedido na lista interna para referência futura.
                pedidosCarregados.add(p); 
                
                //Adiciona a linha na tabela da interface.
                model.addRow(new Object[]{
                    p.getId(),
                    p.getDataPed(),
                    //Mostra a nota ou "Não avaliado" se for nula.
                    (avaliacao != null ? avaliacao.toString() : "Não avaliado")
                });
            }
        
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(tela,
                    "Erro ao carregar pedidos: " + e.getMessage(),
                    "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    //Método privado para pegar o pedido selecionado na tabela.
    private Pedido getPedidoSelecionado() {
        //Pega o índice da linha que o usuário clicou.
        int linha = tela.getTblPedidos().getSelectedRow();
        //Se nenhuma linha estiver selecionada, linha é -1.
        if (linha == -1) {
            //Avisa o usuário e retorna nulo.
            JOptionPane.showMessageDialog(tela,
                    "Por favor, selecione um pedido na tabela primeiro.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        //Retorna o objeto Pedido da lista interna, usando o índice da linha.
        return pedidosCarregados.get(linha);
    }

    
    //Método chamado pelo botão "Avaliar".
    private void avaliarPedido() {
        //Pega o pedido que o usuário selecionou na tabela.
        Pedido pedido = getPedidoSelecionado();
        //Se for nulo (ninguém selecionado), para a execução.
        if (pedido == null) {
            return; 
        }

        //Pega o valor selecionado no ComboBox (ex: "5").
        String notaStr = tela.getCmbNota().getSelectedItem().toString();
        //Converte a string "5" para o número inteiro 5.
        int nota = Integer.parseInt(notaStr); 

        //Abre uma nova conexão para esta transação.
        Conexao conexao = new Conexao();
        try (Connection conn = conexao.getConnection()) {
            //Cria o DAO.
            PedidoDAO dao = new PedidoDAO(conn);
            
            //Chama o método do DAO que atualiza *apenas* a avaliação.
            dao.atualizarAvaliacao(pedido.getId(), nota);

            //Mostra mensagem de sucesso.
            JOptionPane.showMessageDialog(tela,
                    "Pedido " + pedido.getId() + 
                    " avaliado com nota " + nota + "!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            
            //Recarrega a tabela para mostrar a nova nota.
            carregarPedidos();

        //Captura erros de SQL.
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(tela,
                    "Erro ao avaliar pedido: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    //Método chamado pelo botão "Excluir".
    private void excluirPedido() {
        //Pega o pedido selecionado.
        Pedido pedido = getPedidoSelecionado();
        if (pedido == null) {
            return;
        }

        //Pede confirmação ao usuário antes de apagar.
        int opc = JOptionPane.showConfirmDialog(tela,
                "Tem certeza que deseja excluir o pedido "
                        + pedido.getId() + "?",
                "Confirmação de Exclusão",
                JOptionPane.YES_NO_OPTION);

        //Se o usuário clicar em "Não" (opção 1), o método para.
        if (opc != JOptionPane.YES_OPTION) { // YES_OPTION é 0
            return;
        }

        //Abre a conexão.
        Conexao conexao = new Conexao();
        try (Connection conn = conexao.getConnection()) {
            //Cria o DAO.
            PedidoDAO dao = new PedidoDAO(conn);
            //Chama o método 'remover' do DAO.
            dao.remover(pedido);

            //Mostra mensagem de sucesso.
            JOptionPane.showMessageDialog(tela,
                    "Pedido " + pedido.getId() + " excluído com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            //Recarrega a tabela para remover a linha excluída.
            carregarPedidos(); 

        //Captura erros de SQL.
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(tela,
                    "Erro ao excluir pedido: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    //Método chamado pelo botão "Ver Itens".
    private void verItensDoPedido() {
        //Pega o pedido selecionado.
        Pedido pedido = getPedidoSelecionado();
        if (pedido == null) {
            return; 
        }

        //Cria uma lista vazia para guardar os alimentos desse pedido.
        List<Alimento> itensDoPedido = new ArrayList<>();
        //Abre a conexão.
        Conexao conexao = new Conexao();

        try (Connection conn = conexao.getConnection()) {
            //Cria o DAO.
            PedidoDAO dao = new PedidoDAO(conn);
            
            //Chama o método que busca os itens de um pedido específico.
            ResultSet rs = dao.consultarItens(pedido);

            //Itera sobre cada item (alimento) que o banco retornou.
            while (rs.next()) {
                String tipo = rs.getString("tipo_ali");
                Alimento a;

                //Recria os objetos Comida/Bebida com base nos dados (Polimorfismo).
                if (tipo != null && (tipo.toLowerCase().contains("bebida"))) {
                    Bebida b = new Bebida();
                    b.setAlcoolica(tipo.toLowerCase().contains("alcoolica"));
                    b.setZero(rs.getBoolean("zero")); 
                    a = b;
                } else {
                    Comida c = new Comida();
                    c.setVegetariano(rs.getBoolean("vegetariano")); 
                    c.setZero(rs.getBoolean("zero"));
                    a = c;
                }

                //Define os dados comuns do Alimento.
                a.setId(rs.getInt("id_ali"));
                a.setNome(rs.getString("nome_ali"));
                a.setPreco(rs.getDouble("preco_ali"));
                a.setTipo(tipo);

                //Adiciona o alimento recriado à lista.
                itensDoPedido.add(a);
            }

        //Captura erros de SQL.
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(tela,
                    "Erro ao carregar itens do pedido: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Cria a tela GerenciarPedido.
        GerenciarPedido telaGerenciar = new GerenciarPedido();
        
        //Chama o construtor de "Pedido Existente" do ControlePedido.
        new ControlePedido(telaGerenciar, usuarioLogado, pedido, itensDoPedido);
        
        //Exibe a tela.
        telaGerenciar.setLocationRelativeTo(tela);
        telaGerenciar.setVisible(true);
    }
}