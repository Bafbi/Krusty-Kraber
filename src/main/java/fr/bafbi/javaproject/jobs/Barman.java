package fr.bafbi.javaproject.jobs;

import fr.bafbi.javaproject.Stats;

public final class Barman extends Employe {

    private int nbCocktails;

    public Barman(String nom, String prenom, double salaire) {
        super(nom, prenom, salaire);
    }

    @Override
    public Stats getStats() {
        return new BarmanStat(nbCocktails);
    }


    public static class BarmanStat extends Stats {
        private int nbCocktails;

        public BarmanStat(int nbCocktails) {
            this.nbCocktails = nbCocktails;
        }

        public int getNbCocktails() {
            return nbCocktails;
        }

        public void setNbCocktails(int nbCocktails) {
            this.nbCocktails = nbCocktails;
        }
    }
}
