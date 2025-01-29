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
public class FileParsingException extends Exception {
    public FileParsingException() { super(); }
    public FileParsingException(String message) { super(message); }
    public FileParsingException(String message, Throwable cause) { super(message, cause); }
    public FileParsingException(Throwable cause) { super(cause); }
}
