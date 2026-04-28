import java.util.ArrayList;
import java.util.List;

public class Cartella {
    int[][] cartella=new int[3][9];
    String ROSSO = "\u001B[31m";
    String RESET = "\u001B[0m";

    public void stampaCartella(ArrayList<Integer> usciti) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                if(cartella[i][j]==0) {
                    System.out.print("[  ] ");
                }else{
                    int val = cartella[i][j];
                    if (usciti.contains(val)) {
                        // attenzione alla posizione del RESET: deve essere fuori dal %02d
                        System.out.printf(ROSSO + "[%02d] " + RESET, val);
                    } else {
                        System.out.printf("[%02d] ", val);
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean controlloVincite(ArrayList<Integer> vincite,ArrayList<Integer> usciti) {
        int cont=0;
        boolean siVaPerLaTombola=false;
        if(vincite.get(0)==15){siVaPerLaTombola=true;}

        for (int i = 0; i < 3; i++) {
            if(!siVaPerLaTombola){
                cont=0;
            }
            for (int j = 0; j < 9; j++) {
                if(usciti.contains(cartella[i][j])) {
                    cont++;
                }
            }
            if(vincite.get(0)<=cont) {
                return true;
            }
        }
        return false;
    }

    public List<String> getStampaRows() {
        List<String> rows = new ArrayList<>();
        if (cartella == null) {
            rows.add("(cartella non inizializzata)");
            rows.add("");
            return rows;
        }

        for (int i = 0; i < cartella.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < cartella[i].length; j++) {
                int val = cartella[i][j];
                if (val == 0) {
                    sb.append("[  ] ");
                } else {
                    sb.append(String.format("[%02d] ", val));
                }
            }
            rows.add(sb.toString());
        }
        rows.add("");
        return rows;
    }

    // Nuovo: versione che formatta le righe evidenziando i numeri già usciti
    public List<String> getStampaRows(ArrayList<Integer> usciti) {
        List<String> rows = new ArrayList<>();
        if (cartella == null) {
            rows.add("(cartella non inizializzata)");
            rows.add("");
            return rows;
        }

        for (int i = 0; i < cartella.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < cartella[i].length; j++) {
                int val = cartella[i][j];
                if (val == 0) {
                    sb.append("[  ] ");
                } else {
                    if (usciti != null && usciti.contains(val)) {
                        sb.append(ROSSO).append(String.format("[%02d] ", val)).append(RESET);
                    } else {
                        sb.append(String.format("[%02d] ", val));
                    }
                }
            }
            rows.add(sb.toString());
        }
        // riga vuota finale per separazione verticale tra set di righe
        rows.add("");
        return rows;
    }

}