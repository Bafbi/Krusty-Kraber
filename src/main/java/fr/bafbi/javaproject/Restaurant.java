package fr.bafbi.javaproject;

import fr.bafbi.javaproject.jobs.Cuisinier;
import fr.bafbi.javaproject.jobs.Serveur;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    private final Stock stocks = new Stock();
    private final EmployeManager equipe = new EmployeManager();
    private final Cuisine cuisine;
    private final Salle salle;
    private final List<Recette> recettes;
    private final List<Transaction> transaction = new ArrayList<>();
    private boolean clean = true;

    public Restaurant(List<Recette> recettes) {
        this.recettes = recettes;
        this.salle = new Salle(stocks, equipe.getEmployes(Serveur.class));
        this.cuisine = new Cuisine(stocks, equipe.getEmployes(Cuisinier.class), recettes, salle.getCommands());
    }

    public Stock getStocks() {
        return stocks;
    }

    public EmployeManager getEquipe() {
        return equipe;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }


    public static class RestaurantStats extends Stats {
        private final int nbTransactions;
        private final double chiffreAffaire;

        public RestaurantStats(int nbTransactions, double chiffreAffaire) {
            this.nbTransactions = nbTransactions;
            this.chiffreAffaire = chiffreAffaire;
        }

        public int getNbTransactions() {
            return nbTransactions;
        }

        public double getChiffreAffaire() {
            return chiffreAffaire;
        }
    }
}
