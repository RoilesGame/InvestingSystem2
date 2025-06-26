package model;

public class Deal {
    private int dealId;
    private int clientId;
    private int depositId;
    private Integer securityPaperId; // Может быть null

    public Deal(int clientId, int depositId, Integer securityPaperId) {
        this.clientId = clientId;
        this.depositId = depositId;
        this.securityPaperId = securityPaperId;
    }

    public Deal(int dealId, int clientId, int depositId, Integer securityPaperId) {
        this.dealId = dealId;
        this.clientId = clientId;
        this.depositId = depositId;
        this.securityPaperId = securityPaperId;
    }

    // Геттеры и сеттеры
    public int getDealId() {
        return dealId;
    }

    public void setDealId(int dealId) {
        this.dealId = dealId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getDepositId() {
        return depositId;
    }

    public void setDepositId(int depositId) {
        this.depositId = depositId;
    }

    public Integer getSecurityPaperId() {
        return securityPaperId;
    }

    public void setSecurityPaperId(Integer securityPaperId) {
        this.securityPaperId = securityPaperId;
    }
}