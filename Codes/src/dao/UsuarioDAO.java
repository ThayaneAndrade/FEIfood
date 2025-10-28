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
public class UsuarioDAO {
   private final Connection conn;
   
   public UsuarioDAO(Connection conn) {
        this.conn = conn;
    }
   
   public ResultSet consultar(Usuario usuario) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND senha = ?";
        PreparedStatement statement = conn.prepareStatement(sql); 
        statement.setString(1, usuario.getUsuario());
        statement.setString(2, usuario.getSenha()); 
        statement.execute(); 
        ResultSet resultado = statement.getResultSet(); 
        return resultado;
    }
   
   public void inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, usuario, senha) VALUES (?, ?, ?)";
        
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, usuario.getNome());
        statement.setString(2, usuario.getUsuario());
        statement.setString(3, usuario.getSenha());
        
        statement.execute();
   }
   
   public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET senha = ? WHERE usuario = ?";
        
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, usuario.getSenha()); 
        statement.setString(2, usuario.getUsuario()); 
        
        statement.execute();
        
   }
   
   public void remover(Usuario usuario) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE usuario = ?"; 
        
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, usuario.getUsuario()); 
        
        statement.execute(); 
       
    }
}
