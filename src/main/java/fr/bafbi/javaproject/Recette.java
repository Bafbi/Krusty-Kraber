package fr.bafbi.javaproject;

import java.util.List;
import java.util.stream.Stream;

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
}
