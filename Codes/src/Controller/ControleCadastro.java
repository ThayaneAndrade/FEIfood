/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import dao.UsuarioDAO;
import dao.Conexao;
import Model.Usuario;
import View.Cadastro;
import View.Login;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.text.View;
/**
 *
 * @author thaya
 */
public class ControleCadastro {
    private Cadastro tela3;
    
    public ControleCadastro(Cadastro tela3) {
        this.tela3= tela3;
    }
    
    public void salvarUsuario(){
        String nome = tela3.getTxtNome().getText();
        String usuario = tela3.getTxtUsuario().getText();
        String senha = tela3.getTxtSenha().getText();
        Usuario cliente = new Usuario(nome, usuario, senha);
        
        Conexao conexao = new Conexao();
        try{
            Connection conn = conexao.getConnection();
            UsuarioDAO dao = new UsuarioDAO(conn);
            dao.inserir(cliente);
            JOptionPane.showMessageDialog(tela3, "Usuario Cadastrado!", "AVISO",
                                            JOptionPane.INFORMATION_MESSAGE);
            tela3.dispose();
            Login telaLogin = new Login();
            new Controller.ControleLogin(telaLogin);
            telaLogin.setVisible(true);
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(tela3, "Usuário não Cadastrado!", "ERRO",
                                            JOptionPane.ERROR_MESSAGE);
        }
    }
    }

