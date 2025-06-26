package model;

import java.math.BigDecimal;

public class SecurityPaper {
    private int securityPaperId;
    private String type;
    private BigDecimal quote;

    public SecurityPaper(String type, BigDecimal quote) {
        this.type = type;
        this.quote = quote;
    }

    public SecurityPaper(int securityPaperId, String type, BigDecimal quote) {
        this.securityPaperId = securityPaperId;
        this.type = type;
        this.quote = quote;
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

    public BigDecimal getQuote() {
        return quote;
    }

    public void setQuote(BigDecimal quote) {
        this.quote = quote;
    }

    @Override
    public String toString() {
        return type + " (Quote: " + quote + ")";
    }
}