package fr.bafbi.javaproject.application;

import fr.bafbi.javaproject.*;
import fr.bafbi.javaproject.jobs.Serveur;
import io.javalin.Javalin;
import j2html.tags.specialized.DivTag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static j2html.TagCreator.*;

public class ServeurPage {

    public static void setup(Javalin app, Restaurant restaurant) {

        //page recap de l'ensemble des serveurs
        app.get("/serveur", ctx -> {
            var content = html(
                    Application.createHeadElement(),
                    body(
                            Application.HeaderElement(),
                            h1("Serveurs"),
                            div(attrs(".grid grid-cols-2 gap-4"),
                                    each(restaurant.getEmployeManager().getEmployes(Serveur.class), serveur -> li(attrs(".serveur"),
                                            a(serveur.getNom()).withHref("/serveur/" + serveur.getId())
                                    ))
                            )
                    )
            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);
        });

        //page recap des transactions en cours du serveur X
        app.get("/serveur/{serveurId}", ctx -> {
            var serveurId = Integer.parseInt(ctx.pathParam("serveurId"));

            var content = html(
                    Application.createHeadElement(),
                    body(
                            Application.HeaderElement(),
                            h1("Serveur " + serveurId+ " - "+restaurant.getEmployeManager().getEmploye(serveurId).getNom()),
                            div(attrs(".grid grid-rows-2"),
                                // new transaction form
                                form(attrs("#new-transaction-form"),
                                        div(attrs(".flex flex-row gap-2"),
                                                label("Nouvelle transaction : "),
                                                input().withName("table_id").withPlaceholder("Table").isRequired(),
                                                input().withName("client_number").withPlaceholder("Nombre de clients").isRequired(),
                                                button("Ajouter").withType("submit")
                                        ))
                                        .attr("hx-post", "/api/serveur/" + serveurId + "/transaction")
                                        .attr("hx-target", "#transactions")
                                        .attr("hx-swap", "beforeend"),
                                // transactions list
                                    h2("Transactions en cours : "),
                                    div(attrs("#command"),
                                            div(attrs("#transactions .grid grid-cols-5 gap-2"),
                                                    each(restaurant.getTransactionManager().getTransactions(serveurId),
                                                            transaction -> transactionElement(transaction,serveurId))
                                            )
                                    )
                            )

                    )

            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);

        });

        //page permettant de commander des plats
        app.get("/serveur/{serveurId}/{transactionId}", ctx -> {
            var serveurId = Integer.parseInt(ctx.pathParam("serveurId"));
            var transactionId = Integer.parseInt(ctx.pathParam("transactionId"));
            var transaction = restaurant.getTransactionManager().getTransaction(transactionId);


//            int nbRecettes = restaurant.getRecettes().size();
            String transaction_state = transaction.getCommand().isReady()?"Prête":"En préparation";
            var content = html(
                    Application.createHeadElement(),
                    body(attrs(".bg-background"),
                            Application.HeaderElement(),
                            h1("Serveur " + serveurId+ " - "+restaurant.getEmployeManager().getEmploye(serveurId).getNom()),
                            h2("Transactions en cours : "),
                            div(attrs("#osef"),
                                    div(attrs("#transactions .grid grid-cols-5 gap-2"),
                                            each(restaurant.getTransactionManager().getTransactions(serveurId),
                                                    tx -> transactionElement(tx,serveurId))
                                    )
                            ),

                            h1("Transaction " + transactionId+" - Etat : "+transaction_state),
                            div(attrs(".grid grid-cols-2 gap-10"),
                                    div(attrs(".intradiv"),
                                    h2("Carte des plats : "),
                                            each(restaurant.getRecettes(),
                                                    recette -> div(
                                                            commandRecetteElement(restaurant,recette, serveurId, transactionId, transaction.getCommand())
                                                    )
                                            )
                                    ),
                                    div(attrs(".intradiv"),
                                        h2("Carte des Boissons : "),
                                        each(
                                                Arrays.stream(Boisson.values()).toList(), boisson -> div(attrs(".flex flex-row w-full gap-10 justify-between"),
                                                        div(attrs(".flex flex-row"),
                                                                button(attrs("."), boisson.name())
                                                                        .attr("hx-put", "/api/transaction/" + transactionId + "/boisson?boisson_id=" + boisson.name() + "&serveur_id=" + serveurId)
                                                                        .attr("hx-target", "#command-" + boisson.name())
                                                                        .attr("hx-swap", "outerHTML"),
                                                                div(attrs(".flex flex-row"),
                                                                        span("Prix: " + boisson.getPrice() + "€")
                                                                )),
                                                        commandBoissonElement(boisson, serveurId, transactionId, transaction.getCommand())
                                                ))
                                        )
                                    )


                            )
                    );

            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);

        });

        // route ajoutant une tx (tableId+nbDeGens) aux tx en cours du serveur
        app.post("/api/serveur/{serveurId}/transaction", ctx -> {
            var serveurId = Integer.parseInt(ctx.pathParam("serveurId"));
            var tableId = Integer.parseInt(ctx.formParam("table_id"));
            var clientNumber = Integer.parseInt(ctx.formParam("client_number"));
            var transaction = restaurant.getTransactionManager().createTransaction(tableId, clientNumber, serveurId);
            Restaurant.getLogger().info("New transaction for serveur {} : table {} - {} clients, {}", serveurId, tableId, clientNumber, transaction);
            ctx.html(
                    transactionElement(transaction,serveurId).render()
            );
        });

