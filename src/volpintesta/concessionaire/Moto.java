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
public class Moto extends Veicolo
{
    @Override
    public int getNumeroPosti()
    {
        // E' sempre omologata per 2, quindi non serve un attributo,
        // ma va bene restituire sempre lo stesso valore.
        return 2;
    } 

    /**
     * Crea una moto con i valori specificati
     * @param targa la targa del veicolo
     * @param marca la marca (il nome della casa produttrice)
     * @param modello il nome del modello
     * @param annoProduzione l'anno di produzione
     * @param prezzo il prezzo di vendita
     */
    public Moto (String targa, String marca, String modello, int annoProduzione, float prezzo)
    {
        super (targa, marca, modello, annoProduzione, prezzo);
    }
    
    /**
     * Crea una moto con i valori specificati
     * @param targa la targa del veicolo
     * @param nomeCompletoModello il nome completo del modellom da suddividersi in targa più modello sul primo spazio
     * @param annoProduzione l'anno di produzione
     * @param prezzo il prezzo di vendita
     */
    public Moto (String targa, String nomeCompletoModello, int annoProduzione, float prezzo)
    {
        super (targa, nomeCompletoModello, annoProduzione, prezzo);
    }  
}
