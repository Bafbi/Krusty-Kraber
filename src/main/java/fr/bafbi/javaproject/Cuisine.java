package fr.bafbi.javaproject;

import fr.bafbi.javaproject.jobs.Cuisinier;
import j2html.tags.specialized.DivTag;

import java.security.spec.ECPoint;
import java.util.List;
import java.util.Set;

public class Cuisine {

    private final Stock stocks;
    private final EmployeManager employeManager;
    private final List<Recette> recettes;
    private final List<Command> commands;

    public Cuisine(Stock stocks, EmployeManager employeManager, List<Recette> recettes, List<Command> commands) {
        this.stocks = stocks;
        this.employeManager = employeManager;
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
        return employeManager.getEmployes(Cuisinier.class);
    }

    public List<Recette> getRecettes() {
        return recettes;
    }


}
