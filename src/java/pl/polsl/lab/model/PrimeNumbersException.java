package pl.polsl.lab.model;

/**
 * Exception class for object thrown when user pass 1
 *
 * @author Przemysław Drążkiewicz
 * @version 1.0
 */
public class PrimeNumbersException extends Exception{
   
    /**
     * Non-parameter constructor 
     */
    public PrimeNumbersException() {
    }

    /**
     * Constructor
     *
     * @param message display message
     */
    public PrimeNumbersException(String message) {
        super(message);
    }
}
