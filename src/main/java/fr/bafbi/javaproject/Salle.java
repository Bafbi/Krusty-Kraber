package fr.bafbi.javaproject;

import fr.bafbi.javaproject.jobs.Cuisinier;
import fr.bafbi.javaproject.jobs.Serveur;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Salle {

    private final Stock stocks;
    private final EmployeManager employeManager;
    private final List<Transaction> transactions = new ArrayList<>();

    public Salle(Stock stocks, EmployeManager employeManager) {
        this.stocks = stocks;
        this.employeManager = employeManager;
    }

    public Set<Serveur> getServeurs() {
        return employeManager.getEmployes(Serveur.class);
    }

}
