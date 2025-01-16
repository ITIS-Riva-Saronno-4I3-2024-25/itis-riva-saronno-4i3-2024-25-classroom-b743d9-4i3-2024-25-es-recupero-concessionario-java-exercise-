/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package volpintesta.concessionaire;

import java.util.ArrayList;

/**
 *
 * @author User
 */
public class Concessionario
{
    private ArrayList<Veicolo> veicoli;
    
    private float saldo;
    public float getSaldo() { return saldo; }
    
    /**
     * Crea un concessionario con una lista vuota di veicoli e un saldo iniziale
     * di 0 euro.
     */
    public Concessionario()
    {
        veicoli = new ArrayList<Veicolo>();
        saldo = 0;
    }

    /**
     * Aggiunge un veicolo al concessionario verificando che non esistano veicoli
     * con la stessa targa. Nel caso esista già un veicolo con la stessa targa,
     * il veicolo non verrà aggiunto e verrà restituito false.
     * @param v il veicolo da aggiungere
     * @return true se il veicolo è stato aggiunto, false altrimenti
     */
    public boolean aggiungiVeicolo(Veicolo v)
    {
        // Una classe non può basare la consistenza dei suoi dati interni sulla fiducia
        // che chi chiama i suoi metodi passi parametri validi. Dunque, se nel concessionario
        // non possono esserci veicoli null, per evitare di dover gestire casi limite inutili
        // che hanno il solo effetto di rendere più complesso il programma senza dare valore
        // permette l'aggiunta del veicolo. Se quindi il veicolo è null, semplicemente non sarà
        // aggiunto e il metodo terminerà immediatamente comunicando un risultato negativo.
        if (v == null)
            return false;
        
        // Prima va verificato che non esista un veicolo con la stessa targa,
        // quindi ne cerco uno, e se non esiste (viene restituito null)
        // posso andare avanti aggiungendo il veicolo passato come parametro
        // e posso terminare il metodo restituendo un risultato positivo.
        // NOTA: non c'è variabile per il risultato di cercaVeicolo perché
        // dopo il controllo il riferimento non serve più.
        if (cercaVeicolo(v.getTarga()) == null) 
        {
            veicoli.add(v);
            return true;
        }
        
        // Se il metodo sta ancora eseguendo, vuol dire che il programma
        // non è entrato nell'if precedente (altrimenti sarebbe terminato restituendo true).
        // Quindi un veicolo con la stessa marca era stato effettivamente trovato
        // e l'aggiunta del veicolo non è avvenuta, dunque va restituito un risultato negativo.
        return false;
    }
    
    /**
     * Mostra in output tutti i veicoli del catalogo
     */
    public void stampaCatalogo()
    {
        for (Veicolo v : veicoli)
            if (v != null)
                System.out.println(v);
    }
    
    /**
     * Vende il veicolo con la targa specificata e incrementa il saldo corrente
     * del concessionario della cifra ricavata dalla vendita (il prezzo del veicolo).
     * Se la vendita riesce restituisce true. Se la vendita non riesce, perchè non viene
     * trovato alcun veicolo con la targa specificata, restituisce false.
     * @param targa la targa del veicolo da vendere
     * @return true se la vendita viene effettuata, false se il veicolo non viene trovato
     */
    public boolean vendiVeicolo (String targa)
    {
        // Cerco il veicolo da vendere. A differenza dell'aggiunta del veicolo,
        // dove il veicolo cercato non serviva a niente se non a verificare
        // che fosse stato restituito null, in questo caso con il veicolo va
        // effettivamente fatto qualcosa, quindi il riferimento va scritto in una
        // variabile.
        Veicolo veicoloDaVendere = cercaVeicolo(targa);
        
        // Se veicoloDaVendere ha un valore non null, vuol dire che è stato
        // trovato un veicolo con la targa passata come parametro.
        if (veicoloDaVendere != null)
        {
            // Rimuovo il veicolo (nota che lo rimuovo dalla lista, ma il riferimento
            // resta dentro a veicoloDaVendere fino a che la funzione non termina)
            veicoli.remove(veicoloDaVendere);
            
            // aggiungo il prezzo del veicolo venduto al saldo del concessionario
            saldo += veicoloDaVendere.getPrezzo();
            
            // restituisco un risultato positivo che indica l'avvenuta vendita
            return true;
        }
        
        // Se il metodo non ha terminato la sua esecuzione (restituendo quindi un
        // risultato positivo) vuol dire che non è stato trovato un veicolo con la
        // data passata come parametro, quindi non può essere effettuata alcuna vendita.
        return false;
    }
    
