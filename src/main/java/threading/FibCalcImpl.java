package threading;

/**
 * Created by mike on 14.06.17 16:23
 */
public class FibCalcImpl implements FibCalc {
    @Override
    public long fib(int n) {
        if (n < 3) return n == 0 ? 0 : 1;

        long fibonachiNum = 0;
        long first = 0;
        long second = 1;
        long count = 0;

        while (++count < n) {
            fibonachiNum = first + second;
            first = second;
            second = fibonachiNum;
        }
        return fibonachiNum;
    }
}