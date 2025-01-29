/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package volpintesta.concessionaire;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 *
 * @author User
 */
public class Concessionario
{
    private static final String TOKEN_SALDO_INIZIALE = "saldo_iniziale";
    private static final String TOKEN_TIPO = "tipo";
    private static final String TOKEN_TARGA = "targa";
    private static final String TOKEN_MARCA = "marca";
    private static final String TOKEN_MODELLO = "modello";
    private static final String TOKEN_ANNO = "anno";
    private static final String TOKEN_PREZZO = "prezzo";
    private static final String TOKEN_POSTI = "posti";
    private static final String TOKEN_BAGAGLIAIO = "bagagliaio";
    private static final String SEPARATORE_ATTRIBUTI = ";";
    private static final String SEPARATORE_NOME_VALORE = ":";
    
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
     * Crea un nuovo concessionario inizializzandolo con dati letti da file.
     * Per i dettagli sul corretto formato del path, leggere la documentazione delle API
     * della classe java.io.File.
     * @param filepath il path del file
     * @throws FileNotFoundException se il file non esiste
     * @throws IOException se durante l'esecuzione del metodo si verificano errori di I/O durante le operazioni di lettura del file
     * @throws FileParsingException se la lettura del file fallisce non per errori di I/O ma di formato dei dati
     * @throws VeicoloDuplicatoException se si tenta di creare veicoli con la stessa targa
     * @throws NullPointerException se il filepath passato come parametro è null
     * @throws SecurityException se il file può essere aperto per ragioni legate ai permessi sui file (vedi i costruttori di FileInputStream)
     */
    public Concessionario (String filepath)
            throws FileNotFoundException, VeicoloDuplicatoException, FileParsingException, IOException
    {
        this();
        inizializzaDaFile(filepath);
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
    
    /**
     * Inizializza i dati del concessionario leggendoli da file.
     * Per i dettagli sul corretto formato del path, leggere la documentazione delle API
     * della classe java.io.File.
     * @param filepath il path del file
     * @throws FileNotFoundException se il file non esiste
     * @throws IOException se durante l'esecuzione del metodo si verificano errori di I/O durante le operazioni di lettura del file
     * @throws FileParsingException se la lettura del file fallisce non per errori di I/O ma di formato dei dati
     * @throws VeicoloDuplicatoException se si tenta di creare veicoli con la stessa targa
     * @throws NullPointerException se il filepath passato come parametro è null
     * @throws SecurityException se il file può essere aperto per ragioni legate ai permessi sui file (vedi i costruttori di FileInputStream)
     */
    private void inizializzaDaFile(String filepath)
            throws FileNotFoundException, VeicoloDuplicatoException, FileParsingException, IOException
    {
        // Gestisco subito un errore che lancerebbe eccezione più tardi.
        if (filepath == null)
            throw new NullPointerException("Il filepath è null.");
        
        // Creo un backup dei dati in caso di eccezione (vedi la clausola catch alla fine del metodo)
        float backupSaldo = saldo;
        Veicolo[] backupVeicoli = veicoli.toArray(new Veicolo[0]);
        
        // Inizializzo i dati;
        saldo = 0;
        veicoli.clear();
        
        // Creo un riferimento ad un potenziale file del filesystem.
        // Qui ancora non è detto che il file debba esistere, l'oggetto può anche
        // essere usato per creare un nuovo file. I controlli sull'esistenza e sui
        // permessi saranno fatti quando si tenterà effettivamente di accedere al file
        // su disco.
        // File va inteso come una classe che permette di accedere ad un descrittore di
        // file, quindi alle informazioni del file che risiedono nel filesystem, non al
        // suo contenuto. Per accedere al contenuto del file si usano classi che implementano
        // il concetto di stream.
        File file = new File(filepath);
        
        // InputStream definisce uno stream in lettura, cioè un canale a cui arrivano dei byte
        // con chiamate di lettura che permettono di leggere i primi N byte disponibili
        // (quindi anche meno di N, se ce ne sono di meno), aspettando l'arrivo di
        // un byte se nessun byte è disponibile.
        // Questa è una classe astratta che implementa la gestione logica di uno stream
        // in lettura, non implementa i dettagli fisici dell'accesso alla sorgente reale
        // di byte. Infatti l'input stream sarà creato istanziando FileInputStream,
        // che è invece una sottoclasse che specifica le modalità di accesso fisico ai byte.
        // Le chiamate di lettura saranno bloccanti nel caso in cui non siano disponibili byte.
        InputStream inputStream = null;
        
        // InputStreamReader implementa la lettura di caratteri letti da un InputStream interno.
        // Implementa la codifica necessaria al passaggio da byte a carattere.
        // Quindi permette di leggere uno o più caratteri, non implementa il concetto
        // di stringa o di riga, pertanto non permette di leggere fino a un terminatore
        // di stringa ('\0') o di riga ('\n'), ma solo fino a N caratteri per volta,
        // restituendo tutti i caratteri disponibili al momento, senza permettere di terminare
        // in anticipo la lettura se viene trovato un terminatore prima dell'N-esimo carattere,
        // e senza leggere più a lungo se non viene trovato il terminatore nei primi N caratteri
        // disponibili.
        // Le chiamate di lettura saranno bloccanti nel caso in cui non siano disponibili caratteri.
        InputStreamReader inputStreamReader = null;
                
        // BufferedReader implementa la bufferizzazione di caratteri letti da un Reader interno.
        // Quindi permette la lettura efficiente di array di caratteri, di stringhe
        // e di righe terminate con un "a capo", permettendo di leggere fino al terminatore,
        // indipendentemente dal numero di caratteri necessari per raggiungerlo, e di
        // attendere la sua ricezione se non è ancora presente nello stream, senza restituire porzioni incomplete.
        // Questo rende le chiamate di lettura semplici ma anche particolarmente bloccanti.
        BufferedReader bufferedReader = null;
        
        // Ha senso un try con finally ma senza catch?
        // In questo caso sì, perché nessuna delle eccezioni può essere gestita qui, quindi non vengono
        // catturate, vengono solo aggiunte nell'intestazione del metodo tramite clausola throws.
        // Tuttavia, il fatto di creare un blocco try-finally fa sì che, quando viene lanciata
        // una eccezione, prima di uscire dalla funzione, venga eseguito il blocco finally.
        // Questo è necessario perché le risorse di sistema di cui si è ottenuta la proprietà
        // vanno rilasciate per permetterne l'uso agli altri processi, ma nel caso di eccezione
        // non è facile controllare il punto di errore, quindi racchiudiamo tutto in un try, e,
        // qualunque sia il punto in cui verrà eventualmente lanciata una eccezione, anche se questa non
        // viene catturata, prima di propagarla al di fuori del blocco di codice, verrà eseguito
        // il finally, dove scriviamo la logica di rilascio delle risorse (in questo caso chiusura dei file).
        try
        {
            // Nota come la creazione non è di InputStream (astratto) ma di FileInputStream,
            // anche se poi, all'interno dell'InputStreamReader, tutto passerà per i metodi di InputStream
            // Qui il file viene effettiamente aperto in lettura, richiedendone l'accesso al
            // al sistema operativo. Dal momento che entra in gioco il sistema operativo, che è
            // arbitro di risorse condivise, è plausibile aspettarsi errori, infatti il metodo lancia
            // le seguenti eccezioni:
            // FileNotFoundException: se non esiste il file
            // SecurityException: se il file, pur esistendo, non può essere letto per ragioni legate a permessi
            // Entrambe le eccezioni non possono essere gestite qui, perché richiedono delle azioni
            // da parte dell'utente, e questo è un metodo che si occupa di lettura di dati, non di 
            // interazione con l'utente, quindi non è il punto giusto per gestire l'errore.
            // Le due eccezioni non saranno quindi catturate, ma saranno aggiunte all'intestazione
            // del metodo tramite clausola di throws.
            // FileNotFoundException è sottoclasse di IOException, quindi ai fini della compilazione
            // non è necessario esplicitarla, dal momento che più avanti il metodo lancerà anche
            // IOException, ma viene comunque aggiunta perché questo metodo fa molte cose, e il fatto
            // che il file non esista in un metodo che come unico parametro ha il path di un file
            // è una informazione abbastanza rilevante da meritare di essere sottolineata in maniera specifica.
            // SecurityException è una RuntimeException, quindi non viene aggiunta alla clausola
            // throws in quanto non necessaria per la compilazione. Verrà però aggiunta al commento javadoc
            // affinché il programmatore che usa questo metodo sia informato anche riguardo gli
            // errori che non richiedono obbligatoriamente una cattura o un rilancio in codice.
            inputStream = new FileInputStream(file);
            
            // Creo l'istanza di InputStreamReader che implementerà la conversione tra i byte letti
            // dall'InputStream interno e i caratteri necessari per la gestione di valori testuali.
            // La creazione dell'istanza non lancia eccezione perché l'accesso alle risorse di sistema
            // viene fatto non qui, ma quando viene creato il FileInputStream.
            inputStreamReader = new InputStreamReader(inputStream);
            
            // Creo l'istanza di BufferedReader che implementerà la bufferizzazione dei caratteri
            // letti dall'InputStreamReader interno in modo da implementare i concetti di stringa e riga.
            // La creazione dell'istanza non lancia eccezione perché l'accesso alle risorse di sistema
            // viene fatto non qui, ma quando viene creato il FileInputStream.
            bufferedReader = new BufferedReader(inputStreamReader);
            
            // Inizio la lettura del file.
            // Durante la lettura da file, il programma non potrà mai dare per scontato che il file
            // abbia la struttura corretta, perché, dal momento che il file è sotto il controllo
            // dell'utente, non è detto che sia correttamente formattato.
            // Si dovrà quindi gestire ogni condizione di errore possibile, senza far crashare
            // il programma.
            // La lettura del file sarà effettuata riga per riga fino alla fine del file, dal
            // momento che il file di input è strutturato con le monete nella prima riga e poi
            // un veicolo per riga.
            // Il metodo readLine restutiusce null se è stata raggiunta la fine dello stream
            // (che in questo caso vuol dire che il file è finito).
            // Nel caso in cui lo stream sia chiuso, oppure ci siano altri errori di lettura,
            // questa chiamata può lanciare una IOException.
            // Nel momento in cui viene lanciata un'eccezione, non la si cattura per permettere
            // la terminazione del metodo rilanciando l'eccezione, ma prima, nella clausola finally,
            // saranno puliti i dati letti a metà che hanno dato luogo ad un riempimento del
            // concessionario con dati parziali.
            
            
            // Leggo il numero di monete del concessionario dalla prima riga,
            // e lo scrivo nel concessionario.
            String line = "";
            boolean lettoSaldoIniziale = false;
            int righeLette = 0;
            do
            {
                line = line.trim(); // rimuovo eventuali caratteri bianchi dall'inizio e dalla fine della riga
                if (!line.isEmpty()) // salto le righe vuote
                {
                    // Inizializzo tutti i dati a valori non validi in modo da capire, più avanti,
                    // se sono stati letti oppure no.
                    float saldoIniziale = -1;
                    TipoVeicolo tipo = TipoVeicolo.NESSUNO;
                    String targa = null;
                    String marca = null;
                    String modello = null;
                    int anno = -1;
                    float prezzo = -1;
                    int posti = -1;
                    float bagagliaio = -1;
                    
                    // Se è rimasto qualche carattere, cerco di estrapolare i caratteri dalla riga.
                    String[] attributi = line.split(Concessionario.SEPARATORE_ATTRIBUTI);
                    for (String attributo : attributi)
                    {
                        if (attributo != null)
                        {
                            String[] nomeValore = attributo.split(Concessionario.SEPARATORE_NOME_VALORE);
                            if (nomeValore.length == 2) // salto gli attributi che non sono formati esattamente da nome e valore
                            {
                                try
                                {
                                    String nomeUpper = nomeValore[0] != null ? nomeValore[0].trim().toUpperCase() : "";
                                    String valore = nomeValore[1] != null ? nomeValore[1].trim() : "";

                                    if (nomeUpper.equals(Concessionario.TOKEN_SALDO_INIZIALE.toUpperCase()))
                                        saldoIniziale = Integer.parseInt(valore);
                                    else if (nomeUpper.equals(Concessionario.TOKEN_TIPO.toUpperCase()))
                                        tipo = TipoVeicolo.fromString(valore);
                                    else if (nomeUpper.equals(Concessionario.TOKEN_TARGA.toUpperCase()))
                                        targa = valore;
                                    else if (nomeUpper.equals(Concessionario.TOKEN_MARCA.toUpperCase()))
                                        marca = valore;
                                    else if (nomeUpper.equals(Concessionario.TOKEN_MODELLO.toUpperCase()))
                                        modello = valore;
                                    else if (nomeUpper.equals(Concessionario.TOKEN_ANNO.toUpperCase()))
                                        anno = Integer.parseInt(valore);
                                    else if (nomeUpper.equals(Concessionario.TOKEN_PREZZO.toUpperCase()))
                                        prezzo = Float.parseFloat(valore);
                                    else if (nomeUpper.equals(Concessionario.TOKEN_POSTI.toUpperCase()))
                                        posti = Integer.parseInt(valore);
                                    else if (nomeUpper.equals(Concessionario.TOKEN_BAGAGLIAIO.toUpperCase()))
                                        bagagliaio = Float.parseFloat(valore);
                                }
                                catch (NumberFormatException e)
                                {
                                    // Stringa mal formata. Questo può succedere in un file.
                                    // Per adesso non faccio nulla, ma vado avanti nella lettura.
                                    // Se mancano dati per la formazione del veicolo, verrà lanciata
                                    // una eccezione più avanti.
                                }
                            }
                        }
                    }
                    
                    // Dopo aver terminato il parse della stringa, uso i dati letti per inizializzare i dati.
                    // Se già esiste un veicolo con la stessa targa dell'eventuale veicolo appena letto,
                    // viene lanciata VeicoloDuplicatoException.
                    // E' a tutti gli effetti un problema sul contenuto del file, quindi non catturo l'eccezione, ma lascio che sia
                    // rilanciata, e gestita nel main comunicando all'utente l'errore nel formato del file, perchè, come per
                    // il caso delle eccezioni di I/O, non è questa la sede per comunicare l'errore all'utente.
                        
                    if (saldoIniziale >= 0 // SE è stato letto il saldo iniziale
                            && !lettoSaldoIniziale // E non è ancora stato letto il saldo iniziale
                            // E non sono stati letti dati non necessari
                            && tipo == TipoVeicolo.NESSUNO && targa == null && marca == null && modello == null && anno < 0 && prezzo < 0 && posti < 0 && bagagliaio < 0)
                    {
                        // Siamo nella riga del saldo iniziale
                        saldo = saldoIniziale;
                        lettoSaldoIniziale = true; // segno di aver inizializzato il saldo iniziale per evitare sovrascritture
                    }
                    else if (tipo == TipoVeicolo.AUTO // SE è stata letta un'auto
                            // E tutti i dati necessari sono stati correttamente letti
                            && targa != null && marca != null && modello != null && anno >= 0 && prezzo >= 0 && posti >= 0 && bagagliaio >= 0
                            && saldoIniziale < 0) // E non sono stati letti dati non necessari
                    {
                        // Siamo nella riga di un'auto.
                        aggiungiVeicolo(new Auto(targa, marca, modello, anno, prezzo, posti, bagagliaio)); // aggiungiVeicolo lancia VeicoloDuplicatoException
                    }
                    else if (tipo == TipoVeicolo.MOTO // SE è stata letta una moto
                            // E tutti i dati necessari sono stati correttamente letti
                            && targa != null && marca != null && modello != null && anno >= 0 && prezzo >= 0
                            && posti < 0 && bagagliaio < 0 && saldoIniziale < 0) // E non sono stati letti dati non necessari
                    {
                        // Siamo nella riga di una moto
                        aggiungiVeicolo(new Moto(targa, marca, modello, anno, prezzo)); // aggiungiVeicolo lancia VeicoloDuplicatoException
                    }
                    else if (saldoIniziale >= 0 || tipo != TipoVeicolo.NESSUNO
                            || targa != null || marca != null || modello != null || anno >= 0 || prezzo >= 0 || posti >= 0 || bagagliaio >= 0)
                    {
                        // SE sono presenti alcuni dati ma non sono correttamente fomati
                        // lancio una nuova eccezione. Non avendo trovato una eccezione valida nelle
                        // API di Java, ne creo una nuova.
                        throw new FileParsingException("Dati non correttamente formattati in riga " + righeLette);
                    }
                }
                    
                // leggo la prossima riga.
                // readLine restituisce tutti i caratteri fino al prossimo terminatore di riga
                // (\n, \r o \r\n) o alla fine dello stream, rimuovendo il terminatore di riga.
                line = bufferedReader.readLine(); // lancia IOException
                righeLette++;
            } while (line != null); // se line è null è finito il file
            
        }
        catch (Exception e)
        {
            // Se viene lanciato una qualsiasi eccezione (nota che viene catturata la classe più
            // generica possibile), vuol dire che l'inizializzazione dei dati è fallita.
            // Qui non è possibile gestire l'errore in maniera completa, ma è necessario che
            // i dati interni non siano corrotti a causa di letture rimaste a metà. E' meglio
            // pulire i dati riportandoli nella condizione esistente all'inizio dell'esecuzione
            // del metodo. Poi viene rilanciata la stessa identica eccezione che era stata catturata,
            // in modo da non modificarne i dati.
            saldo = backupSaldo;
            veicoli = new ArrayList<Veicolo>();
            for (Veicolo v : backupVeicoli)
                veicoli.add(v);
            
            throw e; // rilancio l'eccezione.
        }
        finally
        {
            // All'uscita dal try, che avvenga normalmente o a causa di eccezione non catturata,
            // oppure all'uscita dal catch di un'eccezione catturata (se c'è),
            // che avvenga normalmente oppure a causa di un'ulteriore eccezione lanciata mentre
            // la prima veniva catturata, ma comunque PRIMA che si esca definitivamente
            // l'eccezione, uscendo dalla funzione, viene eseguito il blocco finally.
            // In parole povere l'esecuzione del blocco finally è garantita appena prima che
            // una eccezione non catturata venga rilanciata al di fuori del blocco try o try-catch associato.
            // Il blocco esiste per eseguire le istruzioni che non devono mai essere saltate
            // a causa di eccezioni, indipendentemente da dove vengano lanciate e quindi da
            // dove interrompano l'esecuzione del blocco di codice.
            // Normalmente, nel finally vanno rilasciate le risorse per essere
            // sicuri di non bloccare altri processi o lo stesso processo mantenendo
            // il possesso di una risorsa che resterà inutilizzata.
            
            // Il metodo close è dichiarato nell'interfaccia Closeable e nell'interfaccia
            // AutoCloseable, entrambe implementate dalla maggior parte delle classi che permettono
            // di manipolare risorse di sistema.
            // L'implementazione chiude lo stream e anche gli stream interni.
            // Dal momento che non sappiamo, in caso di eccezione, quali stream siano stati
            // creati e quali no, è sempre meglio chiuderli tutti, nel dubbio, tanto, stando
            // alle specifiche scritte nell'interfaccia Closeable, la funzione close richiamata
            // su uno stream già chiuso non ha alcun effetto (neanche collaterale). Questo non è
            // sempre vero per le implementazioni della close dell'interfaccia AutoCloseable,
            // dove invece è consigliato l'uso di una "try with resources" (l'implementazione
            // di questo blocco sarà scritta nel metodo di scrittura del file).
            
            // Inoltre, va prima verificata la validità di ogni riferimento perché, non sapendo quando
            // l'esecuzione si è interrotta, non possiamo neanche dare per scontato che tutti 
            // gli stream siano stati creati e abbiano quindi riferimenti validi.
            
            // E' buona norma rilasciare sempre le risorse in ordine inverso a come vengono
            // acquisite. Questo permette di garantire un corretto ordine di pulizia delle
            // risorse (se A è stata creata prima di B e tutto funziona, vuol dire che A
            // può funzionare senza B, non è detto, però, il contrario).
            
            if (bufferedReader != null) bufferedReader.close(); 
            if (inputStreamReader != null) inputStreamReader.close();
            if (inputStream != null) inputStream.close();

            // Le classi che implementano Closeable.close o AutoCloseable.close
            // possono a loro volta lanciare una IOException in chiusura. Se poi questo
            // avvenga o no dipende dalle implementazioni e non lo si può sapere senza
            // conoscere l'implementazione dello stream specifico a basso livello.
            // Se fallisce la chiusura di uno stream e il rilascio delle sue risorse interne,
            // la risorsa potrebbe rimanere in uno stato non consistente
            // e in proprietà al processo. Non ci sono però soluzioni al problema, perché non 
            // avendo accesso diretto alle risorse del sistema non si può fare altro che affidarsi
            // alla corretta implementazione della libreria.
            // Tuttavia, anche in caso di eccezione, l'implementazione specifica della close
            // dovrebbe garantire il rilascio corretto delle risorse, se vengono rispettate le
            // raccomandazioni di implementazione del metodo AutoCloseable.close (vedi API).
            // In ogni caso, l'unica cosa che si può fare in questo caso è lasciare l'eccezione
            // non gestita, lasciando che se ne occupi qualcun altro, e sperando che non succeda mai.
            
        }
    }
    
    /**
     * Scrive i dati in un file su disco
     * @param filepath il path del file (vedi la classe java.io.File per dettagli)
     * @throws IOException se il file non può essere creato, sovrascritto oppure se si verificano errori di I/O in scrittura.
     */
    public void salvaDati(String filepath) throws IOException
    {
        // Gestisco subito un errore che lancerebbe eccezione più tardi.
        if (filepath == null)
            throw new NullPointerException("Il filepath è null.");
        
        // Creo un riferimento al file
        File file = new File (filepath);
        
        // Se il file esiste, lo elimino in modo da poter creare più avanti quello nuovo
        // con i dati aggiornati
        // NOTA: l'implementazione attuale non è bellissima, perché richiede i permessi in cancellazione (delete)
        // per implementare la sovrascrittura di un file. Basterebbe aprire semplicemente il file in scrittura
        // per sovrascriverne il contenuto, cosa che richiederebbe semplicemente i permessi in scrittura, e non anche
        // in cancellazione. A scopo dimostrativo, però, qui sta venendo mostrato anche il delete.
        if (file.exists())
        {
            // Lancia SecurityException se i permessi sul file impediscono l'accesso in scrittura;
            boolean deleted = file.delete();
            if (!deleted)
            {
                throw new IOException("Esiste già un file con nome \""+filepath+"\" e quindi non è possibile sovrascriverlo.");
            }
        }
        
        // Creo il file su cui scrivere i dati
        
        // Lancia IOException se si verificano errori di I/O durante la creazione del nuovo file.
        // Lancia SecurityException se i permessi sul file impediscono l'accesso in scrittura
        // necessario alla creazione del file.
        boolean created = file.createNewFile();
        if (!created)
        {
            throw new IOException("Non è possibile creare il file \""+filepath+"\".");
        }
        
        // OutputStream è uno stream di byte aperto in scrittura. Permette la scrittura di N byte.
        // Come InputStream, è una classe astratta e implementa la gestione logica dello stream,
        // lasciando alle sottoclassi i dettagli implementativi sulla vera e propria scrittura dei
        // byte sul dispositivo di destinazione.
        // Tutti gli OutputStream, per ragioni di ottimizzazione delle scritture e per problematiche
        // legate alla gestione degli stream da parte delle librerie del Java o del sistema operativo,
        // che dipendono da tempistiche e ragioni delle quali non abbiamo il controllo,
        // potrebbe capitare che scritture rimangano "pendenti".
        // A fine scrittura, quindi, bisogna sempre ricordarsi di chiamare il metodo flush, che forza la scrittura
        // dei dati da parte dei sistema operativo sul dispositivo di destinazione, oppure il metodo
        // close, chiama il flush automaticamente prima dellachiusura effettiva chiusura dello stream.
        
        // FileOutputStream è uno stream aperto in scrittura su un file, quindi implementa i dettagli
        // della scrittura di byte su un file su disco.
        // Ci sono 2 modalità di scrittura:
        // - WRITE, che permette di scrivere un file dall'inizio (sovrascrivendone il contenuto), che è la
        // modalità di default di apertura
        // - APPEND, impostabile tramite un overload del costruttore, che permette di scrivere un file
        // continuando dalla fine.
        // Il costruttore di FileOutputStream può lanciare:
        // FileNotFoundException: il file non esiste
        // SecurityException: se i permessi sul file ne impediscono l'accesso in scrittura.
        // Attenzione: una mancata chiusura dello stream e quindi un mancato richiamo della flush finale
        // possono determinare, nel caso di file, la corruzione di un file, che, rimasto aperto e senza
        // i dati necessari a rappresentare la fine del file, non può più essere letto.
        
        // PrintWriter è la stessa classe del riferimento allo standard output contenuto in System.out
        // e implementa la scrittura di dati condificati in varie maniere su OutputStream.
        // Comprende sia le codifiche numeriche che quelle testuali, sia per singoli caratteri e array
        // di caratteri, che per stringhe.
        // Per semplicità, dal momento che si è abiutati con System.out, può essere utilizzata questa
        // classe.
        // Attenzione: in generale, però, sarebbe bene, per essere sicuri che lettura e scrittura usino
        // le stesse codifiche e lavorino in maniera analoga, usare sempre classi "speculari", come
        // BufferedReader e BufferedWriter.
        //Inoltre, PrintWriter presenta un forte problema: come Scanner per l'input, anche PrintWriter per
        // l'output non richiede di catturare le IOException lanciate dai metodi di scrittura. Questo vuol
        // dire che l'eccezione lanciata dal FileInputStream interno, viene racchiusa in una RuntimeException
        // per essere lanciata anche se l'intestazione del metodo non la dichiara. Questo vuol dire che
        // PrintWriter non è adeguato alla scrittura di dati su stream che facilmente possono dare errori,
        // come file e stream di rete.
        
        // OutputStreamWriter implementa la codifica testuale dei byte, quindi permette di passare dalla
        // scrittura di caratteri alla scrittura di byte (lo stream puro trasmette sempre byte non codificati).
        // Permette quindi la scrittura di singoli caratteri, array di caratteri e porzioni di stringhe, senza
        // gestire l'invio dei terminatori di stringa e di linea.
        
        // BufferedWriter implementa la bufferizzazione interna di caratteri per la costruzione di stringhe
        // da inviare poi in una volta. Inoltre permette di inviare stringhe e righe aggiungendo automaticamente
        // il corretto terminatore alla fine, in maniera speculare a un BufferedReader.
        
        // Costrutto "try-catch with resources"
        // E' possibile dichiarare tra parentesi tonde, accanto al try, separati da punto e virgola come
        // istruzioni separate, una serie di riferimenti a classi che implementano l'interfaccia
        // AutoCloseable perché implementano risorse di sistema. Il costrutto "try-catch with resources"
        // richiama automaticamente il metodo close di tutti i riferimenti dichiarati tra le parentesi
        // tonde. Anche se la chiamata del close non viene fatta esplicitamente dal programmatore, l'eccezione
        // IOException che viene lanciata implicitamente dalla clausola try deve essere o catturata o rilanciata,
        // come al solito.
        // Siccome la close è automaticamente chiamata, inoltre, non è necessario scrivere apposta una clausola
        // finally, a meno che non si debba fare anche altro a parte la chiusura degli stream e la liberazione
        // delle risorse. Inoltre, questo evita anche l'insorgere di problemi come quello del metodo di lettura
        // dei datai da file, in cui nel metodo close ci siamo dovuti preoccupare dell'ordine di rilascio delle
        // risorse e di quali avessero riferimenti validi e quali no.
        // Questa comodita, però, ha come costo il fatto che, oltre dover gestire una IOException lanciata implicitamente,
        // nell'implementazione di AutoCloseable non si garantisce (a differenza di quanto richiesto per le
        // implementazioni di Closeable) che chiamate di close su stream già chiusi e risorse già rilasciate
        // non lascino alcuna traccia nei dati interni. E' dunque fortemente consigliato, se si usa questo
        // costrutto, non chiamare mai la close esplicitamente.
        // In generale, comunque, si suggerisce l'uso del try-with-resources quando possibile.
                
        try(BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file))))
        {
            //Scrivo il saldo iniziale
            bufferedWriter.write(TOKEN_SALDO_INIZIALE + SEPARATORE_NOME_VALORE + " " + saldo);
            bufferedWriter.newLine();
            
            // Scrivo tutti i veicoli facendo attenzione che il formato in scrittura sia allineato a quello in lettura
            for (Veicolo v : veicoli)
            {
                if (v != null)
                {
                    String rigaVeicolo = TOKEN_TIPO + SEPARATORE_NOME_VALORE + " " + v.getTipoVeicolo().toString().toLowerCase();
                    rigaVeicolo += " " + SEPARATORE_ATTRIBUTI + " " + TOKEN_TARGA + SEPARATORE_NOME_VALORE + " " + v.getTarga();
                    rigaVeicolo += " " + SEPARATORE_ATTRIBUTI + " " + TOKEN_MARCA + SEPARATORE_NOME_VALORE + " " + v.getMarca();
                    rigaVeicolo += " " + SEPARATORE_ATTRIBUTI + " " + TOKEN_MODELLO + SEPARATORE_NOME_VALORE + " " + v.getModello();
                    rigaVeicolo += " " + SEPARATORE_ATTRIBUTI + " " + TOKEN_ANNO + SEPARATORE_NOME_VALORE + " " + v.getAnno();
                    rigaVeicolo += " " + SEPARATORE_ATTRIBUTI + " " + TOKEN_PREZZO + SEPARATORE_NOME_VALORE + " " + v.getPrezzo();
                    if (v instanceof Auto)
                    {
                        Auto a = (Auto)v;
                        rigaVeicolo += " " + SEPARATORE_ATTRIBUTI + " " + TOKEN_POSTI + SEPARATORE_NOME_VALORE + " " + v.getNumeroPosti();
                        rigaVeicolo += " " + SEPARATORE_ATTRIBUTI + " " + TOKEN_BAGAGLIAIO + SEPARATORE_NOME_VALORE + " " + a.getDimensioneBagagliaio();
                    }
                    bufferedWriter.write(rigaVeicolo);
                    bufferedWriter.newLine();
                }
            }
            
            // Forzo la scrittura su disco di eventuali dati rimasti bufferizzati nelle librerie e non ancora mandati sul file.
            bufferedWriter.flush();
            
            // non chiudo lo stream perché, uscendo dal try-with-resources, ci penseranno le librerie Java
        }
        catch (FileNotFoundException e)
        {
            // Non dovrebbe mai capitare, siccome il file è stato precedentemente creato nuovo e siccome il fallimento nella
            // creazione determina il lancio di una IOException, che non si riesca poi ad aprire il file. Non c'è, quindi,
            // una logica risolutiva che plausibilmente chi chiama questa funzione potrebbe implementare.
            // Decidiamo quindi, per non richiedere di gestire questa eccezione, ma per non zittirla completamente e permettere
            // al programma di crashare con errore, di wrapparla in una RuntimeException.
            throw new RuntimeException("La funzione ha creato correttamente il file nuovo, ma poi non riesce a trovarlo più quando lo deve aprire. "
                    + "Questo è un errore molto strano che non dovrebbe essere possibile che si verifichi mai.", e);
        }
    }
}
