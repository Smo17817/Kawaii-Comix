package acquistoManagement;

public interface CarrelloDAO {

    public Carrello doRetrieveCarrello(int userId);
    public Carrello doRetrieveProdottiCarrello(Carrello carrello);
    public void doCreateCarrello(int userId);
    public Boolean doDeleteCarrello(int carrelloId);
    public void doSvuotaCarrello(Carrello carrello);
}
