/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author thaya
 */
public class Bebida extends Alimento implements ImpostoAlcool{
    private boolean alcoolica;
    private double teorAlcoolico;
    
    public Bebida(){
        super();
        this.tipo = "Bebida";
    }
    public Bebida(int id, String nome, double preco, boolean alcoolica, double 
            teorAlcoolico, boolean zero){
        
       super(id, nome, preco,alcoolica ?"alcoolica":"Bebida" , false, zero);  
        this.alcoolica = alcoolica; 
        this.teorAlcoolico = teorAlcoolico;
    }
    
@Override
    public double calcularImpostoAlcool() {
        if (!alcoolica) return 0.0;
        // regra qualquer só pra mostrar uso da interface
        return preco * 0.30;
    }

    public boolean isAlcoolica() {
        return alcoolica;
    }

    public void setAlcoolica(boolean alcoolica) {
        this.alcoolica = alcoolica;
    }

    public double getTeorAlcoolico() {
        return teorAlcoolico;
    }

    public void setTeorAlcoolico(double teorAlcoolico) {
        this.teorAlcoolico = teorAlcoolico;
    }

  
}
