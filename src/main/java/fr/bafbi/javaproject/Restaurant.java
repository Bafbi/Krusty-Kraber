package fr.bafbi.javaproject;

import fr.bafbi.javaproject.jobs.Cuisinier;
import fr.bafbi.javaproject.jobs.Serveur;
import org.slf4j.Logger;

import java.io.File;
import java.util.*;

public class Restaurant {

    private final Stock stocks;
    private final EmployeManager employeManager = new EmployeManager();
    private final List<Recette> recettes;
    private final TransactionManager transactionManager = new TransactionManager();
    private final Date date = new Date();
    private final Map<Date, List<Facture>> factures;


    private RestaurantState state = RestaurantState.CLOSE;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Restaurant.class);

    public Restaurant(List<Recette> recettes) {
        this.recettes = recettes;
        var defaultStocks = Arrays.stream(Ingredient.values()).collect(java.util.stream.Collectors.toMap(ingredient -> ingredient, ingredient -> 100));
        this.stocks = new Stock(defaultStocks);
        this.factures = loadFactures();
    }

    /**
     * Will load all the factures from disk (./factures/{date}.ser)
     * @return a map of date and list of factures for that date
     */
    private Map<Date, List<Facture>> loadFactures() {
        Map<Date, List<Facture>> factures = new HashMap<>();

        var facturesDir = new File("./factures");
        if (!facturesDir.exists()) {
            facturesDir.mkdir();
        }
        for (File file : facturesDir.listFiles()) {
            if (file.getName().endsWith(".ser")) {
                var date = new Date(Long.parseLong(file.getName().replace(".ser", "")));
                var facture = Utils.loadObject(file, Facture.class);
                if (factures.containsKey(date)) {
                    factures.get(date).add(facture);
                } else {
                    factures.put(date, new ArrayList<>(Collections.singletonList(facture)));
                }
            }
        }
        return factures;
    }

    public void createFacture(Transaction transaction) {
        var facture = new Facture(transaction);
        if (factures.containsKey(date)) {
            factures.get(date).add(facture);
        } else {
            factures.put(date, new ArrayList<>(Collections.singletonList(facture)));
        }
        Utils.saveObject(new File("./factures/" + date.getTime() + ".ser"), facture);
    }

    public Stock getStocks() {
        return stocks;
    }

    public EmployeManager getEmployeManager() {
        return employeManager;
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

    public Map<Date, List<Facture>> getFactures() {
        return factures;
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
