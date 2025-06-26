package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Quote {
    private int quoteId;
    private Timestamp timestamp;
    private BigDecimal price;
    private int securityPaperId;

    // Конструктор для создания новой котировки (без ID)
    public Quote(Timestamp timestamp, BigDecimal price, int securityPaperId) {
        this.timestamp = timestamp;
        this.price = price;
        this.securityPaperId = securityPaperId;
    }

    // Конструктор для существующей котировки (с ID)
    public Quote(int quoteId, Timestamp timestamp, BigDecimal price, int securityPaperId) {
        this.quoteId = quoteId;
        this.timestamp = timestamp;
        this.price = price;
        this.securityPaperId = securityPaperId;
    }

    // Геттеры и сеттеры
    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getSecurityPaperId() {
        return securityPaperId;
    }

    public void setSecurityPaperId(int securityPaperId) {
        this.securityPaperId = securityPaperId;
    }

    @Override
    public String toString() {
        return timestamp + " - " + price + " (SecurityPaperID: " + securityPaperId + ")";
    }
}