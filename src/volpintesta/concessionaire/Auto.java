/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package volpintesta.concessionaire;

/**
 *
 * @author User
 */
public class Auto extends Veicolo
{
    @Override
    protected String getTipoPerStampa() { return "auto"; }
    
    private int numeroPosti;
    /**
     * Restituisce il numero di posti per cui l'auto è omologata.
     * @return il numero di posti per cui l'auto è omologata.
     */
    @Override
    public int getNumeroPosti() { return numeroPosti; }
    
    private float dimensioneBagagliaio;
    /**
     * Restituisce la dimensione del bagagliaio in litri
     * @return la dimensione del bagagliaio in litri
     */
    public float getDimensioneBagagliaio() { return dimensioneBagagliaio; }
    
    /**
     * Crea un'auto con i valori specificati
     * @param targa la targa del veicolo
     * @param marca la marca (il nome della casa produttrice)
     * @param modello il nome del modello
     * @param annoProduzione l'anno di produzione
     * @param prezzo il prezzo di vendita
     * @param numeroPosti il numero di posti per il quale è omologata
     * @param dimensioneBagagliaio la dimensione del bagagliaio in litri
     */
    public Auto (String targa, String marca, String modello, int annoProduzione, float prezzo, int numeroPosti, float dimensioneBagagliaio)
    {
        super (targa, marca, modello, annoProduzione, prezzo);
        this.numeroPosti = numeroPosti;
        this.dimensioneBagagliaio = dimensioneBagagliaio;
    }
    
    /**
     * Crea un'auto con i valori specificati
     * @param targa la targa del veicolo
     * @param nomeCompletoModello il nome completo del modellom da suddividersi in targa più modello sul primo spazio
     * @param annoProduzione l'anno di produzione
     * @param prezzo il prezzo di vendita
     * @param numeroPosti il numero di posti per il quale è omologata
     * @param dimensioneBagagliaio la dimensione del bagagliaio in litri
     */
    public Auto (String targa, String nomeCompletoModello, int annoProduzione, float prezzo, int numeroPosti, float dimensioneBagagliaio)
    {
        super (targa, nomeCompletoModello, annoProduzione, prezzo);
        this.numeroPosti = numeroPosti;
        this.dimensioneBagagliaio = dimensioneBagagliaio;
    }
    
    @Override
    public String toString() {
        return String.format("%s, bagagliaio di %.0f litri", super.toString(), dimensioneBagagliaio);
    }    
}
