package fr.bafbi.javaproject;

import j2html.tags.specialized.DivTag;

import java.util.ArrayList;
import java.util.List;

import static j2html.TagCreator.*;

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

    public void addRecette(Recette recette) {
        recettes.add(recette);
    }

    public void addBoisson(Boisson boisson) {
        boissons.add(boisson);
    }

    public void removeRecette(Recette recette) {
        recettes.remove(recette);
    }
    public void removeRecette(String recetteId) {
        for (Recette recette1 : recettes) {
            if (recette1.getId().equals(recetteId)) {
                recettes.remove(recette1);
                break;
            }
        }
    }

    public void removeBoisson(Boisson boisson) {
        boissons.remove(boisson);
    }

    public void addRecettePrepares(Recette recette) {
        recettesPrepares.add(recette);
    }

    public void addBoissonPrepares(Boisson boisson) {
        boissonsPrepares.add(boisson);
    }

    public DivTag recetteElement(Recette recette) {
        return div(attrs(".recette"),
                recette.element(recettes.stream().filter(r -> r.getId().equals(recette.getId())).count())
        );
    }

}
