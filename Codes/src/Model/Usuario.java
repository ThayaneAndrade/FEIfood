/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author thaya
 */
//Define a classe de modelo (Model) que representa um Usuário.
public class Usuario {
    //Guarda o ID (chave primária) do usuário no banco de dados.
    private int id;
    //Guarda o nome completo do usuário (ex: "Thayane Andrade").
    private String nome;
    //Guarda o nome de login do usuário (ex: "thaya").
    private String usuario;
    //Guarda a senha do usuário.
    private String senha;
    //Guarda uma avaliação (não utilizado neste projeto).
    private int avaliacao;

    //Construtor 1: Usado para criar um novo usuário (Cadastro).
    public Usuario(String nome, String usuario, String senha) {
        this.nome = nome;
        this.usuario = usuario;
        this.senha = senha;
    }
    
    //Construtor 2: Usado para verificar o login (só precisa de usuário e senha).
    public Usuario(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }
    
    //Construtor 3: Usado para criar um objeto com dados vindos do banco (obsoleto).
    public Usuario(int id, String nome, String usuario, String senha, int avaliacao) {
        this.id = id;
        this.nome = nome;
        this.usuario = usuario;
        this.senha = senha;
        this.avaliacao = avaliacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    //Método "setter" para definir a senha.
    public void setSenha(String senha) {
        this.senha = senha;
    }

    //Método "getter" para obter o valor do ID.
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAvaliacao() {
        return avaliacao;
    }
    
    public void setAvaliacao(int avaliacao) {
        this.avaliacao = avaliacao;
    }
    
}