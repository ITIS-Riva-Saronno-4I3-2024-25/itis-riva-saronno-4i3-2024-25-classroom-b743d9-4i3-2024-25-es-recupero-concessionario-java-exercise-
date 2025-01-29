/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package volpintesta.concessionaire;

/**
 * Eccezione lanciata quando viene effettuata un'operazione sul concessionario che
 * avrebbe rotto il vincolo secondo cui non possono esserci veicoli con la stessa
 * targa.
 * @author User
 */
public class VeicoloDuplicatoException extends Exception
{
    // Reimplemento tutti i costruttori della superclasse
    public VeicoloDuplicatoException() { super(); }
    public VeicoloDuplicatoException(String message) { super(message); }
    public VeicoloDuplicatoException(String message, Throwable cause) { super(message, cause); }
    public VeicoloDuplicatoException(Throwable cause) { super(cause); }
}
