/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author thaya
 */
//Define a classe 'Comida', que herda da classe 'Alimento'.
public class Comida extends Alimento{
    //Atributo específico da classe Comida (não existe em Bebida).
    private boolean vegetariana; 
    //Atributo 'zero' (zero açúcar/calorias) duplicado da classe pai 'Alimento'.
    private boolean zero;
    
    //Construtor vazio padrão.
    public Comida(){
        //Chama o construtor vazio da classe pai ('Alimento').
        super();
        //Define o tipo padrão para esta classe.
        this.tipo = "Comida";
    }
    
    //Construtor completo para criar um objeto Comida com todos os dados.
    public Comida (int id, String nome, double preco, boolean vegetariano,
            boolean zero){
        //Chama o construtor da classe pai ('Alimento'), passando os dados.
        //Define o tipo como "Comida" e passa 'vegetariano' e 'zero'.
        super(id, nome, preco, "Comida", vegetariano, zero);
    }

    //Método "getter" para verificar se a comida é vegetariana.
    public boolean isVegetariana() {
        //Retorna o atributo 'vegetariana' da classe pai.
        return vegetariana;
    }

    //Método "setter" para definir se a comida é vegetariana.
    public void setVegetariana(boolean vegetariana) {
        //Define o atributo 'vegetariana' da classe pai.
        this.vegetariana = vegetariana;
    }

    //Método "getter" para verificar se é zero (sobrescreve o da classe pai).
    public boolean isZero() {
        return zero;
    }

    //Método "setter" para definir se é zero (sobrescreve o da classe pai).
    public void setZero(boolean zero) {
        this.zero = zero;
    }
    
    
}