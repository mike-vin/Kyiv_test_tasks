package threading;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mike on 14.06.17 16:45
 */
public class FibCalcImplTest {
    @Test
    public void fib() throws Exception {
        FibCalcImpl fibCalc = new FibCalcImpl();
        assertEquals(0, fibCalc.fib(0));
        assertEquals(1, fibCalc.fib(1));
        assertEquals(1, fibCalc.fib(2));
        assertEquals(2, fibCalc.fib(3));
        assertEquals(3, fibCalc.fib(4));
        assertEquals(5, fibCalc.fib(5));
        assertEquals(8, fibCalc.fib(6));
        assertEquals(13, fibCalc.fib(7));
        assertEquals(21, fibCalc.fib(8));

        assertEquals(75025, fibCalc.fib(25));

        System.out.println(fibCalc.fib(75));
    }
}