        app.put("/api/serveur/{serveurId}/{transactionId}/command", ctx -> {
            var serveurId = Integer.parseInt(ctx.pathParam("serveurId"));
            var transactionId = Integer.parseInt(ctx.pathParam("transactionId"));
            var recetteName = ctx.queryParam("recette_id");
            var recette = restaurant.getRecette(recetteName);
            var transaction = restaurant.getTransactionManager().getTransaction(transactionId);
            transaction.getCommand().addRecette(recette);
            for (Ingredient ingredient: recette.getIngredients().toList()) {
                restaurant.getStocks().takeStock(ingredient,1);
            }
            ctx.html(
                commandRecetteElement(restaurant,recette, serveurId, transactionId, transaction.getCommand()).render());
        });

        app.delete("/api/serveur/{serveurId}/{transactionId}/command", ctx -> {
            var serveurId = Integer.parseInt(ctx.pathParam("serveurId"));
            var transactionId = Integer.parseInt(ctx.pathParam("transactionId"));
            var recetteName = ctx.queryParam("recette_id");
            var recette = restaurant.getRecette(recetteName);
            var transaction = restaurant.getTransactionManager().getTransaction(transactionId);
            transaction.getCommand().removeRecette(recette);
            ctx.html(commandRecetteElement(restaurant,recette, serveurId, transactionId, transaction.getCommand()).render());

        });

        app.put("/api/transaction/{transactionId}/boisson", ctx -> {
            var transactionId = Integer.parseInt(ctx.pathParam("transactionId"));
            var boissonName = ctx.queryParam("boisson_id");
            var serveurId = Integer.parseInt(ctx.queryParam("serveur_id"));
            var boisson = Boisson.valueOf(boissonName);
            var transaction = restaurant.getTransactionManager().getTransaction(transactionId);
            transaction.getCommand().addBoisson(boisson);
            ctx.html(commandBoissonElement(boisson, serveurId, transactionId, transaction.getCommand()).render());

        });

        app.delete("/api/transaction/{transactionId}/boisson", ctx -> {
            var transactionId = Integer.parseInt(ctx.pathParam("transactionId"));
            var boissonName = ctx.queryParam("boisson_id");
            var boisson = Boisson.valueOf(boissonName);
            var transaction = restaurant.getTransactionManager().getTransaction(transactionId);
            transaction.getCommand().removeBoisson(boisson);
            ctx.html(commandBoissonElement(boisson, 0, transactionId, transaction.getCommand()).render());

        });

    }
    private static DivTag transactionElement(Transaction transaction, int serveurId) {
        String color_on_state = transaction.getCommand().isReady()?"text-success":"text-on-error-container";

        return  div(attrs(".transaction intradiv2"),
                            h3(attrs("."+color_on_state), "Table " + transaction.tableId() + " - " + transaction.getClientNumber() + " clients - Tx "+transaction.getId()),
                            div(attrs(".grid grid-cols-2 gap-2"),
                                    a("Commander").withHref("/serveur/" + serveurId + "/" + transaction.getId()),
                                    a("Terminer").withHref("/serveur/" + serveurId + "/" + transaction.getId()))
        );
    }

    private static DivTag commandRecetteElement(Restaurant restaurant, Recette recette, int serveurId, int transactionId, Command command) {
        Boolean isStock = true;
        for(Ingredient ingredient:recette.getIngredients().toList())
        {
            if(restaurant.getStocks().getStock(ingredient)<=0) isStock = false;
        }
        return div(attrs("#command-" + recette.getId()+".flex flex-row w-full gap-10 justify-between"),
                div(attrs(".flex flex-row"),
                        button(attrs("."), recette.getName())
                                .attr("hx-put", "/api/serveur/" + serveurId + "/" + transactionId + "/command?recette_id=" + recette.getId())
                                .attr("hx-target", "#command-" + recette.getId())
                                .attr("hx-swap", "outerHTML")
                                .withCondDisabled(!isStock),
                        div(attrs(".flex flex-row"),
                                span("Prix: " + recette.getPrice() + "€")
                        )
                ),
                div(attrs(" .flex flex-row self-center items-center"),

                        command.getRecettesCommandes().getOrDefault(recette, 0) > 0 ? recette.element(command.getRecettesCommandes().get(recette)) : null,
                        command.getRecettesCommandes().getOrDefault(recette, 0) > 0 ? button("Supprimer")
                                .attr("hx-delete", "/api/serveur/" + serveurId + "/" + transactionId + "/command?recette_id=" + recette.getId() + "&serveur_id=" + serveurId)
                                .attr("hx-target", "#command-" + recette.getId())
                                .attr("hx-swap", "outerHTML") : null
                ));
    }

    private static DivTag commandBoissonElement(Boisson boisson, int serveurId, int transactionId, Command command) {

        return
                div(attrs("#command-" + boisson.name() + " .flex flex-row self-center items-center"),
                        command.getBoissonsCommandes().getOrDefault(boisson, 0) > 0 ? boisson.element(command.getBoissonsCommandes().get(boisson)) : null,
                        command.getBoissonsCommandes().getOrDefault(boisson, 0) > 0 ? button("Supprimer")
                                .attr("hx-delete", "/api/transaction/" + transactionId + "/boisson?boisson_id=" + boisson.name())
                                .attr("hx-target", "#command-" + boisson.name())
                                .attr("hx-swap", "outerHTML") : null
                        );
    }


}
