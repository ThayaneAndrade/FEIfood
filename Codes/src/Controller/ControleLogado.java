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
//Define a classe de controle para a tela "Logado" (Minha Conta).
public class ControleLogado {
   private Logado tela4;
   private Usuario usuario;
   
   //Construtor que inicializa o controller com a tela e o usuário.
   public ControleLogado(Logado tela4, Usuario usuario){
       this.tela4 = tela4;
       this.usuario = usuario;
   }
    
   //Método chamado pela View (botão "Alterar Senha").
   //Ele apenas retorna o objeto 'usuario' logado.
   public Usuario chamarAlteracao(){
       return usuario;
   }
   
   //Método chamado pela View (botão "Excluir Cadastro").
   //Ele apenas retorna o objeto 'usuario' logado.
   public Usuario chamarExclusao(){
       return usuario;
   }
  
}