/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package volpintesta.concessionaire;

import java.util.Scanner;

/**
 *
 * @author User
 */
public class MainClass
{
    private static Scanner input = new Scanner(System.in);
    
    /**
     * Il seguente main funziona soltanto fino alla parte 3
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Concessionario c = new Concessionario(); // crea un concessionario vuoto
        c.aggiungiVeicolo(new Auto("AA 111 AA", "BMW", "X1", 2020, 40000, 4, 540));
        c.aggiungiVeicolo(new Auto("BB 222 BB", "BMW", "X1", 2022, 50000, 5, 540));
        c.aggiungiVeicolo(new Auto("CC 333 CC", "BMW", "X3", 2023, 70000, 4, 570));
        c.aggiungiVeicolo(new Auto("DD 444 DD", "Ferrari", "F40", 1987, 3000000, 2, 35));
        c.aggiungiVeicolo(new Auto("EE 555 EE", "Lamborghini Urus", 2021, 245000, 5, 616));
        c.aggiungiVeicolo(new Moto("FF 666 FF", "Ducati", "Panigale V4", 2024, 34000));
        c.aggiungiVeicolo(new Moto("GG 777 GG", "Kawasaki Ninja 650", 2022, 8000));
        
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
                    successo = (veicolo != null) ? c.aggiungiVeicolo(veicolo) : false;
                    if (successo)
                        System.out.println("Veicolo aggiunto con successo!");
                    else
                        System.out.println("ERRORE: non può essere aggiunto un veicolo con la stessa targa di uno già esistente.");
                    
                    break;
                    
                case "3":
                    System.out.println("Vendita di un veicolo");
                    System.out.print(" >>> Targa: ");
                    targa = input.nextLine();
                    successo = c.vendiVeicolo(targa);
                    if (successo)
                        System.out.println("Veicolo venduto con successo! Saldo corrente: " + c.getSaldo() + " €");
                    else
                        System.out.println("Non è stato trovato nessun veicolo con la targa richiesta.");
                    break;
                    
                case "4":
                    System.out.println("Ricerca per marca");
                    System.out.print(" >>> Marca: ");
                    marca = input.nextLine();
                    veicoli = c.cercaPerMarca(marca);
                    stampaRisultato(veicoli);
                    break;
                    
                case "5":
                    System.out.println("Ricerca per anno tra due date");
                    System.out.print(" >>> dall'anno: ");
                    annoMin = Integer.parseInt(input.nextLine());
                    System.out.print(" >>> all'anno: ");
                    annoMax = Integer.parseInt(input.nextLine());
                    veicoli = c.cercaPerAnno(annoMin, annoMax);
                    stampaRisultato(veicoli);
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
    
    private static void mostraMenu()
    {
        System.out.println("--- MENU DEI COMANDI ---");
        System.out.println("1 - Stampa catalogo");
        System.out.println("2 - Aggiungi veicolo");
        System.out.println("3 - Vendi veicolo");
        System.out.println("4 - Cerca per marca");
        System.out.println("5 - Cerca per anno");
        System.out.println("6 - Cerca per targa");
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
        
        return null;
    }
}
