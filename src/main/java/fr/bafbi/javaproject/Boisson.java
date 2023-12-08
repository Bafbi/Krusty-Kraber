package fr.bafbi.javaproject;

import j2html.tags.DomContent;

import static j2html.TagCreator.*;
import static j2html.TagCreator.span;

public enum Boisson {
    LIMONADE(4),
    CIDRE(5),
    BIERE(5),
    JUS_FRUIT(1),
    EAU_PLATE(0),
    ;

    private final double price;

    Boisson(double price) {
        this.price = price;
    }

    public double getPrice() {
        return this.price;
    }

    public DomContent element(int quantity) {
        return div(
                h3(name()),
                span("Prix: " + getPrice()),
                span(rawHtml("&#8364;")),
                quantity > 1 ? span("Quantité: " + quantity): null
        );
    }
}
