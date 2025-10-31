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
   
   public ResultSet consultar(Usuario usuario)throws SQLException{
       String sql = "select * from tbusuarios where usuario_usu = ? and "
               + "  senha_usu = ?";
       PreparedStatement statement = conn.prepareStatement(sql);
       statement.setString(1, usuario.getNome());
       statement.setString(2, usuario.getSenha());
       statement.setString(1, usuario.getSenha());
       statement.execute();
       ResultSet resultado = statement.getResultSet();
       return resultado;
       
   }
   
   
   public void inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO tbusuarios (nome_usu, usuario_usu, senha_usu) VALUES ('"
                                                       + usuario.getNome() + "','"
                                                       + usuario.getUsuario() + "','"
                                                       + usuario.getSenha() + "')'";
        PreparedStatement statement = conn.prepareStatement(sql);  
        statement.execute();
        conn.close();
   }
   
   public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE tbusuarios SET senha_usu = ? WHERE usuario_usu = ?";
        
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, usuario.getSenha()); 
        statement.setString(2, usuario.getUsuario()); 
        
        statement.execute();
        conn.close();
        
   }
   
   public void remover(Usuario usuario) throws SQLException {
        String sql = "DELETE FROM tbusuarios WHERE usuario_usu = ?"; 
        
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, usuario.getUsuario()); 
        
        statement.execute(); 
        conn.close();
       
    }
}
