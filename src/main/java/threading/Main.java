package threading;

import java.util.Scanner;

/**
 * Created by mike on 14.06.17 17:30
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nadd Fibonachi Number> ");
        int fibonachiNum = scanner.nextInt();

        System.out.print("\nadd execution count> ");
        int executionCount = scanner.nextInt();

        System.out.print("\nadd thread Pool count> ");
        int threadPoolCount = scanner.nextInt();

        PerformanceTesterImpl performanceTester = new PerformanceTesterImpl();
        PerformanceTestResult testResult = performanceTester.runPerformanceTest(
                () -> new FibCalcImpl().fib(fibonachiNum)
                , executionCount
                , threadPoolCount);

        System.out.println(testResult);
    }
}