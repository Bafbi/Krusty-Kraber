package fr.bafbi.javaproject.jobs;

import fr.bafbi.javaproject.Stats;

public final class Manager extends Employe {
    public Manager(String nom, String prenom, double salaire) {
        super(nom, prenom, salaire);
    }

    @Override
    public Stats getStats() {
        return null;
    }
}
