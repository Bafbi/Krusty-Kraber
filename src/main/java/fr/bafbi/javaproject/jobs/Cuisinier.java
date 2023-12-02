package fr.bafbi.javaproject.jobs;

import fr.bafbi.javaproject.Stats;

public final class Cuisinier extends Employe {

    private final CuisinierStats stats = new CuisinierStats();

    public Cuisinier(String nom, String prenom, double salaire) {
        super(nom, prenom, salaire);
    }

    @Override
    public Stats getStats() {
        return stats;
    }

    public static class CuisinierStats extends Stats {
        private int nbPlats = 0;

        public void incrementPlats() {
            nbPlats++;
        }


    }
}
