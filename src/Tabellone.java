import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

public class Tabellone {
    int[][] tabellone = new int[9][10];
    int[][] foglio = new int[18][9];
    ArrayList<ArrayList<Integer>> numeriDaUsare = new ArrayList<>(); //suddivisi in colonne in base a come usarli, 1:9,10:19,20:29 ecc...
    LinkedList<Integer> sacchetto = new LinkedList<>();
    //per scrivere colorato
    String ROSSO = "\u001B[31m";
    String RESET = "\u001B[0m";

    public Tabellone() {
        creatoreTabellone();
        creatoreNumeriDaUsare();
        creatoreSacchetto();
    }

    public void creatoreTabellone(){
        for(int i=0;i<9;i++){
            for(int j=0;j<10;j++){
                tabellone[i][j]=j+(10*i)+1;
            }
        }
    }
    public void creatoreNumeriDaUsare(){
        numeriDaUsare.clear();
        for(int i=0;i<9;i++){
            numeriDaUsare.add(new ArrayList<Integer>());
        }

        ArrayList<Integer> listaTemp=new ArrayList<>();
        for(int[] riga:tabellone){
            for(int valore:riga){
                listaTemp.add(valore);
            }
        }
        Collections.shuffle(listaTemp);

        for(int i=0;i<listaTemp.size();i++){
            int indice=(listaTemp.get(i)==90) ? 8 : listaTemp.get(i)/10;
            numeriDaUsare.get(indice).add(listaTemp.get(i));
        }

    }

    public void creatoreFoglio(){
        for(int i=0;i<9;i++){
            List<Integer> rigaDati = numeriDaUsare.get(i);
            Collections.shuffle(rigaDati);

            if(i==0){
                while(rigaDati.size()<18){
                    rigaDati.add(0);
                }
                Collections.shuffle(rigaDati);
                for(int j=0;j<18;j++){
                    foglio[j][i]=rigaDati.get(j);
                }
            } else {
                while(!rigaDati.isEmpty()){
                    Integer valore = rigaDati.remove(0);

                    ArrayList<Integer> indici = creaRigheConNUguale(foglio);

                    Collections.shuffle(indici);
                    int sceltaRiga = indici.get(0);

                    foglio[sceltaRiga][i] = valore;
                }
            }
        }
    }

    public int trovaRigaPiuVuota(int[][] foglio) {
        int indiceRigaMigliore = -1;
        int maxZeri = -1;

        for (int i = 0; i < foglio.length; i++) {
            int conteggioZeri = 0;

            // Conta quanti zeri ci sono nella riga corrente
            for (int j = 0; j < foglio[i].length; j++) {
                if (foglio[i][j] == 0) {
                    conteggioZeri++;
                }
            }

            // Se questa riga ha più zeri della migliore trovata finora, aggiorna
            if (conteggioZeri > maxZeri) {
                maxZeri = conteggioZeri;
                indiceRigaMigliore = i;
            }
        }

        return indiceRigaMigliore; // Restituisce l'indice (0-17)
    }

    public ArrayList<Integer> creaRigheConNUguale(int[][] foglio) {
        ArrayList<Integer> righeConNUguale=new ArrayList<>();
        int indice=trovaRigaPiuVuota(foglio);
        righeConNUguale.add(indice);
        int nMin=0;

        //conta quanti numeri ha la riga con meno numeri
        for (int col = 0; col < foglio[indice].length; col++) {
            if (foglio[indice][col] != 0) {
                nMin++;
            }
        }

        //controlla quante righe hanno lo stesso numero di quella con meno numeri
        int cont;
        for(int i=0;i< foglio.length;i++){
            if(i==indice){continue;}
            cont=0;
            for(int j=0;j<foglio[i].length;j++){
                if (foglio[i][j] != 0) {
                    cont++;
                }
            }
            if(cont==nMin){
                righeConNUguale.add(i);
            }
        }

        return righeConNUguale;
    }

    public int estrattoreNumeri(){
        int numero = sacchetto.getFirst();
        sacchetto.removeFirst();
        return numero;
    }
    public void creatoreSacchetto(){
        sacchetto.clear();
        for(int i=0;i<90;i++){
            sacchetto.add(i+1);
        }
        Collections.shuffle(sacchetto);
    }

    public void stampaFoglio() {
        for(int i=0;i<18;i++){
            if(i%3==0){
                for(int j=0;j<9;j++){
                    System.out.print("-----");
                }
                System.out.println();
            }
            for(int j=0;j<9;j++){
                if(foglio[i][j]==0){System.out.print("[  ] ");}
                else{System.out.printf("[%02d] ",foglio[i][j]);}
            }
            System.out.println();
        }
    }

    public void stampaTabellone(java.util.List<Integer> numeriUsciti) {
        // converti in Set per ricerche più veloci
        java.util.Set<Integer> usciti = new java.util.HashSet<>(numeriUsciti);

        for (int i = 0; i < tabellone.length; i++) {
            for (int j = 0; j < tabellone[i].length; j++) {
                int val = tabellone[i][j];
                if (usciti.contains(val)) {
                    // attenzione alla posizione del RESET: deve essere fuori dal %02d
                    System.out.printf(ROSSO + "[%02d] " + RESET, val);
                } else {
                    System.out.printf("[%02d] ", val);
                }
            }
            System.out.println();
        }
    }
}