/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package volpintesta.concessionaire;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class MainClass
{
    private static Scanner input = new Scanner(System.in);
     
    public static void main(String[] args)
    {
        String filePath = "savedata.txt";
        
        Concessionario c = null;
        try
        {
            c = new Concessionario(filePath, true); // crea un concessionario riempiendone i dati da file      
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Il file \""+filePath+"\" non esiste.");
        }
        catch (SecurityException e) // Catturo anche se è una RuntimeException per dare all'utente un output user-friendly
        {
            System.out.println("Il file \""+filePath+"\" non può essere aperto in lettura.");
            System.out.println(e.getMessage());
       }
        catch (IOException e)
        {
            System.out.println("Errori di I/O hanno impedito di completare la lettura del file \""+filePath+"\".");
            System.out.println(e.getMessage());
        }
        catch (FileParsingException e)
        {
            System.out.println("Il file \""+filePath+"\" non può essere correttamente letto perché il suo formato interno non è valido.");
            System.out.println(e.getMessage());
        } 
        catch (VeicoloDuplicatoException e)
        {
            System.out.println("La lettura dei dati del concessionario è fallita perché nel file \""+filePath+"\" sono presenti veicoli con la stessa targa.");
        }
        
        if (c == null)
        {
            // Se il concessionario è ancora null, ci sono stati errori nell'inizializzazione.
            // Termino l'esecuzione restituendo un codice di errore al terminale.
            System.exit(1);
        }
        else
        {
            System.out.println("----------------------");
            mostraMenu();

            boolean continua = true;
            do
            {
                String targa = "";
                String marca = "";
                int annoMin = 0;
                int annoMax = 0;
                float prezzo = 0;
                float dimensioneBagagliaio = 0;
                TipoVeicolo tipoVeicolo = TipoVeicolo.NESSUNO;
                Veicolo veicolo = null;
                boolean successo = false;
                Veicolo[] veicoli = new Veicolo[]{};

                System.out.println("----------------------");
                System.out.print("Cosa vuoi fare? ");

                switch (input.nextLine())
                {
                    case "1":
                        System.out.println("Catalogo concessionario:");
                        c.stampaCatalogo();
                        break;

                    case "2":                    
                        veicolo = creaVeicolo();                    
                        if (veicolo != null)
                        {
                            // Se il veicolo è diverso da null, la creazione è riuscita, ora proviamo ad aggiungerlo
                            // nel concessionario.
                            try
                            {
                                // Provo ad aggiungere un veicolo. Se l'aggiunta non può essere effettuata,
                                // perché già esiste un veicolo con la stessa targa, allora l'esecuzione del
                                // metodo terminerà anticipatamente lanciando un'eccezione.
                                // In questo caso, però, siccome esiste un blocco catch che cattura un'eccezione
                                // del tipo corretto, l'esecuzione del codice riprenderà all'interno del blocco
                                // catch.
                                c.aggiungiVeicolo(veicolo);

                                // Se esegue questa riga, vuol dire che il metodo aggiungiVeicolo ha eseguito
                                // il suo codice normalmente fino alla fine, senza terminare anticipatamente con eccezione,
                                // quindi l'aggiunta del veicolo è riuscita.
                                // Dunque, mostro in output un messaggio di successo.
                                System.out.println("Veicolo aggiunto con successo!");
                            }
                            catch(VeicoloDuplicatoException e) // eccezione lanciata dal metodo aggiungiVeicolo
                            {
                                // Se il metodo aggiungVeicolo lancia una eccezione di tipo VeicoloDuplicatoException
                                // vuol dire che cià esisteva un veicolo con la stessa targa.
                                // Il programma non ha nessuna logica risolutiva da poter eseguire per correggere
                                // l'errore, ma siccome siamo in un metodo main, dove ci si occupa dell'interazione
                                // con l'utente, e siccome l'errore è chiaramente nell'input della targa da parte
                                // dell'utente, in ultima istanza si può comunicare all'utente l'errore, indicandogli
                                // con un messaggio user-friendly che l'operazione non è riuscita a causa di un suo
                                // errore e come risolverlo.
                                // Il programma può continuare la sua esecuzione perché l'errore non ha avuto nessun
                                // effetto sui dati dell'applicazione, che sono quindi rimasti consistenti, altrimenti dopo
                                // la segnalazione dell'errore avrebbe dovuto terminare forzatamente la propria
                                // esecuzione richiamando il metodo System.exit per evitare di proseguire con dati corrotti
                                // senza che nessuno ne sia più a conoscenza.
                                System.out.println("Il veicolo creato ha una targa uguale a una già presente in concessionario. "
                                        + "Il veicolo non è stato aggiunto perché non è possibile che siano presenti nel concessionario due veicoli con la stessa targa.");

                                // IMPORTANTE
                                // Quando una eccezione viene catturata e viene mostrato un messaggio di errore, come in questo caso,
                                // l'esecuzione del programma continuerà normalmente dopo il blocco catch, il che vuol dire che 
                                // si sta impedendo a chiunque altro di catturarla a sua volta, quindi si sta a tutti gli 
                                // effetti "zittendo" un errore.
                                // Se, dopo aver "zittito" un errore, quest'ultimo ha avuto degli effetti sui dati,
                                // l'esecuzione continuerà con dati errati, cosa che è potenzialmente molto peggio di un crash,
                                // perché potrebbe portare problemi inattesi più avanti nell'esecuzione del programma,
                                // che, tra l'altro, sarebbero pure molto difficili da debuggare, perché conseguenza di
                                // errori che, essendo sono stati "zittiti" nel punto in cui avrebbero dovuto essere invece affrontati,
                                // adesso potrebbe essere impossibile ricostruire.
                                // La gestione di un errore implica la scrittura di un algoritmo apposito per far fronte alla situazione.
                                // Un esempio di logica di gestione di errore è quella del ritrasferimento di un messaggio TCP, in cui,
                                // per far fronte alla mancata ricezione dell'ACK per un messaggio inviato, si ritrasmette il messaggio.
                                // Stampare un messaggio a video, invece, vuol dire aver gestito un errore, vuol dire averlo comunicarlo all'utente
                                // in maniera user-friendly in modo che egli possa capire cosa è successo, magari darli qualche suggerimento
                                // per risolverlo, mentre il programma continua la sua esecuzione come se l'errore non fosse mai successo.
                                // Si sta quindi demandando la risoluzione del problema all'utente come ultima spiaggia.
                                // Tuttavia, l'utente deve occuparsi solo dei problemi che lo competono, come questo legato alla targa che
                                // non può essere duplicata, mentre è il programma che deve prendersi carico della risoluzione dei problemi
                                // che è in grado di gestire da solo.
                                // Se il problema non è qualcosa di cui l'utente può prendersi carico, nel catch non si mostra nessun output di errore,
                                // ma si gestisce l'errore.
                                // Se il programma non è in grado di gestire l'errore, non si usa affatto il try-catch, ma si lascia l'eccezione non
                                // catturata scrivendo nell'intestazione del metodo la clausola throws che dichiara che il metodo potrebbe
                                // lanciare l'eccezione, in modo da spostare il problema della gestione dell'eccezione alla funzione chiamante.
                                // Prima o poi qualcuno, tanto, dovrà gestire l'eccezione, oppure notificare l'utente che qualcosa è andato storto
                                // e non c'è altro da fare che terminare l'esecuzione del programma.
                                // Potrebbe capitare, in questo modo, che l'eccezione rimanga non gestita e che risalga tutto lo stack delle chiamate
                                // facendo crashare il programma. Questo è un caso contemplato nel meccanismo delle eccezioni,
                                // vuol dire che c'è un caso di errore che chi ha scritto il codice ha deciso che restasse non gestito
                                // (mettendo quindi una clausola throws nell'intestazione del main).
                            }
                            catch (IOException e)
                            {
                                System.out.println("Veicolo aggiunto con successo, ma il salvataggio delle modifiche sul file \""+filePath+"\" è fallito.");
                            }
                        }
                        else
                        {
                            // Se il veicolo creato è null, vuol dire che ci sono stati errori nella creazione
                            // del veicolo (prima ancora che si provi ad aggiungerlo nel concessionario)
                            System.out.println("Operazione fallita per errori nella creazione del veicolo");
                        }
                        break;

                    case "3":
                        System.out.println("Vendita di un veicolo");
                        System.out.print(" >>> Targa: ");
                        targa = input.nextLine();
                        try
                        {
                            successo = c.vendiVeicolo(targa);
                            if (successo)
                            {
                                System.out.println("Veicolo venduto con successo! Saldo corrente: " + c.getSaldo() + " €");
                            }
                            else
                                System.out.println("Non è stato trovato nessun veicolo con la targa richiesta.");
                        }
                        catch (IOException e)
                        {
                            System.out.println("Veicolo venduto con successo! Saldo corrente: " + c.getSaldo() + " €");
                            System.out.println("Veicolo aggiunto con successo, ma il salvataggio delle modifiche sul file \""+filePath+"\" è fallito.");
                        }
                        break;

                    case "4":
                        System.out.println("Ricerca per marca");
                        System.out.print(" >>> Marca: ");
                        marca = input.nextLine();
                        System.out.print(" >>> Filtra per [auto, moto, altro = non filtrare]: ");
                        tipoVeicolo = inputTipoVeicolo();
                        veicoli = (tipoVeicolo != TipoVeicolo.NESSUNO) ? c.cercaPerMarca(marca, tipoVeicolo) : c.cercaPerMarca(marca);
                        stampaRisultato(veicoli);
                        break;

                    case "5":
                        try
                        {
                            System.out.println("Ricerca per anno tra due date");
                            System.out.print(" >>> dall'anno: ");
                            annoMin = Integer.parseInt(input.nextLine());
                            System.out.print(" >>> all'anno: ");
                            annoMax = Integer.parseInt(input.nextLine());
                            System.out.print(" >>> Filtra per [auto, moto, altro = non filtrare]: ");
                            tipoVeicolo = inputTipoVeicolo();
                            veicoli = (tipoVeicolo != TipoVeicolo.NESSUNO) ? c.cercaPerAnno(annoMin, annoMax, tipoVeicolo) : c.cercaPerAnno(annoMin, annoMax);
                            stampaRisultato(veicoli);
                        }
                        catch (NumberFormatException e) // eccezione lanciata da Integer.parseInt
                        {
                            System.out.println("Errore di input: inserire un numero intero.");
                        }
                        break;

                    case "6":
                        System.out.println("Ricerca per targa");
                        System.out.print(" >>> Targa: ");
                        targa = input.nextLine();
                        veicolo = c.cercaVeicolo(targa);
                        if (veicolo != null)
                            System.out.println(veicolo);
                        else
                            System.out.println("Veicolo non trovato!");
                        break;

                    case "7":
                        try
                        {
                            System.out.println("Ricerca auto per capienza minima del bagagliaio");
                            System.out.print(" >>> Capienza minima (litri): ");
                            dimensioneBagagliaio = Float.parseFloat(input.nextLine());
                            veicoli = c.cercaAutoPerCapienzaBagagliaio(dimensioneBagagliaio);
                            stampaRisultato(veicoli);
                        }
                        catch (NumberFormatException e) // eccezione lanciata da Integer.parseInt
                        {
                            System.out.println("Errore di input: inserire un numero decimale.");
                        }
                        break;

                    case "0":
                        continua = false;
                        break;

                    case "?":
                        mostraMenu();
                        break;

                    default:
                        System.out.println("Scelta non valida.");
                }
            } while (continua);
        }
    }
    
    private static void stampaRisultato (Veicolo[] veicoli)
    {
        if (veicoli.length == 0)
            System.out.println("Nessun veicolo trovato.");
        else
        {
            System.out.println("Veicoli trovati:");
            for (Veicolo v : veicoli)
            if (v != null)
                System.out.println(v);
        }
    }
    
    private static TipoVeicolo inputTipoVeicolo()
    {
        String nomeDaCercare = input.nextLine().trim().toUpperCase(); // rimuove gli spazi iniziali e finali e converte in maiuscolo
        for (TipoVeicolo t : TipoVeicolo.values()) // itera tutti i valori della enum
            if (t != TipoVeicolo.NESSUNO && t.toString().toUpperCase().equals(nomeDaCercare))
                return t; // se trova il tipo lo restituisce
        return TipoVeicolo.NESSUNO;
    }
    
    private static void mostraMenu()
    {
        System.out.println("--- MENU DEI COMANDI ---");
        System.out.println("1 - Stampa catalogo");
        System.out.println("2 - Aggiungi veicolo");
        System.out.println("3 - Vendi veicolo");
        System.out.println("4 - Cerca per marca");
        System.out.println("5 - Cerca per anno");
        System.out.println("6 - Cerca per targa");
        System.out.println("7 - Cerca auto per capienza minima del bagagliaio");
        System.out.println("0 - esci");
        System.out.println("? - Menu dei comandi");
    }
    
    /**
     * Crea un veicolo gestendo input e output. Se il parametro è true crea un'auto, sennò una moto
     * @param auto se true, crea un auto, sennò una moto
     * @return il veicolo creato, null se ci sono stati errori di input.
     */
    private static Veicolo creaVeicolo()
    {
        System.out.println("Inserimento di un nuovo veicolo");
       
        String targa = "";
        String marca = "";
        String modello = "";
        int anno = 0;
        float prezzo = 0f;
        int numeroPosti = 0;
        float dimensioneBagagliaio = 0f;
        
        System.out.print(" >>> Tipo [1 = auto, 2 = moto]: ");
        try
        {
            int tipo = Integer.parseInt(input.nextLine());

            if (tipo == 1 || tipo == 2)
            {
                System.out.print(" >>> Targa: ");
                targa = input.nextLine();
                System.out.print(" >>> Marca: ");
                marca = input.nextLine();
                System.out.print(" >>> Modello: ");
                modello = input.nextLine();                    
                System.out.print(" >>> Anno di produzione: ");
                anno = Integer.parseInt(input.nextLine());                                        
                System.out.print(" >>> Prezzo (€): ");
                prezzo = Float.parseFloat(input.nextLine());

                if (tipo == 1)
                {
                    System.out.print(" >>> Omologata per (numero di posti): ");
                    numeroPosti = Integer.parseInt(input.nextLine());                                        
                    System.out.print(" >>> Dimensione bagagliaio (litri): ");
                    dimensioneBagagliaio = Float.parseFloat(input.nextLine());
                    return new Auto(targa, marca, modello, anno, prezzo, numeroPosti, dimensioneBagagliaio);
                }
                else if (tipo == 2)
                {
                    return new Moto(targa, marca, modello, anno, prezzo);
                }
            }
            else
            {
                System.out.print("Tipo di veicolo non valido!");
            }
        }
        catch (NumberFormatException e) // eccezione lanciata da Integer.parseInt
        {
            System.out.println("Errore di input: inserire un valore numerico.");
        }
        
        return null;
    }
    
}
