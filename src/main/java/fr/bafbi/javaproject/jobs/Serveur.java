package fr.bafbi.javaproject.jobs;

import fr.bafbi.javaproject.Stats;

public final class Serveur extends Employe {
    public Serveur(String nom, String prenom, double salaire, int id) {
        super(nom, prenom, salaire, id);
    }

    @Override
    public Stats getStats() {
        return null;
    }
}
