package fr.bafbi.javaproject;

import fr.bafbi.javaproject.jobs.Serveur;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Salle {

    private final Stock stocks;
    private final Set<Serveur> serveurs;
    private final List<Transaction> transactions = new ArrayList<>();

    public Salle(Stock stocks, Set<Serveur> serveurs) {
        this.stocks = stocks;
        this.serveurs = serveurs;
    }

}
