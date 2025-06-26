package model;

public class Company {
    private int companyId;
    private int clientId;
    private Integer depositId;
    private Integer securityPaperId;

    public Company(int clientId, Integer depositId, Integer securityPaperId) {
        this.clientId = clientId;
        this.depositId = depositId;
        this.securityPaperId = securityPaperId;
    }

    public Company(int companyId, int clientId, Integer depositId, Integer securityPaperId) {
        this.companyId = companyId;
        this.clientId = clientId;
        this.depositId = depositId;
        this.securityPaperId = securityPaperId;
    }

    // Геттеры и сеттеры
    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Integer getDepositId() {
        return depositId;
    }

    public void setDepositId(Integer depositId) {
        this.depositId = depositId;
    }

    public Integer getSecurityPaperId() {
        return securityPaperId;
    }

    public void setSecurityPaperId(Integer securityPaperId) {
        this.securityPaperId = securityPaperId;
    }
}