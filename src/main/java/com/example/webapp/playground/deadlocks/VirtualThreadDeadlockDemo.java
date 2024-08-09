package com.example.webapp.playground.deadlocks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualThreadDeadlockDemo {
    public static void main(String[] args) {
        BankAccount account1 = new BankAccount("Alice", 1000);
        BankAccount account2 = new BankAccount("Bob", 1000);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 10; i++) {  // Create multiple tasks
                executor.submit(() -> {
                    for (int j = 0; j < 1000; j++) {
                        BankTransfer.transfer(account1, account2, 10);
                        Thread.sleep(10);  // Add a small delay
                    }
                    return null;
                });

                executor.submit(() -> {
                    for (int j = 0; j < 1000; j++) {
                        BankTransfer.transfer(account2, account1, 10);
                        Thread.sleep(10);  // Add a small delay
                    }
                    return null;
                });
            }

            // Wait for user input before shutting down
            System.out.println("Application running. Press Enter to stop...");
            new java.util.Scanner(System.in).nextLine();

            executor.shutdown();
        }
    }
}