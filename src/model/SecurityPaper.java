package model;

public class SecurityPaper {
    private int securityPaperId;
    private String type;

    public SecurityPaper(String type) {
        this.type = type;
    }

    public SecurityPaper(int securityPaperId, String type) {
        this.securityPaperId = securityPaperId;
        this.type = type;
    }

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