package fr.bafbi.javaproject;

import j2html.tags.DomContent;

import java.io.Serial;
import java.io.Serializable;

import static j2html.TagCreator.*;

public class Facture implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Transaction transaction;

    public Facture(Transaction transaction) {
        this.transaction = transaction;
    }

        public Transaction getTransaction() {
            return transaction;
        }

        public DomContent element() {
            return div(attrs(".flex flex-col gap-2 bg-secondary-container p-2"),
                    h3("Facture"),
                    div(attrs(".grid grid-cols-2 gap-1"),
                    span("Table: " + transaction.tableId()),
                    span("Client: " + transaction.getClientNumber()),
                    span("Serveur: " + transaction.getServeurId()),
                    span("Montant total: " + transaction.getCommand().getTotalPrice() + "€")),
                    //recus
                    ul(
                            each(transaction.getRecus(), recu -> li("Recu: " + recu + "€"))
                    )
            );
        }

}
