package com.example.webapp.playground.deadlocks;

import lombok.Getter;

@Getter
public class BankAccount {
    private final String name;
    private int balance;

    public BankAccount(String name, int initialBalance) {
        this.name = name;
        this.balance = initialBalance;
    }

    public void withdraw(int amount) {
        balance -= amount;
    }

    public void deposit(int amount) {
        balance += amount;
    }

}
