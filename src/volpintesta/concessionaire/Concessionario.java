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
    public Veicolo[] getVeicoli() { return veicoli.toArray(new Veicolo[0]); }
    
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
     * Verifica l'esistenza di un veicolo con una determinata targa.
     * @param targa la targa del veicolo
     * @return true se esiste un veicolo con la targa specificata, false altrimenti.
     */
    public boolean esisteVeicolo(String targa)
    {
        return cercaVeicolo(targa) != null;
    }
    
    /**
     * Aggiunge un veicolo al concessionario verificando che non esistano veicoli
     * con la stessa targa. Nel caso esista già un veicolo con la stessa targa,
     * il veicolo non verrà aggiunto e lanciata un'eccezione.
     * NOTA: questa implementazione con l'eccezione è a scopo didattico, per dimostrare
     * l'uso delle eccezioni, ma non è ottimale. Sarebbe meglio implementare semplicemente
     * un valore di ritorno booleano, come nella versione precedente, perché questa
     * implementazione obbliga chi chiama il metodo a catturare un'eccezione, e non è
     * quindi un modo comodo di gestire un caso di errore comune.
     * @param v il veicolo da aggiungere
     * @throws volpintesta.concessionaire.VeicoloDuplicatoException se il veicolo ha una targa
     * uguale ad un altro veicolo già presente in concessionario.
     */
    public void aggiungiVeicolo(Veicolo v) throws VeicoloDuplicatoException
    {
        // Una classe non può basare la consistenza dei suoi dati interni sulla fiducia
        // che chi chiama i suoi metodi passi parametri validi. Dunque, se nel concessionario
        // non possono esserci veicoli null, per evitare di dover gestire casi limite inutili
        // che hanno il solo effetto di rendere più complesso il programma senza dare valore
        // aggiunto, il responsabile dell'aggiunta di un riferimento null è il metodo che
        // permette l'aggiunta del veicolo. Se quindi il veicolo è null, semplicemente non sarà
        // aggiunto e il metodo terminerà senza fare altro.
        if (v != null)
        {
            // Prima va verificato che non esista un veicolo con la stessa targa,
            // quindi ne cerco uno, e se non esiste (viene restituito null)
            // posso andare avanti aggiungendo il veicolo passato come parametro.
            // NOTA: non c'è variabile per il risultato di cercaVeicolo perché
            // dopo il controllo il riferimento non serve più.
            if (cercaVeicolo(v.getTarga()) == null) 
            {
                veicoli.add(v);
            }
            else
            {
                // Se invece un veicolo con la stessa marca era stato effettivamente trovato
                // e l'aggiunta del veicolo non è avvenuta, il metodo lancia un'eccezione.
                
                // Quando viene lanciata l'eccezione il metodo termina anticipatamente la sua esecuzione e,
                // uno alla volta tutti i blocchi di codice termineranno anticipatamente la loro
                // esecuzione.
                // Le funzioni che terminano non restituiscono alcun valore, cosa che impedisce la
                // normale prosecuzione del codice. Funzioni che possono terminare anticipatamente
                // la loro esecuzione a causa di eccezioni devono dichiararlo nell'intestazione usando
                // la clausola throws.
                // Quando il blocco che termina è un blocco try, però, prima di forzare la terminazione
                // del blocco in cui è contenuto si controlla se esiste una clausola catch che catturi
                // un'eccezione di tipo compatibile con l'eccezione lanciata.
                // In caso affermativo, l'esecuzione del programma riprenderà dall'interno del blocco
                // catch, e l'eccezione catturata sarà disponibile tramite un riferimento per leggerne
                // eventuali dati.                
                
                throw new VeicoloDuplicatoException("Impossibile aggiungere il veicolo perché è già presente un veicolo con targa '"+v.getTarga()+"'");
            }
        }
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
     * Restituisce l'array contenente i veicoli di un certo tipo con la marca specificata.
     * L'array restituito non avrà elementi a null, quindi sarà esattamente della
     * dimensione richiesta.
     * @param marca la marca da cercare
     * @param tipoVeicolo il tipo di veicoli. Se TipoVeicolo.NESSUNO, il filtro viene ignorato
     * @return un array contenente i veicoli di un certo tipo con la marca specificata
     */
    public Veicolo[] cercaPerMarca (String marca, TipoVeicolo tipoVeicolo)
    {
        // Inizialmente bisogna allocare un array di risultati della dimensione massima possibile
        Veicolo[] temp = new Veicolo[veicoli.size()];
        int risultatiTrovati = 0;
        for (Veicolo v : veicoli)
        {
            if (v != null
                    && v.getMarca().equals(marca)
                    && (tipoVeicolo == TipoVeicolo.NESSUNO || tipoVeicolo == v.getTipoVeicolo()))
            {
                temp[risultatiTrovati++] = v; // usa il numero di risultati trovati come indice di inserimento, poi lo incrementa per passare al prossimo
                // Non c'è break ma si va avanti con le iterazioni perché, potendoci essere più di un veicolo con la stessa marca, bisogna iterare l'intera lista.
            }
        }
        
        // Adesso, conoscendo quanti sono gli elementi, è possibile allocare un array della
        // dimensione corretta e travasare gli elementi.
        Veicolo[] risultato = new Veicolo[risultatiTrovati];
        // Ogni volta che bisogna lavorare con gli indici, ovviamente non è possibile
        // usare il foreach, ma è obbligatorio tornare all'utilizzo del for normale.
        for (int i = 0; i < risultatiTrovati; i++)
            risultato[i] = temp[i];
        
        return risultato;
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
        return cercaPerMarca(marca, TipoVeicolo.NESSUNO);
    }
    
    /**
     * Restituisce l'array contenente i veicoli prodotti in un anno compreso tra gli
     * estremi specificati e facenti parte di un certo tipo.
     * L'array restituito non avrà elementi a null, quindi sarà esattamente della
     * dimensione richiesta.
     * @param annoMin l'estremo inferiore dell'intervallo della ricerca (inclusivo)
     * @param annoMax l'estremo superiore dell'intervallo della ricerca (inclusivo)
     * @param tipoVeicolo il tipo di veicoli. Se TipoVeicolo.NESSUNO, il filtro viene ignorato
     * @return un array contenente i veicoli di un certo tipo prodotti in un anno compreso tra gli estremi specificati
     */
    public Veicolo[] cercaPerAnno (int annoMin, int annoMax, TipoVeicolo tipoVeicolo)
    {
        ArrayList<Veicolo> risultato = new ArrayList<Veicolo>();
        for (Veicolo v : veicoli)
        {
            if (v != null
                    && v.getAnno() >= annoMin && v.getAnno() <= annoMax
                    && (tipoVeicolo == TipoVeicolo.NESSUNO || tipoVeicolo == v.getTipoVeicolo()))
            {
                risultato.add(v);
                // Non c'è break ma si va avanti con le iterazioni perché, potendoci essere più di un veicolo con la stessa marca, bisogna iterare l'intera lista.
            }
        }
        return risultato.toArray(new Veicolo[0]);
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
        return cercaPerAnno(annoMin, annoMax, TipoVeicolo.NESSUNO);
    }
    
    /**
     * Restituisce l'array contenente le auto con un bagagliaio di capienza minima specificata
     * @param capienzaMinimaBagagliaio la capienza minima del bagagliaio in litri
     * @return un array contenente le auto con un bagagliaio di capienza minima specificata.
     */
    public Auto[] cercaAutoPerCapienzaBagagliaio (float capienzaMinimaBagagliaio)
    {
        ArrayList<Auto> risultato = new ArrayList<Auto>();
        for (Veicolo v : veicoli)
        {
            if (v != null && v instanceof Auto)
            {
                Auto a = (Auto)v;
                if (a.getDimensioneBagagliaio() >= capienzaMinimaBagagliaio)
                    risultato.add(a);
            }
        }
        return risultato.toArray(new Auto[0]);
    }
}
