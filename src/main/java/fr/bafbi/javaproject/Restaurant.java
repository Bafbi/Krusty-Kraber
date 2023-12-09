package fr.bafbi.javaproject;

import fr.bafbi.javaproject.jobs.Cuisinier;
import fr.bafbi.javaproject.jobs.Serveur;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

public class Restaurant {

    private final Stock stocks;
    private final EmployeManager employeManager = new EmployeManager();
    private final List<Recette> recettes;
    private final TransactionManager transactionManager = new TransactionManager();



    private RestaurantState state = RestaurantState.CLOSE;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Restaurant.class);

    public Restaurant(List<Recette> recettes) {
        this.recettes = recettes;
        var defaultStocks = Arrays.stream(Ingredient.values()).collect(java.util.stream.Collectors.toMap(ingredient -> ingredient, ingredient -> 100));
        this.stocks = new Stock(defaultStocks);
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

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }


    public RestaurantState getState() {
        return state;
    }

    public void setState(RestaurantState state) {
        this.state = state;
    }
    public static Logger getLogger() {
        return logger;
    }

    public Recette getRecette(String recetteId) {
        for (Recette recette : recettes) {
            if (recette.getId().equals(recetteId)) {
                return recette;
            }
        }
        return null;
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
