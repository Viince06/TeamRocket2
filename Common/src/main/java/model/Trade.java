package model;

public class Trade {
    private Resources resource;
    private int quantity;

    public Trade() {}

    public Trade(Resources resource, int quantity) {
        this.resource = resource;
        this.quantity = quantity;
    }

    public Resources getResource() {
        return resource;
    }

    public int getQuantity() {
        return quantity;
    }
}
