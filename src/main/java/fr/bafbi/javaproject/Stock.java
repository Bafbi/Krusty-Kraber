package fr.bafbi.javaproject;

import j2html.tags.DomContent;

import java.util.HashMap;
import java.util.Map;

import static j2html.TagCreator.*;

public class Stock {

    private final Map<Ingredient, Integer> stocks = new HashMap<>();
    private Map<Ingredient, Integer> defaultStocks;

    private Map<Ingredient, Integer> listeCourse;

    public Stock(Map<Ingredient, Integer> defaultStocks) {
        this.defaultStocks = defaultStocks;
        this.stocks.putAll(defaultStocks);
    }

    public void addStock(Ingredient ingredient, int quantity) {
        stocks.put(ingredient, stocks.get(ingredient) + quantity);
    }

    public void takeStock(Ingredient ingredient, int quantity) throws NotSufficientStockException {
        stocks.put(ingredient, stocks.get(ingredient) - quantity);
    }

    public int getStock(Ingredient ingredient) {
        return stocks.get(ingredient);
    }

    public Map<Ingredient, Integer> diffStocks() {
        Map<Ingredient, Integer> diff = new HashMap<>();
        for (Map.Entry<Ingredient, Integer> entry : stocks.entrySet()) {
            diff.put(entry.getKey(), entry.getValue() - defaultStocks.get(entry.getKey()));
        }
        return diff;
    }

    public void setDefaultStocks(Map<Ingredient, Integer> defaultStocks) {
        this.defaultStocks = defaultStocks;
    }

    public void resetStocks() {
        this.stocks.putAll(defaultStocks);
    }

    public Map<Ingredient, Integer> refillStocks() {
        Map<Ingredient, Integer> diff = diffStocks();
        // reset stocks that are below default
        for (Map.Entry<Ingredient, Integer> entry : diff.entrySet()) {
            if (entry.getValue() < 0) {
                stocks.put(entry.getKey(), defaultStocks.get(entry.getKey()));
            }
        }
        listeCourse = diff;
        return diff;
    }

    public Map<Ingredient, Integer> getListeCourse() {
        return listeCourse;
    }

    public DomContent element(Ingredient ingredient) {
        return div(attrs("#stock-" + ingredient.name()),
                h3(ingredient.name()),
                div(attrs(".flex flex-row gap-5"),
                        span("Quantité: "),
                        span("x" + stocks.get(ingredient).toString()),
                        span(attrs(".text-outline text-md"), "x" + defaultStocks.get(ingredient).toString())
                )
        );

    }

    public static class NotSufficientStockException extends Exception {
        public NotSufficientStockException(String message) {
            super(message);
        }
    }

}
