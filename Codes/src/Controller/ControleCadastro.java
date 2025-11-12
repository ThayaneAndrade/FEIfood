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
//Define a classe de controle para a tela de Cadastro.
public class ControleCadastro {
    
    private Cadastro tela3;
    
    //Construtor que inicializa o controller com a tela de Cadastro.
    public ControleCadastro(Cadastro tela3) {
        //Armazena a referência da tela passada como parâmetro.
        this.tela3= tela3;
    }
    
    //Método público chamado pelo botão "Salvar" da View.
    public void salvarUsuario(){
        //Pega o texto digitado no campo de nome da tela.
        String nome = tela3.getTxtNome().getText();
        //Pega o texto digitado no campo de usuário da tela.
        String usuario = tela3.getTxtUsuario().getText();
        //Pega o texto digitado no campo de senha da tela.
        String senha = tela3.getTxtSenha().getText();
        //Cria um objeto 'Usuario' com os dados coletados.
        Usuario cliente = new Usuario(nome, usuario, senha);
        
        //Cria uma instância do objeto de conexão com o banco.
        Conexao conexao = new Conexao();
        //Inicia um bloco try-catch para lidar com possíveis erros de SQL.
        try{
            //Obtém a conexão ativa com o banco de dados.
            Connection conn = conexao.getConnection();
            //Cria o DAO (Objeto de Acesso a Dados) passando a conexão.
            UsuarioDAO dao = new UsuarioDAO(conn);
            //Chama o método 'inserir' do DAO para salvar o novo usuário no banco.
            dao.inserir(cliente);
            //Exibe uma mensagem de sucesso para o usuário.
            JOptionPane.showMessageDialog(tela3, "Usuario Cadastrado!", "AVISO",
                                            JOptionPane.INFORMATION_MESSAGE);
            //Fecha a tela de Cadastro (this.tela3) pois o cadastro foi concluído.
            tela3.dispose();
            //Cria uma nova instância da tela de Login.
            Login telaLogin = new Login();
            //Instancia o controller para a nova tela de login.
            new Controller.ControleLogin(telaLogin);
            //Torna a tela de Login visível para o usuário.
            telaLogin.setVisible(true);

            
        //Bloco 'catch' para capturar qualquer erro que o 'try' possa lançar.
        }catch(SQLException ex){
            //Exibe uma mensagem de erro caso o cadastro falhe.
            JOptionPane.showMessageDialog(tela3, "Usuário não Cadastrado!", 
                                            "ERRO",
                                            JOptionPane.ERROR_MESSAGE);
        }
    }
    }

