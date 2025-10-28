/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author thaya
 */
public class Conexao {
    public Connection getConnection() throws SQLException {
        Connection conexao = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/feifood", //Verifique este nome!
                "postgres", //Usuário padrão
                "0711"); //Senha que usamos no pgAdmin
return conexao;
    }
}