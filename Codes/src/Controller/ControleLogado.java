/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Usuario;
import View.Logado;
import View.MeusPedidos; 
import Controller.ControleMeusPedidos;
/**
 *
 * @author thaya
 */
public class ControleLogado {
   private Logado tela4;
   private Usuario usuario;
   

   
   public ControleLogado(Logado tela4, Usuario usuario){
       this.tela4 = tela4;
       this.usuario = usuario;
     
   }
    
   public Usuario chamarAlteracao(){
       return usuario;
   }
   
   public Usuario chamarExclusao(){
       return usuario;
   }
  
}
