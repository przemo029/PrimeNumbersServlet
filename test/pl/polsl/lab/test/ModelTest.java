package pl.polsl.lab.test;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import pl.polsl.lab.model.PrimeNumbersException;
import pl.polsl.lab.model.PrimeNumbersModel;

/**
 *
 * @author Przemysław Drążkiewicz
 * @version 1.0
 */
public class ModelTest {

    PrimeNumbersModel model;

    //initializing PrimeNumbersModel
    @Before
    public void setup() {
        model = new PrimeNumbersModel();
    }

    //Tests the isPrime method
    @Test
    public void isPrimeTest() {
        assertTrue(model.isPrime(19));
        assertTrue(model.isPrime(2));
        assertFalse(model.isPrime(1));
        assertFalse(model.isPrime(0));
        assertFalse(model.isPrime(-1));
        assertTrue(model.isPrime(2147483647));
        assertFalse(model.isPrime(-213));
    }
    
    //Tests the nthPrimeNumber method with unsettled arguments
    @Test
    public void nthPrimeNumberTestWithoutSetArgs() {
        try {
            model.nthPrimeNumber();
            fail("An exception should be thrown when occurs invalid value");
        } catch (PrimeNumbersException e) {
        }
    }

    //Tests the nthPrimeNumber method
    @Test
    public void nthPrimeNumberTest() {
        model.setNthPrimeNumber(5);
        try {
            model.nthPrimeNumber();
        } catch (PrimeNumbersException e) {
        }
        assertEquals(model.getNthPrimeNumberResult(), 11);

        model.setNthPrimeNumber(1);
        try {
            model.nthPrimeNumber();
        } catch (PrimeNumbersException e) {
        }
        assertEquals(model.getNthPrimeNumberResult(), 2);

        model.setNthPrimeNumber(500);
        try {
            model.nthPrimeNumber();
        } catch (PrimeNumbersException e) {
        }
        assertEquals(model.getNthPrimeNumberResult(), 3571);

    }

    //Tests the nthPrimeNumber method with exception
    @Test
    public void nthPrimeNumberTestException() {
        model.setNthPrimeNumber(0);
        try {
            model.nthPrimeNumber();
            fail("An exception should be thrown when occurs invalid value");
        } catch (PrimeNumbersException e) {
        }

        model.setNthPrimeNumber(-50);
        try {
            model.nthPrimeNumber();
            fail("An exception should be thrown when occurs invalid value");
        } catch (PrimeNumbersException e) {
        }
    }

    //Tests the primeNumberInRange method with unsettled arguments
    @Test
    public void primeNumbersInRangeTestWithoutSetArgs() {
        try {
            model.primeNumbersInRange();
            fail("An exception should be thrown when the arguments have not been set");
        } catch (PrimeNumbersException e) {
        }
    }

    //Tests the primeNumberInRange method
    @Test
    public void primeNumbersInRangeTest() {
        model.setStartRange(10);
        model.setEndRange(30);
        try {
            model.primeNumbersInRange();
        } catch (PrimeNumbersException e) {
        }
        int[] expectedResult = new int[]{11, 13, 17, 19, 23, 29};
        int[] result = model.getArrayOfPrimeNumbers().stream().mapToInt(Integer::intValue).toArray();
        assertTrue(Arrays.equals(result, expectedResult));

        model.setStartRange(1);
        model.setEndRange(20);
        try {
            model.primeNumbersInRange();
        } catch (PrimeNumbersException e) {
        }
        expectedResult = new int[]{2, 3, 5, 7, 11, 13, 17, 19};
        result = model.getArrayOfPrimeNumbers().stream().mapToInt(Integer::intValue).toArray();
        assertTrue(Arrays.equals(result, expectedResult));

        model.setStartRange(-20);
        model.setEndRange(20);
        try {
            model.primeNumbersInRange();
        } catch (PrimeNumbersException e) {
        }
        expectedResult = new int[]{2, 3, 5, 7, 11, 13, 17, 19};
        result = model.getArrayOfPrimeNumbers().stream().mapToInt(Integer::intValue).toArray();
        assertTrue(Arrays.equals(result, expectedResult));

    }

    //Tests the primeNumberInRange method with exception
    @Test
    public void primeNumbersInRangeTestException() {
        model.setStartRange(60);
        model.setEndRange(30);
        try {
            model.primeNumbersInRange();
            fail("An exception should be thrown when occurs invalid value");
        } catch (PrimeNumbersException e) {
        }
    }
}
