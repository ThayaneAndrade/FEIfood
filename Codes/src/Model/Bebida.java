/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author thaya
 */
//Define a classe 'Bebida', que herda da classe 'Alimento' e implementa a interface 'ImpostoAlcool'.
public class Bebida extends Alimento implements ImpostoAlcool{
    //Atributo específico da classe Bebida para marcar se é alcoólica.
    private boolean alcoolica;
    
    //Construtor vazio padrão.
    public Bebida(){
        //Chama o construtor vazio da classe pai ('Alimento').
        super();
        //Define o tipo padrão para esta classe.
        this.tipo = "Bebida";
    }
    
    //Construtor completo para criar um objeto Bebida com todos os dados.
    public Bebida(int id, String nome, double preco, boolean alcoolica, double 
            teorAlcoolico, boolean zero){
        
       //Chama o construtor da classe pai ('Alimento'), passando os dados comuns.
       //Usa um operador ternário para definir o 'tipo' como "alcoolica" ou "Bebida".
       super(id, nome, preco, alcoolica ? "alcoolica" : "Bebida" , false, zero);  
       //Define os atributos específicos desta classe.
        this.alcoolica = alcoolica; 
    }
    
//Sobrescreve o método definido na interface 'ImpostoAlcool'.
@Override
    //Método que calcula o imposto (Polimorfismo).
    public double calcularImpostoAlcool() {
        //Se a bebida não for alcoólica, o imposto é zero.
        if (!alcoolica) return 0.0;
        //Regra de negócio: bebidas alcoólicas pagam 30% de imposto sobre o preço.
        return preco * 0.30;
    }

    //Método "getter" para verificar se a bebida é alcoólica.
    public boolean isAlcoolica() {
        return alcoolica;
    }

    //Método "setter" para definir se a bebida é alcoólica.
    public void setAlcoolica(boolean alcoolica) {
        this.alcoolica = alcoolica;
    }
  
}