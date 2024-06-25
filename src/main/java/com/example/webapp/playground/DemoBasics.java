package com.example.webapp.playground;

import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DemoBasics {

    @SneakyThrows
    public static void main(String[] args) {
        basics();
        System.out.println("~~~~~ Executors");
        basicsExecutors();
        System.out.println("~~~~~ Completable Future");
        basicsCompletableFuture();
    }

    @SneakyThrows
    private static void basicsCompletableFuture() {
        var completableFuture = CompletableFuture.runAsync(() -> System.out.println(Thread.currentThread() + " - First"), Executors.newVirtualThreadPerTaskExecutor());

        completableFuture.thenAccept(r -> System.out.println(r + " result"));
    }

    @SneakyThrows
    private static void basicsExecutors() {

        try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            executorService.submit(() -> System.out.println(Thread.currentThread() + " - First"));
        }

        try (var myExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<?> future = myExecutor.submit(() -> System.out.println(Thread.currentThread() + " - Second"));
            future.get();
        }
    }

    private static void basics() throws InterruptedException {
        //use prefix to identify in vm profiler, name
        Thread thread1 = Thread.startVirtualThread(() -> System.out.println(Thread.currentThread() + " - First"));

        Thread thread2 = Thread.ofVirtual().name("Some name").factory().newThread(() -> System.out.println(Thread.currentThread() + " - Second"));
        thread2.start();

        Thread thread3 = Thread.ofVirtual().start(() -> System.out.println(Thread.currentThread() + " - Third"));

        //simple thread
        Thread thread4 = new Thread(() -> System.out.println(Thread.currentThread() + " - Fourth"));
        thread4.start();

        Thread thread5 = Thread.ofPlatform().start(() -> System.out.println(Thread.currentThread() + " - Fifth"));

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        thread5.join();
    }


}
