package fr.bafbi.javaproject.jobs;

import fr.bafbi.javaproject.Stats;
import j2html.tags.specialized.DivTag;

import static j2html.TagCreator.*;

public abstract class Employe {

    private final String nom;
    private final String prenom;
    private int id;
    private double salaire;
    private int consecutiveWorkDays = 0;

    public Employe(String nom, String prenom, double salaire) {
        this.nom = nom;
        this.prenom = prenom;
        this.salaire = salaire;
    }

    abstract public Stats getStats();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public double getSalaire() {
        return salaire;
    }

    public void incrementConsecutiveWorkDays() {
        consecutiveWorkDays++;
    }

    public void resetConsecutiveWorkDays() {
        consecutiveWorkDays = 0;
    }

    @Override
    public String toString() {
        return "Employe{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", salaire=" + salaire +
                '}';
    }

    public DivTag element() {
        return div(attrs(".employe"),
                h2(nom + " " + prenom),
                span(this.getClass().getSimpleName())

        );
    }
}
