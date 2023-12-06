package fr.bafbi.javaproject.jobs;

import fr.bafbi.javaproject.Stats;

public final class Manager extends Employe {
    public Manager(String nom, String prenom, double salaire, int id) {
        super(nom, prenom, salaire, id);
    }

    @Override
    public Stats getStats() {
        return null;
    }
}
