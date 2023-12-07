package fr.bafbi.javaproject.application;

import fr.bafbi.javaproject.Command;
import fr.bafbi.javaproject.Restaurant;
import fr.bafbi.javaproject.Transaction;
import fr.bafbi.javaproject.jobs.Serveur;
import io.javalin.Javalin;
import j2html.tags.specialized.DivTag;

import static j2html.TagCreator.*;

public class ServeurPage {

    public static void setup(Javalin app, Restaurant restaurant) {

        //page recap de l'ensemble des serveurs
        app.get("/serveur", ctx -> {
            var content = html(
                    Application.createHeadElement(),
                    body(
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
                            h1("Serveur " + serveurId),
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
            var content = html(
                    Application.createHeadElement(),
                    body(attrs(".bg-background"),
                            h1("Serveur " + serveurId),
                            h2("Transaction " + transactionId),
                            div(attrs(".grid grid-cols-2 gap-4"),
                                    each(restaurant.getRecettes(), recette -> div(attrs(".recette"),
                                            button( recette.getName())
                                                    .attr("hx-put", "/api/serveur/" + serveurId + "/" + transactionId + "/command?recette_id=" + recette.getId())
                                                    .attr("hx-target", "#command")
                                                    .attr("hx-swap", "outerHTML")
                                                    .attr("hx-vals", "recette_zzid=" + recette.getId())

                                    ))
                            ),
                            commandElement(transaction.getCommand(), serveurId, transactionId)
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
                    transactionElement(transaction,serveurId).render());
        });

        app.put("/api/serveur/{serveurId}/{transactionId}/command", ctx -> {
            var serveurId = Integer.parseInt(ctx.pathParam("serveurId"));
            var transactionId = Integer.parseInt(ctx.pathParam("transactionId"));
            var recetteName = ctx.queryParam("recette_id");
            var recette = restaurant.getRecette(recetteName);
            var transaction = restaurant.getTransactionManager().getTransaction(transactionId);
            transaction.getCommand().addRecette(recette);
            ctx.res().setContentType("text/plain; charset=utf-8");
            ctx.html(commandElement(transaction.getCommand(), serveurId, transactionId).render());
        });

        app.delete("/api/serveur/{serveurId}/{transactionId}/command", ctx -> {
            var serveurId = Integer.parseInt(ctx.pathParam("serveurId"));
            var transactionId = Integer.parseInt(ctx.pathParam("transactionId"));
            var recetteName = ctx.queryParam("recette_id");
            var recette = restaurant.getRecette(recetteName);
            var transaction = restaurant.getTransactionManager().getTransaction(transactionId);
            transaction.getCommand().removeRecette(recette);
            ctx.html(commandElement(transaction.getCommand(), serveurId, transactionId).render());
        });

    }
    private static DivTag transactionElement(Transaction transaction, int serveurId) {

        return  div(attrs(".transaction intradiv"),
                            h3("Table " + transaction.tableId() + " - " + transaction.getClientNumber() + " clients"),
                            div(attrs(".grid grid-cols-2 gap-2"),
                                    a("Commander").withHref("/serveur/" + serveurId + "/" + transaction.getId()),
                                    a("Terminer").withHref("/serveur/" + serveurId + "/" + transaction.getId()))
                    );
    }
    private static DivTag commandElement(Command command, int serveurId, int transactionId) {
        return div(attrs("#command"),
                div(
                        each(
                                command.getRecettesCommandes().keySet(),
                                recette -> div(
                                        recette.element(command.getRecettesCommandes().get(recette)),
                                        button("Supprimer")
                                                .attr("hx-delete", "/api/serveur/" + serveurId + "/"+ transactionId +"/command?recette_id=" + recette.getId())
                                                .attr("hx-target", "#command")
                                                .attr("hx-swap", "outerHTML")
                                                .withId("command" + recette.getId())
                                )
                        )
                )
        );
    }

}