    /**
     * Restituisce un veicolo con la targa specificata. Restituisce null se
     * non trova nessun veicolo con la targa specificata.
     * @param targa la targa da cercare
     * @return il veicolo con la targa specificata
     */
    public Veicolo cercaVeicolo (String targa)
    {
        // Inizializzo ad un riferimento non valido. Se non viene trovato nessun
        // veicolo con la targa passata come parametro, il valore non sarà mai
        // modificato, e il metodo restituirà quindi null.
        // La variabile dichiarata fuori dal ciclo fa si che, se invece il veicolo
        // viene trovato, il suo riferimento possa essere scritto su una variabile
        // che sopravviva all'uscita dal ciclo. In questo caso il valore restituito
        // dal metodo sarà il riferimento al veicolo trovato all'interno del ciclo.
        Veicolo veicoloTrovato = null; 
        for (Veicolo v : veicoli) // itero tutti i veicoli (questo è un foreach)
        {
            // NOTA: Le stringhe vanno sempre controllate con equals, perché
            // non esistono quasi mai casi in cui serva controllare l'uguaglianza
            // superficiale, ma va quasi sempre controllata l'uguaglianza profonda,
            // a meno di casi minori e particolari che mettono sempre in gioco
            // stringhe costanti (quindi scritte in codice e non lette tramite
            // un canale di input).
            // Vedi: "Uguagliaza superficiale e profonda"
            // Vedi: "equals e == "
            if (v.getTarga().equals(targa))
            {
                veicoloTrovato = v;
                break; 
            }
        }
        return veicoloTrovato;
    }
    
    /**
     * Restituisce l'array contenente i veicoli con la marca specificata.
     * L'array restituito non avrà elementi a null, quindi sarà esattamente della
     * dimensione richiesta.
     * @param marca la marca da cercare
     * @return un array contenente i veicoli con la marca specificata
     */
    public Veicolo[] cercaPerMarca (String marca)
    {
        Veicolo[] risultato = new Veicolo[veicoli.size()]; // nel dubbio bisogna allocare un array di risultati della dimensione massima possibile
        int risultatiTrovati = 0;
        for (Veicolo v : veicoli)
        {
            if (v != null && v.getMarca().equals(marca)) // NOTA: non c'è nessun controllo in aggiunta del veicolo sul null, quindi non è detto che non ci siano elementi null in lista.
            {
                risultato[risultatiTrovati++] = v; // usa il numero di risultati trovati come indice di inserimento, poi lo incrementa per passare al prossimo
                // Non c'è break ma si va avanti con le iterazioni perché, potendoci essere più di un veicolo con la stessa marca, bisogna iterare l'intera lista.
            }
        }
        return risultato;
    }
    
    /**
     * Restituisce l'array contenente i veicoli prodotti in un anno compreso tra gli
     * estremi specificati.
     * L'array restituito non avrà elementi a null, quindi sarà esattamente della
     * dimensione richiesta.
     * @param annoMin l'estremo inferiore dell'intervallo della ricerca (inclusivo)
     * @param annoMax l'estremo superiore dell'intervallo della ricerca (inclusivo)
     * @return un array contenente i veicoli prodotti in un anno compreso tra gli estremi specificati
     */
    public Veicolo[] cercaPerAnno (int annoMin, int annoMax)
    {
        Veicolo[] risultato = new Veicolo[veicoli.size()]; // nel dubbio bisogna allocare un array di risultati della dimensione massima possibile
        int risultatiTrovati = 0;
        for (Veicolo v : veicoli)
        {
            if (v != null && v.getAnno() >= annoMin && v.getAnno() <= annoMax) // NOTA: non c'è nessun controllo in aggiunta del veicolo sul null, quindi non è detto che non ci siano elementi null in lista.
            {
                risultato[risultatiTrovati++] = v; // usa il numero di risultati trovati come indice di inserimento, poi lo incrementa per passare al prossimo
                // Non c'è break ma si va avanti con le iterazioni perché, potendoci essere più di un veicolo con la stessa marca, bisogna iterare l'intera lista.
            }
        }
        return risultato;
    }
}
