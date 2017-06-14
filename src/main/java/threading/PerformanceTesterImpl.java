package threading;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by mike on 14.06.17 16:25
 */
public class PerformanceTesterImpl implements PerformanceTester {

    @Override
    public PerformanceTestResult runPerformanceTest
            (Runnable task, int executionCount, int threadPoolSize) throws InterruptedException {
        List<Long> time = new LinkedList<>();
        Executor executor = Executors.newFixedThreadPool(threadPoolSize);
        for (int i = 0; i < executionCount; i++) {
            executor.execute(task);
            //todo add time results into list >> more time i need
        }
        Long min = time.stream().mapToLong(Long::longValue).min().getAsLong();
        Long max = time.stream().mapToLong(Long::longValue).max().getAsLong();
        Long total = time.stream().mapToLong(Long::longValue).sum();
        return new PerformanceTestResult(total, min, max);
    }
}