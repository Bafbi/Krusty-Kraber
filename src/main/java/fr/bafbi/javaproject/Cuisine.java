package fr.bafbi.javaproject;

import fr.bafbi.javaproject.jobs.Cuisinier;

import java.util.List;
import java.util.Set;

public class Cuisine {

    private final Stock stocks;
    private final Set<Cuisinier> cuisiniers;
    private final List<Recette> recettes;
    private final List<Command> commands;

    public Cuisine(Stock stocks, Set<Cuisinier> cuisiniers, List<Recette> recettes, List<Command> commands) {
        this.stocks = stocks;
        this.cuisiniers = cuisiniers;
        this.recettes = recettes;
        this.commands = commands;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public Stock getStocks() {
        return stocks;
    }

    public Set<Cuisinier> getCuisiniers() {
        return cuisiniers;
    }

    public List<Recette> getRecettes() {
        return recettes;
    }
}
