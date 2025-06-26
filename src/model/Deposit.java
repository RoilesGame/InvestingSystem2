package model;

public class Deposit {
    private int depositId;
    private String type;

    // Конструктор для создания нового депозита (без ID)
    public Deposit(String type) {
        this.type = type;
    }

    // Конструктор для существующего депозита (с ID)
    public Deposit(int depositId, String type) {
        this.depositId = depositId;
        this.type = type;
    }

    // Геттеры и сеттеры
    public int getDepositId() {
        return depositId;
    }

    public void setDepositId(int depositId) {
        this.depositId = depositId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}