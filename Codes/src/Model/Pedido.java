/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author thaya
 */
//Define a classe de modelo (Model) que representa um Pedido.
public class Pedido {
    //Guarda o ID (chave primária) do pedido no banco de dados.
    private int id;
    //Guarda a referência ao objeto Usuario que fez este pedido.
    private Usuario usuario;
    //Guarda a data em que o pedido foi feito (tipo java.sql.Date).
    private java.sql.Date dataPed;
    //Guarda a nota de avaliação (0-5) do pedido. 'Integer' permite que seja nulo.
    private Integer avaliacaoPed;
    //Lista que armazena todos os objetos 'Alimento' associados a este pedido.
    private List<Alimento> itens;
    
    //Construtor padrão da classe Pedido.
    public Pedido(){
        //Inicializa a lista de 'itens' para evitar 'NullPointerException'.
        itens = new ArrayList<>();
    }
   
    //Método utilitário para adicionar um Alimento à lista de itens do pedido.
    public void adicionarItem(Alimento a){
        itens.add(a);
    }
    
    //Método utilitário para remover um Alimento da lista de itens do pedido.
    public void removerItem(Alimento a){
        itens.remove(a);
    }
    
    //Método "getter" para obter o valor do ID.
    public int getId() {
        return id;
    }

    //Método "setter" para definir o valor do ID.
    public void setId(int id) {
        this.id = id;
    }

    //Método "getter" para obter o objeto Usuario.
    public Usuario getUsuario() {
        return usuario;
    }

    //Método "setter" para definir o objeto Usuario.
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    //Método "getter" para obter a data do pedido.
    public Date getDataPed() {
        return dataPed;
    }

    //Método "setter" para definir a data do pedido.
    public void setDataPed(Date dataPed) {
        this.dataPed = dataPed;
    }

    //Método "getter" para obter a avaliação do pedido.
    public Integer getAvaliacaoPed() {
        return avaliacaoPed;
    }

    //Método "setter" para definir a avaliação do pedido.
    public void setAvaliacaoPed(Integer avaliacaoPed) {
        this.avaliacaoPed = avaliacaoPed;
    }

    //Método "getter" para obter a lista completa de itens (Alimentos).
    public List<Alimento> getItens() {
        return itens;
    }

    //Método "setter" para definir a lista de itens (Alimentos).
    public void setItens(List<Alimento> itens) {
        this.itens = itens;
    }
}
