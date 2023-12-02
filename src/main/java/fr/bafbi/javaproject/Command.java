package fr.bafbi.javaproject;

import java.util.ArrayList;
import java.util.List;

public class Command {

    private final List<Recette> recettes;
    private final List<Recette> recettesPrepares = new ArrayList<>();
    private final List<Boisson> boissons;
    private final List<Boisson> boissonsPrepares = new ArrayList<>();

    public Command(List<Recette> recettes, List<Boisson> boissons) {
        this.recettes = recettes;
        this.boissons = boissons;
    }

    public List<Recette> getRecettes() {
        return recettes;
    }

    public List<Recette> getRecettesPrepares() {
        return recettesPrepares;
    }


    public List<Boisson> getBoissons() {
        return boissons;
    }

    public List<Boisson> getBoissonsPrepares() {
        return boissonsPrepares;
    }


}
