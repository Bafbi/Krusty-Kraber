package fr.bafbi.javaproject;

import java.io.Serializable;

public class Facture implements Serializable {
    private final Transaction transaction;

    public Facture(Transaction transaction) {
        this.transaction = transaction;
    }

        public Transaction getTransaction() {
            return transaction;
        }


}
