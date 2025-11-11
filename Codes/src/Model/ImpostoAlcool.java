/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author thaya
 */
//Define a interface 'ImpostoAlcool'.
//Uma interface é um "contrato" que define um conjunto de métodos abstratos.
public interface ImpostoAlcool {
    //Define a assinatura do método que qualquer classe que implementar esta interface
    //será OBRIGADA a escrever.
    //Neste caso, força a classe (ex: Bebida) a ter um método que calcula o imposto.
    double calcularImpostoAlcool();
    
}