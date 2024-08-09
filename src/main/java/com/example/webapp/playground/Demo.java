package com.example.webapp.playground;

import lombok.SneakyThrows;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class Demo {

    @SneakyThrows
    public static void main(String[] args) {
        long virtualThreadsTime, platformThreadsTime;

        System.out.println("~~~~~ Heavy Load Comparison");
        long[] comparisonTimes = heavyLoadComparison();
        virtualThreadsTime = comparisonTimes[0];
        platformThreadsTime = comparisonTimes[1];

        System.out.println("~~~~~ Summary");
        System.out.println("Virtual threads heavy load time: " + virtualThreadsTime + "ms, " +
                "Platform threads heavy load time: " + platformThreadsTime + "ms");
    }

    @SneakyThrows
    private static long[] heavyLoadComparison() {
        int taskCount = 10000;
        long virtualThreadsTime, platformThreadsTime;

        System.out.println("Running with virtual threads...");
        Instant start = Instant.now();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = IntStream.range(0, taskCount)
                    .mapToObj(i -> executor.submit(() -> simulateHeavyTask(i)))
                    .toArray(Future[]::new);
            for (Future<?> future : futures) {
                future.get();
            }
        }
        Instant end = Instant.now();
        virtualThreadsTime = Duration.between(start, end).toMillis();
        System.out.println("Virtual threads time: " + virtualThreadsTime + "ms");

        System.out.println("Running with platform threads...");
        start = Instant.now();
        try (var executor = Executors.newFixedThreadPool(1000)) {
            var futures = IntStream.range(0, taskCount)
                    .mapToObj(i -> executor.submit(() -> simulateHeavyTask(i)))
                    .toList();
            for (Future<?> future : futures) {
                future.get();
            }
        }
        end = Instant.now();
        platformThreadsTime = Duration.between(start, end).toMillis();
        System.out.println("Platform threads time: " + platformThreadsTime + "ms");

        return new long[]{virtualThreadsTime, platformThreadsTime};
    }


    @SneakyThrows
    private static void simulateHeavyTask(int taskId) {
        System.out.println(Thread.currentThread() + " - Task " + taskId + " started");
        Thread.sleep(1000);
        System.out.println(Thread.currentThread() + " - Task " + taskId + " completed");
    }
}
