package pl.polsl.lab.model;

// packages containing class definitions 
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * A class that performs calculations on primary numbers
 *
 * @author Przemysław Drążkiewicz
 * @version 3.0
 */
public class PrimeNumbersModel {

    // beginning of the range 
    private int startRange;
    // end of range
    private int endRange;
    // number representing potential prime number
    private int number;
    // nth prime number
    private int nthPrimeNumber;
    // number representing result of nth prime number to check
    private int nthPrimeNumberResult;
    // typically a secure class of objects containing range of prime numbers
    private List<Integer> arrayOfPrimeNumbers;
    // collection stores data
    private Hashtable<String, String> stats;

    // specification of the lambda expression with two parameters
    interface IntegerMath {

        boolean operation(int a, int b);
    }

    //implements of the lambda expression
    IntegerMath range = (a, b) -> {
        if ((a - b) < 0) {
            return false;
        }
        return true;
    };

    //constructor
    public PrimeNumbersModel() {
        stats = new Hashtable<String, String>();
    }

    /**
     * Check if the number is a prime number
     *
     * @param number - value representing potential prime number
     * @return true if the value is prime number, otherwise false
     */
    public boolean isPrime(int number) {

        // 2 is primary number
        if (number == 2) {
            return true;
        } else if (number > 2) {
            //if the number is divisible by 2 then it isn't prime number
            if (number % 2 == 0) {
                return false;
            } else {
                double sqrtNumber = Math.sqrt(number);
                for (int i = 3; i <= sqrtNumber; i += 2) {
                    if (number % i == 0) {
                        return false;
                    }
                }
            }
            //if the divisor wasn't found return true
            return true;
        } else {
            //if number is less than 2 then return false
            return false;
        }
    }

    /**
     * Sets the prime numbers in the given range
     *
     * @throws PrimeNumbersException - own defined exception thrown when occurs
     * invalid range
     */
    @SuppressWarnings("unchecked")
    public void primeNumbersInRange() throws PrimeNumbersException {

        //if first value of range is greater than second value throw an own exception
        //using lambda expression
        if ((range.operation(this.startRange, this.endRange))) {
            throw new PrimeNumbersException("Invalid range");
        }
        arrayOfPrimeNumbers = new ArrayList();
        //a loop checks successive numbers in the range
        for (int i = this.startRange; i <= this.endRange; i++) {
            if (isPrime(i)) {
                arrayOfPrimeNumbers.add(i);
            }
        }
    }

    /**
     * Sets nth prime number
     *
     * @throws PrimeNumbersException - own defined exception thrown when value
     * is less than 1
     */
    public void nthPrimeNumber() throws PrimeNumbersException {
        //value representing the nth prime number
        int counter = this.nthPrimeNumber;
        //if value is incorrect throw an own exception
        if (counter < 1) {
            throw new PrimeNumbersException("Invalid nth prime number");
        }
        //value representing first prime number
        int num = 2;
        while (counter > 0) {
            if (isPrime(num)) {
                counter--;
            }
            num++;
        }
        //if counter=0 the nth prime number is num minus 1
        this.nthPrimeNumberResult = --num;
    }

    /**
     * Sets the value of the private field "startRange"
     *
     * @param startRange - containing the value of nthPrimeNumber
     */
    public void setStartRange(int startRange) {
        this.startRange = startRange;
    }

    /**
     * Sets the value of the private field "endRange"
     *
     * @param endRange - containing the value of endRange
     */
    public void setEndRange(int endRange) {
        this.endRange = endRange;
    }

    /**
     * Returns the value of the private field "startRange"
     *
     * @return int containing the value of the private field in the
     * {@link PrimeNumbersModel}.
     */
    public int getStartRange() {
        return startRange;
    }

    /**
     * Returns the value of the private field "endRange"
     *
     * @return int containing the value of the private field in the
     * {@link PrimeNumbersModel}.
     */
    public int getEndRange() {
        return endRange;
    }

    /**
     * Sets the value of the private field "number"
     *
     * @param number - containing the value of number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Returns the value of the private field "number"
     *
     * @return int containing the value of the private field in the
     * {@link PrimeNumbersModel}.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets the value of the private field "nthPrimeNumber"
     *
     * @param nthPrimaryNumber - containing the value of nthPrimeNumber
     */
    public void setNthPrimeNumber(int nthPrimaryNumber) {
        this.nthPrimeNumber = nthPrimaryNumber;
    }

    /**
     * Returns the value of the private field "nthPrimeNumber"
     *
     * @return int containing the value of the private field in the
     * {@link PrimeNumbersModel}.
     */
    public int getNthPrimeNumber() {
        return nthPrimeNumber;
    }

    /**
     * Returns the value of the private field "nthPrimeNumberResult"
     *
     * @return int containing the value of the private field in the
     * {@link PrimeNumbersModel}.
     */
    public int getNthPrimeNumberResult() {
        return nthPrimeNumberResult;
    }

    /**
     * Returns the array of the private field "arrayOfPrimeNumbers"
     *
     * @return array containing the values of the private field in the
     * {@link PrimeNumbersModel}.
     */
    public List<Integer> getArrayOfPrimeNumbers() {
        return arrayOfPrimeNumbers;
    }

    /**
     * Returns the Hashtable of statistics
     *
     * @return history of range prime numbers
     */
    public Hashtable<String, String> getStats() {
        return stats;
    }

    /**
     * Puts two string into Hashtable of history calculate
     *
     * @param key range
     * @param value prime numbers in range
     */
    public void putStats(String key, String value) {
        stats.put(key, value);
    }

}
