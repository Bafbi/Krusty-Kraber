package fr.bafbi.javaproject.jobs;

import fr.bafbi.javaproject.Stats;

public final class Serveur extends Employe {
    public Serveur(String nom, String prenom, double salaire) {
        super(nom, prenom, salaire);
    }

    @Override
    public Stats getStats() {
        return null;
    }
}
