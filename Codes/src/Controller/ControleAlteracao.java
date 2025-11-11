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
//Define a classe de controle para a tela de alteração de senha.
public class ControleAlteracao {
    private Alteracao tela6;
    private Usuario usuario;
    
    //Construtor que inicializa o controller com a tela e o usuário.
    public ControleAlteracao(Alteracao tela6, Usuario usuario){
        this.tela6 = tela6;
        this.usuario = usuario;
    }
    
    //Método público chamado pela View (botão "Salvar") para iniciar a atualização.
    public void atualizar(){
        //Pega o nome de usuário (ex: "thaya") do objeto original.
        String cliente = usuario.getUsuario();
        //Pega a *nova* senha digitada pelo usuário na caixa de texto da tela.
        String novaSenha=  tela6.getTxtNovaSenha().getText();
        //Cria um *novo* objeto Usuario contendo apenas os dados necessários para o DAO.
        Usuario usuario = new Usuario("", cliente, novaSenha);
        //Cria uma instância do objeto de conexão.
        Conexao conexao = new Conexao();
        
        try{
              //Abre a conexão com o banco de dados.
              Connection conn = conexao.getConnection();
              //Cria o DAO (Objeto de Acesso a Dados) 
              UsuarioDAO dao = new UsuarioDAO(conn);
              //Chama o método 'atualizar' do DAO para executar o UPDATE no banco.
              dao.atualizar(usuario);
              //Exibe uma mensagem de sucesso para o usuário.
              JOptionPane.showMessageDialog(tela6, 
                      "Senha de Usuário atualizada com Sucesso!",
                      "AVISO", JOptionPane.INFORMATION_MESSAGE);
          //Captura qualquer exceção de SQL (ex: banco offline, tabela não existe).
          }catch(SQLException e){
              //Exibe uma mensagem de erro genérica.
              JOptionPane.showMessageDialog(tela6, "Falha de conexão!", "ERRO", 
                                                JOptionPane.ERROR_MESSAGE);
          }
      }          
    }