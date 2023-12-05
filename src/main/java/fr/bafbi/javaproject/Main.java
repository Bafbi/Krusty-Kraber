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

        var stocks = restaurant.getStocks();

        // Add some ingredients to the restaurant's stock
        stocks.addStock(Ingredient.PAIN, 10);
        stocks.addStock(Ingredient.FROMAGE, 10);
        stocks.addStock(Ingredient.BOEUF, 10);
        stocks.addStock(Ingredient.SAUCISSE, 10);
        stocks.addStock(Ingredient.PATE, 10);
        stocks.addStock(Ingredient.TOMATE, 10);
        stocks.addStock(Ingredient.CHAMPIGNON, 10);
        stocks.addStock(Ingredient.OIGNON, 10);
        stocks.addStock(Ingredient.SALADE, 10);

        // Hire some employees
        var serveur1 = new Serveur("Dupont", "Jean", 1500);
        var serveur2 = new Serveur("Lemarcher", "Patrique", 1300);
        var serveur3 = new Serveur("Brisette", "Stéphanie", 1500);
        var cuisinier = new Cuisinier("Lefevre", "Marie", 1500);
        var barman = new Barman("Paquet", "Adorlee", 1500);
        var manager = new Manager("Lefebvre", "Jeanne", 2500);

        var equipes = restaurant.getEquipe();

        equipes.recruter(serveur1);
        equipes.recruter(serveur2);
        equipes.recruter(serveur3);
        equipes.recruter(cuisinier);
        equipes.recruter(barman);
        equipes.recruter(manager);

        // Assign some employees to the team
        restaurant.getEquipe().getEmployes().forEach(equipes::planifier);


        // Create transactions
        Transaction client1 = new Transaction(1, 4, serveur1.getId());
        Transaction client2 = new Transaction(2, 2, serveur2.getId());
        Transaction client3 = new Transaction(3, 1, serveur3.getId());
        Transaction client4 = new Transaction(4, 3, serveur1.getId());

        restaurant.getTransaction().add(client1);
        restaurant.getTransaction().add(client2);
        restaurant.getTransaction().add(client3);
        restaurant.getTransaction().add(client4);

        var app = new Application(restaurant);
        app.run();


    }
}