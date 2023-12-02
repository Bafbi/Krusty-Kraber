package fr.bafbi.javaproject;

public enum Boisson {
    LIMONADE(4),
    CIDRE(5),
    BIERE(5),
    JUS_FRUIT(1),
    EAU_PLATE(0),
    ;

    Boisson(double price) {
    }

    public double getPrice() {
        return 0;
    }
}
