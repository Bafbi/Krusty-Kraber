package fr.bafbi.javaproject;

import fr.bafbi.javaproject.application.Application;
import fr.bafbi.javaproject.jobs.Barman;
import fr.bafbi.javaproject.jobs.Cuisinier;
import fr.bafbi.javaproject.jobs.Manager;
import fr.bafbi.javaproject.jobs.Serveur;

import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static void main(String[] args) throws InterruptedException {

        // Create some recipe
        Recette cheeseBurger = new Recette("Burger", List.of(
                Ingredient.PAIN,
                Ingredient.FROMAGE,
                Ingredient.BOEUF,
                Ingredient.PAIN
        ), 1);
        Recette hotDog = new Recette("Hotdog", List.of(
                Ingredient.PAIN,
                Ingredient.SAUCISSE,
                Ingredient.PAIN
        ), 2);
        Recette pizza = new Recette("Pizza", List.of(
                Ingredient.PATE,
                Ingredient.TOMATE,
                Ingredient.FROMAGE,
                Ingredient.CHAMPIGNON,
                Ingredient.OIGNON
        ), 3);
        Recette salade = new Recette("Salade", List.of(
                Ingredient.SALADE,
                Ingredient.TOMATE,
                Ingredient.OIGNON
        ), 4);

        // Create a restaurant
        Restaurant restaurant = new Restaurant(List.of(
                cheeseBurger,
                hotDog,
                pizza,
                salade
        ));

        // Hire some employees
        var equipes = restaurant.getEmployeManager();

        var serveur1 = equipes.recruter("Dupont", "Jean", 1500, Serveur.class);
        var serveur2 = equipes.recruter("Lemarcher", "Patrique", 1300, Serveur.class);
        var serveur3 = equipes.recruter("Brisette", "Stéphanie", 1500, Serveur.class);
        var cuisinier = equipes.recruter("Lefevre", "Marie", 1500, Cuisinier.class);
        var barman = equipes.recruter("Paquet", "Adorlee", 1500, Barman.class);
        var manager = equipes.recruter("Lefebvre", "Jeanne", 2500, Manager.class);

        // Assign some employees to the team
        restaurant.getEmployeManager().getEmployes().forEach(equipes::planifier);

        // Create some transactions
        var transactions = restaurant.getTransactionManager();
        var client1 = transactions.createTransaction(1, 4, serveur1.getId());
        transactions.createTransaction(2, 2, serveur2.getId());
        transactions.createTransaction(3, 1, serveur3.getId());
        transactions.createTransaction(4, 3, serveur1.getId());

        client1.getCommand().addRecette(cheeseBurger);
        client1.getCommand().addRecette(hotDog);
        client1.getCommand().addRecette(hotDog);

        var app = new Application(restaurant);
        app.run();


    }
}