package com.example.webapp.playground.deadlocks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankTransfer {
    public static void transfer(BankAccount from, BankAccount to, int amount) {
        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();

        try {
            if (lock1.tryLock()) {
                try {
                    if (lock2.tryLock()) {
                        try {
                            from.withdraw(amount);
                            to.deposit(amount);
                            System.out.println("Transfer successful: " + amount + " from " + from.getName() + " to " + to.getName());
                        } finally {
                            lock2.unlock();
                        }
                    }
                } finally {
                    lock1.unlock();
                }
            }
        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }
}
