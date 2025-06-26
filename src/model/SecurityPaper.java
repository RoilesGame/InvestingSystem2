package model;

public class SecurityPaper {
    private int securityPaperId;
    private String type;

    // Конструктор для создания новой ценной бумаги (без ID)
    public SecurityPaper(String type) {
        this.type = type;
    }

    // Конструктор для существующей ценной бумаги (с ID)
    public SecurityPaper(int securityPaperId, String type) {
        this.securityPaperId = securityPaperId;
        this.type = type;
    }

    // Геттеры и сеттеры
    public int getSecurityPaperId() {
        return securityPaperId;
    }

    public void setSecurityPaperId(int securityPaperId) {
        this.securityPaperId = securityPaperId;
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