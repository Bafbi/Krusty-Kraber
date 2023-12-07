package fr.bafbi.javaproject;

import j2html.tags.specialized.DivTag;

import java.util.List;
import java.util.stream.Stream;

import static j2html.TagCreator.*;

public class Recette {
    private final String name;
    private final List<Ingredient> ingredients;
    private final double price;

    public Recette(String name, List<Ingredient> ingredients, double price) {
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
    }

    public Stream<Ingredient> getIngredients() {
        return ingredients.stream();
    }

    public double getPrice() {
        return price;
    }


    @Override
    public String toString() {
        return "Recette{" +
                "name=" + name +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return name.toLowerCase().replace(" ", "_");
    }

    public DivTag element(long quantity) {
        return div(
                h3(name),
                span("Prix: " + price ),
                span(rawHtml("&#8364;")),
                quantity > 1 ? span("Quantité: " + quantity): null
        );
    }
}
