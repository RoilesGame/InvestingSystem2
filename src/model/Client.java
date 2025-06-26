package model;

public class Client {
    private int clientId;
    private String title;
    private String address;
    private String phoneNumber;
    private String typeProperty;

    public Client(String title, String address, String phoneNumber, String typeProperty) {
        this.title = title;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.typeProperty = typeProperty;
    }

    public Client(int clientId, String title, String address, String phoneNumber, String typeProperty) {
        this.clientId = clientId;
        this.title = title;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.typeProperty = typeProperty;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTypeProperty() {
        return typeProperty;
    }

    public void setTypeProperty(String typeProperty) {
        this.typeProperty = typeProperty;
    }
}