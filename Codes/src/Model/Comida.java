/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author thaya
 */
public class Comida extends Alimento{
    private boolean vegetariana; 
    private boolean zero;
    
    public Comida(){
        super();
        this.tipo = "Comida";
    }
    public Comida (int id, String nome, double preco, boolean vegetariano,
            boolean zero){
        super(id, nome, preco, "Comida", vegetariano, zero);
    }

    public boolean isVegetariana() {
        return vegetariana;
    }

    public void setVegetariana(boolean vegetariana) {
        this.vegetariana = vegetariana;
    }

    public boolean isZero() {
        return zero;
    }

    public void setZero(boolean zero) {
        this.zero = zero;
    }
    
    
}
