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
public class ControleExclusao {
    private Exclusao tela5;
    private Usuario usuario;
    
    public ControleExclusao(Exclusao tela5){
        this.tela5 = tela5;
        this.usuario = usuario;
    }
    
    public void remover(){
        int option = JOptionPane.showConfirmDialog(tela5, 
                                        "Deseja realmente excluir o cadastro?",
                                        "AVISO", JOptionPane.YES_NO_OPTION);
      if(option != 1){
          Conexao conexao = new Conexao();
          try{
              Connection conn = conexao.getConnection();
              UsuarioDAO dao = new UsuarioDAO(conn);
              dao.remover(usuario);
              JOptionPane.showMessageDialog(tela5, "Usuario removido com Sucesso!",
                                            "AVISO", JOptionPane.INFORMATION_MESSAGE);
          }catch(SQLException e){
              JOptionPane.showMessageDialog(tela5, "Falha de conexão!", "ERRO", 
                                                JOptionPane.ERROR_MESSAGE);
          }
      }          
    }
}
