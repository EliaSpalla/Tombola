import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class Main {
    static ArrayList<Giocatore> giocatore = new ArrayList<>();
    static Tabellone tabellone = new Tabellone();
    static ArrayList<Integer> numeriUsciti = new ArrayList<>();
    static ArrayList<Integer> vincite=new ArrayList<>(); //da 2 a 6

    // pattern per riconoscere i codici ANSI di controllo colore (es. \u001B[31m, \u001B[0m, ecc.)
    private static final Pattern ANSI_PATTERN = Pattern.compile("\\u001B\\[[;\\d]*m");

    public static void main(){
        Scanner sc=new Scanner(System.in);
        richiestaDati(sc);

        gioco(sc);
    }
    public static void main(Scanner sc){
        numeriUsciti.clear();
        creaVincite();
        giocatore.clear();
        richiestaDati(sc);

        gioco(sc);
    }

    public static void gioco(Scanner sc){
        numeriUsciti.clear();
        creaVincite();
        tabellone.creatoreSacchetto();

        while(!vincite.isEmpty()){
            int numero=tabellone.estrattoreNumeri();
            numeriUsciti.add(numero);

            stampaTutto();

            dormi(3000);
        }
        menuChiusura(sc);
    }

    public static void menuChiusura(Scanner sc){
        System.out.println("rigiocare? ");
        System.out.println("1:stessi giocatori ");
        System.out.println("2:richiedimi i dati ");
        System.out.println("3:torna al desktop ");
        int scelta= sc.nextInt();
        switch(scelta){
            case 1:
                gioco(sc);
                break;
            case 2:
                main(sc);
                break;
            case 3:
                break;
        }
    }

    public static void stampaTutto(){
        pulisciTerminale();
        tabellone.stampaTabellone(numeriUsciti);
        stampaGiocatoriAffiancati(numeriUsciti);
        controlloVinciteCartelle();
    }

    public static void controlloVinciteCartelle(){
        String cosaVince;
        switch(vincite.get(0)){
            case 2:
                cosaVince="ambo";
                break;
            case 3:
                cosaVince="terno";
                break;
            case 4:
                cosaVince="quaterna";
                break;
            case 5:
                cosaVince="cinquina";
                break;
            case 15:
                cosaVince="tombola";
                break;
            default:
                cosaVince="errore";
        }
        boolean vittoria=false;
        int i=1;
        for(Giocatore g: giocatore){
            int j=1;
            for(Cartella c: g.cartelle){
                if(c.controlloVincite(vincite,numeriUsciti)){
                    vittoria=true;
                    System.out.println(cosaVince.toUpperCase()+"!! "+"Giocatore "+i+" con la certella "+j);
                    dormi(3000);
                }
                j++;
            }
            i++;
        }
        if(vittoria){
            vincite.remove(0);
        }
    }

    public static void dormi(int millis){
        try {
            sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void creaVincite(){
        // correggo qui: svuoto la lista prima di aggiungere le vincite
        vincite.clear();
        for(int i=2;i<6;i++){
            vincite.add(i);
        }
        vincite.add(15);
    }

    public static void richiestaDati(Scanner sc){
        int NGiocatori=-1;
        while(NGiocatori<1){
            System.out.print("quanti Giocatori siete?");
            NGiocatori=sc.nextInt();
        }

        for(int i=0;i<NGiocatori;i++){
            System.out.print("Giocatore "+(i+1)+" :");

            int NCartelle=-1;
            while(NCartelle>6||NCartelle<1){
                System.out.print("quante cartelle vuoi? (max 6)");
                NCartelle=sc.nextInt();
                if(NCartelle>6||NCartelle<1){System.out.println("valore impossibile, riprova");}
            }
            giocatore.add(new Giocatore(NCartelle));
        }
    }

    public static void stampaCartelle(ArrayList<Integer> numeriUsciti){
        for(int i=0;i<giocatore.size();i++){
            System.out.println("Giocatore "+(i+1)+" :");
            giocatore.get(i).stampaCartelle(numeriUsciti);
        }
    }


    // Nuovo metodo: stampa le cartelle dei giocatori affiancate, ma con ogni giocatore incolonnato
    // (cioè per ogni giocatore le sue cartelle sono impilate verticalmente, e i giocatori sono affiancati orizzontalmente)
    // Ora riceve numeriUsciti e passa la lista a Cartella.getStampaRows(...) per mantenere la colorazione in rosso
    public static void stampaGiocatoriAffiancati(ArrayList<Integer> numeriUsciti) {
        // costruiamo per ogni giocatore un "blocco" di righe che contiene:
        // - intestazione "Giocatore N:"
        // - le sue cartelle una sotto l'altra (ogni cartella fornisce 4 righe: 3 + vuota)
        List<List<String>> blocchiGiocatori = new ArrayList<>();
        List<Integer> larghezze = new ArrayList<>();

        for (int p = 0; p < giocatore.size(); p++) {
            Giocatore g = giocatore.get(p);

            List<String> blocco = new ArrayList<>();
            blocco.add("Giocatore " + (p + 1) + ":");

            // per ciascuna cartella del giocatore aggiungo le righe (impilate)
            for (Cartella c : g.cartelle) {
                List<String> rows = c.getStampaRows(numeriUsciti); // usa la versione con colorazione
                // aggiungo tutte le righe della cartella al blocco (ogni cartella ha la riga vuota finale)
                for (String riga : rows) {
                    blocco.add(riga);
                }
            }

            // calcolo larghezza massima del blocco per padding usando lunghezza visibile (senza codici ANSI)
            int maxWidth = 0;
            for (String s : blocco) {
                int vis = visibleLength(s);
                if (vis > maxWidth) maxWidth = vis;
            }

            blocchiGiocatori.add(blocco);
            larghezze.add(maxWidth);
        }

        // determino quante righe stampare in totale (il blocco più alto)
        int maxRigheTotali = 0;
        for (List<String> b : blocchiGiocatori) {
            if (b.size() > maxRigheTotali) maxRigheTotali = b.size();
        }

        // stampo riga per riga, affiancando i blocchi dei giocatori e facendo padding per allineare le colonne
        for (int r = 0; r < maxRigheTotali; r++) {
            StringBuilder sb = new StringBuilder();
            for (int p = 0; p < blocchiGiocatori.size(); p++) {
                List<String> blocco = blocchiGiocatori.get(p);
                String line = (r < blocco.size()) ? blocco.get(r) : "";
                int width = larghezze.get(p);
                // usa padRightWithAnsi: calcola la lunghezza visibile e aggiunge spazi effettivi
                line = padRightWithAnsi(line, width);
                sb.append(line);
                // spazio fra i giocatori
                sb.append("   ");
            }
            System.out.println(sb.toString());
        }
    }

    // rimuove i codici ANSI dalla stringa (per contare la lunghezza visibile)
    private static String stripAnsi(String s) {
        if (s == null) return "";
        return ANSI_PATTERN.matcher(s).replaceAll("");
    }

    // lunghezza visibile dei caratteri (senza codici ANSI)
    private static int visibleLength(String s) {
        return stripAnsi(s).length();
    }

    // aggiunge spazi a destra in modo da ottenere 'width' caratteri visibili,
    // ma lascia intatti eventuali codici ANSI presenti nella stringa
    private static String padRightWithAnsi(String s, int width) {
        if (s == null) s = "";
        int vis = visibleLength(s);
        if (vis >= width) return s;
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < width - vis; i++) sb.append(' ');
        return sb.toString();
    }

    public static void pulisciTerminale() {
        try {
            // Verifica il sistema operativo
            if (System.getProperty("os.name").contains("Windows")) {
                // Comando per Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Comando per Linux e macOS
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException ex) {
            // In caso di errore, stampa una serie di righe vuote come fallback
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }
}