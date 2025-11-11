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
public class Pedido {
    private int id;
    private Usuario usuario;
    private java.sql.Date dataPed;
    private Integer avaliacaoPed;
    private List<Alimento> itens;
    public Pedido(){
        itens = new ArrayList<>();
    }
   
    public void adicionarItem(Alimento a){
        itens.add(a);
    }
    
    public void removerItem(Alimento a){
        itens.remove(a);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getDataPed() {
        return dataPed;
    }

    public void setDataPed(Date dataPed) {
        this.dataPed = dataPed;
    }

    public Integer getAvaliacaoPed() {
        return avaliacaoPed;
    }

    public void setAvaliacaoPed(Integer avaliacaoPed) {
        this.avaliacaoPed = avaliacaoPed;
    }

    public List<Alimento> getItens() {
        return itens;
    }

    public void setItens(List<Alimento> itens) {
        this.itens = itens;
    }


}
