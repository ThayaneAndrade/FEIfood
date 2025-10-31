/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import View.Alteracao;
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
public class ControleAlteracao {
    private Alteracao tela6;
    private Usuario usuario;
    
    public ControleAlteracao(Alteracao tela6, Usuario usuario){
        this.tela6 = tela6;
        this.usuario = usuario;
    }
    
    public void atualizar(){
        String cliente = usuario.getUsuario();
        String novaSenha=  tela6.getTxtNovaSenha().getText();
        Usuario usuario = new Usuario("", cliente, novaSenha);
        Conexao conexao = new Conexao();
        try{
              Connection conn = conexao.getConnection();
              UsuarioDAO dao = new UsuarioDAO(conn);
              dao.atualizar(usuario);
              JOptionPane.showMessageDialog(tela6, "Senha de Usuário atualizada com Sucesso!",
                                            "AVISO", JOptionPane.INFORMATION_MESSAGE);
          }catch(SQLException e){
              JOptionPane.showMessageDialog(tela6, "Falha de conexão!", "ERRO", 
                                                JOptionPane.ERROR_MESSAGE);
          }
      }          
    }

