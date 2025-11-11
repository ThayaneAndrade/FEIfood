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
public class ControleMeusPedidos {
    private final MeusPedidos tela;
    private final Usuario usuarioLogado;
    private final List<Pedido> pedidosCarregados;
    
    public ControleMeusPedidos(MeusPedidos tela, Usuario usuarioLogado) {
        this.tela = tela;
        this.usuarioLogado = usuarioLogado;
        this.pedidosCarregados = new ArrayList<>();
       
        configurarListeners();
        
        this.tela.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                carregarPedidos();
            }
            
        });
    }
    private void configurarListeners() {
            this.tela.getBtAvaliar().addActionListener(e -> avaliarPedido());
            this.tela.getBtExcluir().addActionListener(e -> excluirPedido());
            this.tela.getBtVerItens().addActionListener(e -> verItensDoPedido());
            this.tela.getBtVoltar().addActionListener(e -> tela.dispose());
        }
    public void carregarPedidos() {
        // Limpa a tabela e a lista interna
        DefaultTableModel model = (DefaultTableModel) tela.getTblPedidos().getModel();
        model.setRowCount(0);
        pedidosCarregados.clear();

        Conexao conexao = new Conexao();
        try (Connection conn = conexao.getConnection()) {
            PedidoDAO dao = new PedidoDAO(conn);
            
            // Usa o método do DAO para buscar pedidos por usuário
            ResultSet rs = dao.consultarPorUsuario(usuarioLogado); //

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getInt("id_ped"));
                p.setDataPed(rs.getDate("data_ped"));
                
                // Trata a avaliação, que pode ser NULA no banco
                Integer avaliacao = (Integer) rs.getObject("avaliacao_ped");
                p.setAvaliacaoPed(avaliacao);
                
                p.setUsuario(usuarioLogado); // Define o usuário
                pedidosCarregados.add(p); // Adiciona na lista interna

                // Adiciona na tabela da tela
                model.addRow(new Object[]{
                    p.getId(),
                    p.getDataPed(),
                    (avaliacao != null ? avaliacao.toString() : "Não avaliado")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(tela,
                    "Erro ao carregar pedidos: " + e.getMessage(),
                    "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Pega o pedido selecionado na JTable.
     * @return O objeto Pedido selecionado, ou null se ninguém for selecionado.
     */
    private Pedido getPedidoSelecionado() {
        int linha = tela.getTblPedidos().getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(tela,
                    "Por favor, selecione um pedido na tabela primeiro.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        // Retorna o pedido da nossa lista interna usando o índice da linha
        return pedidosCarregados.get(linha);
    }

    /**
     * Ação do botão "Avaliar".
     * Pega a nota do ComboBox e atualiza o pedido no banco.
     */
    private void avaliarPedido() {
        Pedido pedido = getPedidoSelecionado();
        if (pedido == null) {
            return; // Usuário não selecionou um pedido
        }

        // Pega a nota da ComboBox (ex: "5 estrelas")
        String notaStr = tela.getCmbNota().getSelectedItem().toString();
        int nota = Integer.parseInt(notaStr); // Converte para int

        Conexao conexao = new Conexao();
        try (Connection conn = conexao.getConnection()) {
            PedidoDAO dao = new PedidoDAO(conn);

            // **IMPORTANTE**: Precisamos de um novo método no PedidoDAO
            // que SÓ atualiza a avaliação.
            dao.atualizarAvaliacao(pedido.getId(), nota);

            JOptionPane.showMessageDialog(tela,
                    "Pedido " + pedido.getId() + " avaliado com nota " + nota + "!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            // Recarrega a tabela para mostrar a nova nota
            carregarPedidos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(tela,
                    "Erro ao avaliar pedido: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Ação do botão "Excluir".
     * Remove o pedido do banco de dados (e seus itens, via DAO).
     */
    private void excluirPedido() {
        Pedido pedido = getPedidoSelecionado();
        if (pedido == null) {
            return; // Usuário não selecionou
        }

        int opc = JOptionPane.showConfirmDialog(tela,
                "Tem certeza que deseja excluir o pedido " + pedido.getId() + "?",
                "Confirmação de Exclusão",
                JOptionPane.YES_NO_OPTION);

        if (opc != JOptionPane.YES_OPTION) {
            return;
        }

        Conexao conexao = new Conexao();
        try (Connection conn = conexao.getConnection()) {
            PedidoDAO dao = new PedidoDAO(conn);
            dao.remover(pedido); //

            JOptionPane.showMessageDialog(tela,
                    "Pedido " + pedido.getId() + " excluído com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            carregarPedidos(); // Recarrega a tabela

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(tela,
                    "Erro ao excluir pedido: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Ação do botão "Ver Itens" (ou "Editar").
     * Carrega os itens do pedido e abre a tela GerenciarPedido.
     */
    private void verItensDoPedido() {
        Pedido pedido = getPedidoSelecionado();
        if (pedido == null) {
            return; // Não selecionou
        }

        List<Alimento> itensDoPedido = new ArrayList<>();
        Conexao conexao = new Conexao();

        try (Connection conn = conexao.getConnection()) {
            PedidoDAO dao = new PedidoDAO(conn);
            
            // **IMPORTANTE**: O SQL em 'consultarItens' precisa ser atualizado
            ResultSet rs = dao.consultarItens(pedido); //

            while (rs.next()) {
                String tipo = rs.getString("tipo_ali");
                Alimento a;

                // Recria os objetos Comida/Bebida com base nos dados do banco
                if (tipo != null && (tipo.toLowerCase().contains("bebida"))) {
                    Bebida b = new Bebida();
                    b.setAlcoolica(tipo.toLowerCase().contains("alcoolica"));
                    b.setZero(rs.getBoolean("zero")); // Assume que 'zero' foi adicionado ao SQL
                    a = b;
                } else {
                    Comida c = new Comida();
                    c.setVegetariano(rs.getBoolean("vegetariano")); // Assume que 'vegetariano' foi adicionado
                    c.setZero(rs.getBoolean("zero"));
                    a = c;
                }

                a.setId(rs.getInt("id_ali"));
                a.setNome(rs.getString("nome_ali"));
                a.setPreco(rs.getDouble("preco_ali"));
                a.setTipo(tipo);

                itensDoPedido.add(a);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(tela,
                    "Erro ao carregar itens do pedido: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Se carregou com sucesso, abre a tela de GerenciarPedido
        GerenciarPedido telaGerenciar = new GerenciarPedido();
        
        new ControlePedido(telaGerenciar, usuarioLogado, pedido, itensDoPedido);
        
        telaGerenciar.setLocationRelativeTo(tela);
        telaGerenciar.setVisible(true);
    }
}
