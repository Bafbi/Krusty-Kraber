package fr.bafbi.javaproject;

import j2html.tags.DomContent;
import j2html.tags.specialized.DivTag;
import j2html.tags.specialized.UlTag;
import kotlin.Pair;

import java.util.*;

import static j2html.TagCreator.*;

public class Command {

    private final Map<Recette, Integer> recettesCommandes = new HashMap<>();
    private final Map<Recette, Integer> recettesPrepares = new HashMap<>();
    private final Map<Boisson, Integer> boissonsCommandes = new HashMap<>();
    private final Map<Boisson, Integer> boissonsPrepares = new HashMap<>();

    public Map<Recette, Integer> getRecettesCommandes() {
        return recettesCommandes;
    }

    public Recette getRecette(String recetteId) {
        for (Recette recette : recettesCommandes.keySet()) {
            if (recette.getId().equals(recetteId)) {
                return recette;
            }
        }
        return null;
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

    public void prepareRecette(Recette recette) {
        recettesPrepares.put(recette, recettesPrepares.getOrDefault(recette, 0) + 1);
    }

    public void prepareRecette(String recetteId) {
        for (Recette recette : recettesCommandes.keySet()) {
            if (recette.getId().equals(recetteId)) {
                prepareRecette(recette);
            }
        }
    }

    public void addBoisson(Boisson boisson) {
        boissonsCommandes.put(boisson, boissonsCommandes.getOrDefault(boisson, 0) + 1);
    }

    public void prepareBoisson(Boisson boisson) {
        boissonsPrepares.put(boisson, boissonsPrepares.getOrDefault(boisson, 0) + 1);
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

    public boolean isRecetteReady(Recette recette) {
        return Objects.equals(recettesCommandes.getOrDefault(recette, 0), recettesPrepares.getOrDefault(recette, 0));
    }

    public boolean isBoissonReady(Boisson boisson) {
        return Objects.equals(boissonsCommandes.getOrDefault(boisson, 0), boissonsPrepares.getOrDefault(boisson, 0));
    }

    public boolean isReady() {
        if (recettesCommandes.isEmpty() && boissonsCommandes.isEmpty()) {
            return false;
        }
        for (Recette recette : recettesCommandes.keySet()) {
            if (!isRecetteReady(recette)) {
                return false;
            }
        }
        for (Boisson boisson : boissonsCommandes.keySet()) {
            if (!isBoissonReady(boisson)) {
                return false;
            }
        }
        return true;
    }

}
