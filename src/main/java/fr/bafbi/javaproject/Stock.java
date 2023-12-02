package fr.bafbi.javaproject;

import j2html.tags.specialized.LiTag;
import j2html.tags.specialized.UlTag;

import java.util.HashMap;
import java.util.Map;

import static j2html.TagCreator.*;

public class Stock {

    private final Map<Ingredient, Integer> stocks = new HashMap<>();
    private Map<Ingredient, Integer> defaultStocks;

    public void addStock(Ingredient ingredient, int quantity) {
        stocks.put(ingredient, quantity);
    }

    public void takeStock(Ingredient ingredient, int quantity) throws NotSufficientStockException {
        stocks.put(ingredient, stocks.get(ingredient) - quantity);
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

    public LiTag createStockElement(Ingredient ingredient) {
        return li(attrs(".stock"),
                h2(ingredient.name()),
                span(stocks.get(ingredient).toString())
        );

    }

    public UlTag createStocksElement() {
        return ul(attrs(".flex flex-row"), each(stocks.keySet(), this::createStockElement));
    }

    public static class NotSufficientStockException extends Exception {
        public NotSufficientStockException(String message) {
            super(message);
        }
    }

}
