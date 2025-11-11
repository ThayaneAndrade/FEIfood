/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author thaya
 */
//Define a classe abstrata base para todos os itens vendáveis (Comida e Bebida).
//'abstract' significa que esta classe não pode ser instanciada diretamente.
public abstract class Alimento {
    //Guarda o ID do alimento (chave primária no banco).
    protected int id;
    //Guarda o nome do alimento (ex: "Pizza 4 Queijos").
    protected String nome;
    //Guarda o preço unitário do alimento (ex: 45.0).
    protected double preco;
    //Guarda o tipo (ex: "Comida", "Bebida", "Bebida Alcoólica").
    protected String tipo;
    //Indica se o alimento é vegetariano (aplicável a Comida).
    protected boolean vegetariano;
    //Indica se o alimento é "Zero Açúcar" (aplicável a Comida e Bebida).
    protected boolean zero;

    //Construtor vazio padrão.
    public Alimento(){
        
    }

    //Construtor completo para inicializar um objeto Alimento com todos os dados.
    public Alimento(int id, String nome, double preco, String tipo,
            boolean vegetariano, boolean zero) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.tipo = tipo;
        this.vegetariano = vegetariano;
        this.zero = zero;
    }

    //Método "getter" para obter o valor do ID.
    public int getId() {
        return id;
    }

    //Método "setter" para definir o valor do ID.
    public void setId(int id) {
        this.id = id;
    }

    //Método "getter" para obter o valor do Nome.
    public String getNome() {
        return nome;
    }

    //Método "setter" para definir o valor do Nome.
    public void setNome(String nome) {
        this.nome = nome;
    }

    //Método "getter" para obter o valor do Preço.
    public double getPreco() {
        return preco;
    }

    //Método "setter" para definir o valor do Preço.
    public void setPreco(double preco) {
        this.preco = preco;
    }

    //Método "getter" para obter o valor do Tipo.
    public String getTipo() {
        return tipo;
    }

    //Método "setter" para definir o valor do Tipo.
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    //Método "getter" para verificar se é Vegetariano (padrão 'is' para booleano).
    public boolean isVegetariano() {
        return vegetariano;
    }

    //Método "setter" para definir se é Vegetariano.
    public void setVegetariano(boolean vegetariano) {
        this.vegetariano = vegetariano;
    }

    //Método "getter" para verificar se é Zero Açúcar.
    public boolean isZero() {
        return zero;
    }

    //Método "setter" para definir se é Zero Açúcar.
    public void setZero(boolean zero) {
        this.zero = zero;
    }
    
    
    //Sobrescreve o método padrão 'toString' do Java.
    @Override
    public String toString() {
        //Retorna uma string formatada (ex: "Pizza 4 Queijos — R$ 45,00").
        return nome + " — R$ " + 
                String.format("%.2f", preco).replace('.', ',');
    }


}