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
import View.BuscaAlimento;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;


/**
 *
 * @author thaya
 */
//Define a classe de controle para a tela de Login.
public class ControleLogin {
    
    private Login tela1;
    
    //Construtor que inicializa o controller com a tela de Login.
    public ControleLogin(Login tela1) {
        this.tela1 = tela1;
    }
    
    //Método principal chamado pelo botão "Entrar" da View.
    public void LoginUsuario(){
        //Pega os textos dos campos "Usuario" e "Senha" da tela.
        Usuario usuario = new Usuario(null, tela1.getTxtUsuario().getText(),
                                tela1.getTxtSenha().getText());
        //Cria uma instância do objeto de conexão.
        Conexao conexao = new Conexao();
     
        try (Connection conn = conexao.getConnection()){
            UsuarioDAO dao = new UsuarioDAO(conn);
            //Executa a consulta (SELECT) no banco para verificar se o usuário existe.
            ResultSet res = dao.consultar(usuario);
            
            //Verifica se o banco de dados retornou alguma linha (usuário encontrado).
            if(res.next()){
                //Avisa o usuário que o login foi bem-sucedido.
                JOptionPane.showMessageDialog(tela1, "Login Feito", "AVISO", 
                                              JOptionPane.INFORMATION_MESSAGE);
                //Pega os dados completos do usuário que veio do banco.
                String nome = res.getString("nome_usu");
                String usu = res.getString("usuario_usu");
                String senha = res.getString("senha_usu");
                
                //Cria um objeto Usuario completo para manter na sessão.
                Usuario usuarioLogado = new Usuario(nome,usu,senha);
                //Define o ID do usuário no objeto (essencial para futuros pedidos).
                usuarioLogado.setId(res.getInt("id_usu"));
                
                //Cria a próxima tela (BuscaAlimento), que é a tela principal.
                BuscaAlimento telaBusca = new BuscaAlimento();
                //Cria o controller da tela principal, passando o usuário logado.
                new ControleBuscaAlimento(telaBusca, usuarioLogado);
                
                //Posiciona a nova tela no centro.
                telaBusca.setLocationRelativeTo(tela1);
                //Torna a nova tela visível.
                telaBusca.setVisible(true);
                //Fecha a tela de login (tela1).
                tela1.dispose();

                
            //Se 'res.next()' for falso, o usuário ou senha estão errados.
            }else{
                //Avisa o usuário que o login falhou.
                JOptionPane.showMessageDialog(tela1, "Login não efetuado",
                                             "ERRO" ,JOptionPane.ERROR_MESSAGE);
            }
        }catch(SQLException e ){
            //Exibe uma mensagem de erro.
            JOptionPane.showMessageDialog(tela1, "Erro de Conexão: " + e.getMessage(), "ERRO",
                                            JOptionPane.ERROR_MESSAGE);
        }
    }
}