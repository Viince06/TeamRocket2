package model;

public class Exchange {

    private boolean left;
    private Trade tradeToBeDone;

    public Exchange() {}

    public Exchange(boolean left, Trade tradeToBeDone) {
        this.left = left;
        this.tradeToBeDone = tradeToBeDone;
    }

    public boolean isLeft() {
        return left;
    }

    public Trade getTradeToBeDone() {
        return tradeToBeDone;
    }
}
