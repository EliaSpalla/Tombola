import java.util.ArrayList;

public class Giocatore {
    ArrayList<Cartella> cartelle = new ArrayList<>();
    Tabellone tabellone=new Tabellone();
    int nCartelle;

    public Giocatore(int nCartelle){
        this.nCartelle=nCartelle;
        tabellone.creatoreFoglio();
        aggiungiCartelle();
    }

    public void aggiungiCartelle(){
        for(int i=0;i<nCartelle;i++){
            cartelle.add(new Cartella());
            popolaCartelle(i);
        }
    }

    public void popolaCartelle(int indice){
        for(int i=0;i<3;i++){
            for(int j=0;j<9;j++){
                cartelle.get(indice).cartella[i][j]=tabellone.foglio[(indice*3)+i][j];
            }
        }
    }

    public void stampaCartelle(ArrayList<Integer> numeriUsciti){
        for(int i=0;i<nCartelle;i++){
            System.out.println("Cartella " + (i+1));
            cartelle.get(i).stampaCartella(numeriUsciti);
        }
    }


}
