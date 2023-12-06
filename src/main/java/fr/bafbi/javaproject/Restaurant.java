package fr.bafbi.javaproject;

import fr.bafbi.javaproject.jobs.Cuisinier;
import fr.bafbi.javaproject.jobs.Serveur;
import org.slf4j.Logger;

import java.util.List;

public class Restaurant {

    private final Stock stocks = new Stock();
    private final EmployeManager employeManager = new EmployeManager();
    private final Cuisine cuisine;
    private final Salle salle;
    private final List<Recette> recettes;
    private final TransactionManager transactionManager = new TransactionManager();
    private boolean clean = true;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Restaurant.class);

    public Restaurant(List<Recette> recettes) {
        this.recettes = recettes;
        this.salle = new Salle(stocks, employeManager.getEmployes(Serveur.class));
        this.cuisine = new Cuisine(stocks, employeManager.getEmployes(Cuisinier.class), recettes, transactionManager.getCommands());
    }

    public Stock getStocks() {
        return stocks;
    }

    public EmployeManager getEmployeManager() {
        return employeManager;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public Salle getSalle() {
        return salle;
    }

    public List<Recette> getRecettes() {
        return recettes;
    }

    public Recette getRecette(String recetteId) {
        return recettes.stream().filter(r -> r.getId().equals(recetteId)).findFirst().orElse(null);
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public static Logger getLogger() {
        return logger;
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
