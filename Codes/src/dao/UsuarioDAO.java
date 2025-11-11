/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Model.Usuario;
/**
 *
 * @author thaya
 */
//Define a classe DAO (Objeto de Acesso a Dados) para Usuários.
public class UsuarioDAO {
   private final Connection conn;
   
   public UsuarioDAO(Connection conn) {
        this.conn = conn;
    }
   
   //Consulta um usuário no banco para verificar se o login é válido.
   public ResultSet consultar(Usuario usuario)throws SQLException{
       //Define a string SQL para a consulta SELECT.
       String sql = "select * from tbusuarios where usuario_usu = ? and "
               + "  senha_usu = ?";
       //Cria o PreparedStatement (declaração SQL pré-compilada).
       PreparedStatement statement = conn.prepareStatement(sql);
       //Define o primeiro parâmetro (?) como o usuário.
       statement.setString(1, usuario.getUsuario());
       //Define o segundo parâmetro (?) como a senha.
       statement.setString(2, usuario.getSenha());
       //Executa a consulta no banco.
       statement.execute();
       //Obtém o conjunto de resultados da consulta.
       ResultSet resultado = statement.getResultSet();
       //Retorna o ResultSet para o Controller (que deve fechá-lo).
       return resultado;
       
   }
   
   
   //Insere um novo usuário no banco de dados.
   public void inserir(Usuario usuario) throws SQLException {
        //Define a string SQL para o comando INSERT.
        String sql = "INSERT INTO tbusuarios (nome_usu, usuario_usu, senha_usu)"
                + "VALUES (?,?,?)";
        //Usa 'try-with-resources' para garantir que o 'statement' feche.
        try(PreparedStatement statement = conn.prepareStatement(sql)){
                 //Define o primeiro parâmetro (?) como o nome.
                 statement.setString(1, usuario.getNome());
                 //Define o segundo parâmetro (?) como o usuário.
                 statement.setString(2, usuario.getUsuario());
                 //Define o terceiro parâmetro (?) como a senha.
                 statement.setString(3, usuario.getSenha());
                 //Executa o comando INSERT.
                 statement.execute();
        }//O 'statement' é fechado automaticamente aqui.
   }
   
   //Atualiza a senha de um usuário existente no banco.
   public void atualizar(Usuario usuario) throws SQLException {
        //Define a string SQL para o comando UPDATE.
        String sql = "UPDATE tbusuarios SET senha_usu = ? WHERE usuario_usu = ?";
        
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
        statement.setString(1, usuario.getSenha()); 
        statement.setString(2, usuario.getUsuario());
        statement.execute();
        }
   }
   
   //Remove um usuário do banco de dados.
   public void remover(Usuario usuario) throws SQLException {
        //Define a string SQL para o comando DELETE.
        String sql = "DELETE FROM tbusuarios WHERE usuario_usu = ?"; 
        
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
        statement.setString(1, usuario.getUsuario()); 
        statement.execute();
        }
   }
}
