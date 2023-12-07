package fr.bafbi.javaproject;

import j2html.tags.DomContent;
import j2html.tags.specialized.DivTag;
import j2html.tags.specialized.UlTag;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

public class Command {

    private final Map<Recette, Integer> recettesCommandes = new HashMap<>();
    private final Map<Recette, Integer> recettesPrepares = new HashMap<>();
    private final Map<Boisson, Integer> boissonsCommandes = new HashMap<>();
    private final Map<Boisson, Integer> boissonsPrepares = new HashMap<>();

    public Map<Recette, Integer> getRecettesCommandes() {
        return recettesCommandes;
    }

    public Map<Recette, Integer> getRecettesPrepares() {
        return recettesPrepares;
    }

    public Map<Recette, Pair<Integer, Integer>> getRecettes() {
        Map<Recette, Pair<Integer, Integer>> recettes = new HashMap<>();
        for (Recette recette : recettesCommandes.keySet()) {
            recettes.put(recette, new Pair<>(recettesCommandes.get(recette), recettesPrepares.get(recette)));
        }
        return recettes;
    }

    public Map<Boisson, Integer> getBoissonsCommandes() {
        return boissonsCommandes;
    }

    public Map<Boisson, Integer> getBoissonsPrepares() {
        return boissonsPrepares;
    }

    public Map<Boisson, Pair<Integer, Integer>> getBoissons() {
        Map<Boisson, Pair<Integer, Integer>> boissons = new HashMap<>();
        for (Boisson boisson : boissonsCommandes.keySet()) {
            boissons.put(boisson, new Pair<>(boissonsCommandes.get(boisson), boissonsPrepares.get(boisson)));
        }
        return boissons;
    }

    public void addRecette(Recette recette) {
        recettesCommandes.put(recette, recettesCommandes.getOrDefault(recette, 0) + 1);
    }

    public void addBoisson(Boisson boisson) {
        boissonsCommandes.put(boisson, boissonsCommandes.getOrDefault(boisson, 0) + 1);
    }

    public void removeRecette(Recette recette) {
        recettesCommandes.put(recette, recettesCommandes.getOrDefault(recette, 0) - 1);
    }

    public void removeRecette(String recetteId) {
        for (Recette recette : recettesCommandes.keySet()) {
            if (recette.getId().equals(recetteId)) {
                removeRecette(recette);
            }
        }
    }

    public void removeBoisson(Boisson boisson) {
        boissonsCommandes.put(boisson, boissonsCommandes.getOrDefault(boisson, 0) - 1);
    }


}
