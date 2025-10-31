/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import View.Login;
import View.Logado;
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
public class ControleLogin {
    private Login tela1;
    
    public ControleLogin(Login tela1) {
        this.tela1 = tela1;
    }
    
    public void LoginUsuario(){
        Usuario usuario = new Usuario(null, tela1.getTxtUsuario().getText(),
                                tela1.getTxtSenha().getText());
        Conexao conexao = new Conexao();
        try{
            Connection conn = conexao.getConnection();
            UsuarioDAO dao = new UsuarioDAO(conn);
            ResultSet res = dao.consultar(usuario);
            if(res.next()){
                JOptionPane.showMessageDialog(tela1, "Login Feito", "AVISO", 
                                              JOptionPane.INFORMATION_MESSAGE);
                String nome = res.getString("nome_usu");
                String usu = res.getString("usuario_usu");
                String senha = res.getString("senha_usu");
                Logado tela2 = new Logado (new Usuario(nome, usu, senha));
                tela2.setLocationRelativeTo(tela1);
                tela2.setVisible(true);
                tela1.dispose();
 
                
            }else{
                JOptionPane.showMessageDialog(tela1, "Login não efetuado",
                                             "ERRO" ,JOptionPane.ERROR_MESSAGE);
            }
        }catch(SQLException e ){
            JOptionPane.showMessageDialog(tela1, "Erro de Conexão", "ERRO",
                                            JOptionPane.ERROR_MESSAGE);
        }
    }
}
