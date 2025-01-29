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
public enum TipoVeicolo
{
    NESSUNO
    , AUTO
    , MOTO
    ;
    
    public static TipoVeicolo fromString(String s)
    {
        if (s == null || s.isEmpty())
            return TipoVeicolo.NESSUNO;
        else
        {
            for (TipoVeicolo t : TipoVeicolo.values())
                if (t.toString().toUpperCase().equals(s.toUpperCase()))
                    return t;
        }
        
        // Se l'esecuzione arriva a questo punto, non è stato trovato un valore appropriato
        // Lancio un'eccezione analoga a quelle lanciate da Integer.parseInt e simili.
        // Quando si vuole usare una eccezione di libreria si deve indagare su perché esiste
        // e come viene usata, non basta sceglierla solo per il nome, perché le funzioni di
        // libreria vengono a volte usate per ragioni specifiche.
        // (Per esempio, se si lancia InterruptedException molto probabilmente si creano
        // seri bug nella gestione dei programmi multithread).
        // In questo caso, la scelta NumberFormatException è un po' tirata, ma considerando la enum
        // come l'estensione di un tipo numerico è abbastanza giustificabile. 
        throw new NumberFormatException("\""+s+"\" non corrisponde a nessun valore della enum TipoVeicolo");
    }
}
