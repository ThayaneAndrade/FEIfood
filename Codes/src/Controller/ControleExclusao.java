/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import View.Exclusao;
import dao.UsuarioDAO;
import dao.Conexao;
import Model.Usuario;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author thaya
 */
//Define a classe de controle para a tela de confirmação de exclusão.
public class ControleExclusao {
    //Guarda a referência da tela que este controller gerencia.
    private Exclusao tela5;
    //Guarda o objeto do usuário que será excluído.
    private Usuario usuario;
    
    //Construtor que inicializa o controller com a tela e o usuário.
    public ControleExclusao(Exclusao tela5, Usuario usuario){
        this.tela5 = tela5;
        this.usuario = usuario;
    }
    
    //Método público chamado pela View (botão "Excluir").
    public void remover(){
        //Exibe uma caixa de diálogo de confirmação (Sim/Não).
        int option = JOptionPane.showConfirmDialog(tela5, 
                                        "Deseja realmente excluir o cadastro?",
                                        "AVISO", JOptionPane.YES_NO_OPTION);
      if(option != 0){
          //Cria uma instância do objeto de conexão.
          Conexao conexao = new Conexao();
          //Inicia um bloco try-catch para lidar com erros de SQL.
          try (Connection conn = conexao.getConnection()) {
            UsuarioDAO dao = new UsuarioDAO(conn);
            dao.remover(usuario);
            JOptionPane.showMessageDialog(tela5, "Usuario removido com Sucesso!",
                                          "AVISO", JOptionPane.INFORMATION_MESSAGE);
            
 
            
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(tela5, "Falha de conexão! " + e.getMessage(), "ERRO", 
                                              JOptionPane.ERROR_MESSAGE);
            }
        } 
    }
}

    