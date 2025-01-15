/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package volpintesta.concessionaire;

import java.time.LocalDate;

/**
 *
 * @author User
 */
public class Veicolo
{
    public  final static String NOT_DEFINED_STRING = "%ND"; // Ok public perche' è costante
    
    private String targa;    
    private String marca;
    private String modello;
    private int annoProduzione;
    private float prezzo;
    
    public String getTarga(){
        return targa;
    }
    public String getMarca(){
        return marca;
    }
    public String getModello(){
        return modello;
    }
    public String getNomeCompletoModello(){
        return marca+" "+modello;
    }
    public int getAnno(){
        return annoProduzione;
    }
    public float getPrezzo(){
        return prezzo;
    }
    
    /** Crea un nuovo veicolo con i valori specificati
     * @param targa la targa del veicolo
     * @param marca la marca (il nome della casa produttrice)
     * @param modello il nome del modello
     * @param annoProduzione l'anno di produzione
     * @param prezzo il prezzo di vendita
     */
    public Veicolo (String targa, String marca, String modello, int annoProduzione, float prezzo)
    {
        this.annoProduzione=annoProduzione;
        this.targa=targa;
        this.marca=marca;
        this.modello=modello;
        this.prezzo = prezzo;
    }
    
    /** Crea un nuovo veicolo con i valori specificati
     * @param targa la targa del veicolo
     * @param nomeCompletoModello il nome completo del modellom da suddividersi in targa più modello sul primo spazio
     * @param annoProduzione l'anno di produzione
     * @param prezzo il prezzo di vendita 
     */
    public Veicolo (String targa, String nomeCompletoModello, int annoProduzione, float prezzo)
    {
        this(targa, calcolaMarca(nomeCompletoModello), calcolaModello(nomeCompletoModello), annoProduzione, prezzo);
    }
    
    @Override
    public String toString() {
        return targa+": "+getNomeCompletoModello()+" del "+annoProduzione+" ("+calcolaEtaVeicolo()+" anni)";
    }
    
    public int calcolaEtaVeicolo(){
        return LocalDate.now().getYear() - annoProduzione;
    }
    
    private static String calcolaMarca(String nomeCompletoModello)
    {
        String[] parti = nomeCompletoModello.split(" ");
        return (parti.length > 0) ? parti[0] : NOT_DEFINED_STRING;
    }
    
    private static String calcolaModello(String nomeCompletoModello)
    {
        String marca = calcolaMarca(nomeCompletoModello);
        // marca.length() + 1 perche' si toglie anche il primo spazio separatore.
        // Nota: in questo caso è corretto il controllo con != invece che con equals
        // perche' la stringa restituita che indica errore è costante.
        return (marca != NOT_DEFINED_STRING) ? nomeCompletoModello.substring(marca.length()+1) : NOT_DEFINED_STRING;
    }
}
