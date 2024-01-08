package acquistoManagement;

import catalogoManagement.Prodotto;
import utenteManagement.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Carrello implements Serializable{
    private static final long serialVersionUID = 1L;

    private int carrelloId;

    private transient HashSet<Prodotto> listaProdotti = new HashSet<>();

    public Carrello(int carrelloId) {
        this.carrelloId = carrelloId;
    }

    public Carrello(){}

    public int getCarrelloId() {
        return carrelloId;
    }

    public HashSet<Prodotto> getListaProdotti() {
        return listaProdotti;
    }

    public void setListaProdotti(HashSet<Prodotto> listaProdotti) {
        this.listaProdotti = listaProdotti;
    }

    public void add(Prodotto p){
        this.listaProdotti.add(p);
    }

    public void empty(){
        this.listaProdotti.clear();
    }

}
