package acquistoManagement;

import catalogoManagement.Prodotto;
import utenteManagement.User;

import java.util.ArrayList;

public class Carrello {
    private static final long serialVersionUID = 1L;

    private int carrelloId;

    private transient ArrayList<Prodotto> listaProdotti = new ArrayList<>();

    public Carrello(int carrelloId) {
        this.carrelloId = carrelloId;
    }

    public Carrello(){}

    public int getCarrelloId() {
        return carrelloId;
    }

    public ArrayList<Prodotto> getListaProdotti() {
        return listaProdotti;
    }

    public void setListaProdotti(ArrayList<Prodotto> listaProdotti) {
        this.listaProdotti = listaProdotti;
    }

    public void add(Prodotto p){
        this.listaProdotti.add(p);
    }

    public void empty(){
        this.listaProdotti.clear();
    }

}
