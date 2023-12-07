package fr.bafbi.javaproject.application;

import fr.bafbi.javaproject.Command;
import fr.bafbi.javaproject.Restaurant;
import fr.bafbi.javaproject.jobs.Serveur;
import io.javalin.Javalin;
import j2html.tags.specialized.DivTag;

import static j2html.TagCreator.*;

public class ServeurPage {

    public static void setup(Javalin app, Restaurant restaurant) {

        app.get("/serveur", ctx -> {
            var content = html(
                    Application.createHeadElement(),
                    body(attrs(".bg-background"),
                            Application.HeaderElement(),
                            h1(attrs(".text-lg "), "Serveur"),
                            ul(attrs(".grid grid-cols-2 gap-4"),
                                    each(restaurant.getEmployeManager().getEmployes(Serveur.class), serveur -> li(attrs(".serveur"),
                                            a(attrs(".text-tertiary"), serveur.getNom()).withHref("/serveur/" + serveur.getId())
                                    ))
                            )
                    )

            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);

        });

        app.get("/serveur/{serveurId}", ctx -> {
            var serveurId = Integer.parseInt(ctx.pathParam("serveurId"));


            var content = html(
                    Application.createHeadElement(),
                    body(attrs(".bg-background"),
                            Application.HeaderElement(),
                            h1("Serveur " + serveurId),
                            // new transaction form
                            form(attrs("#new-transaction-form"),
                                    label("Nouvelle transaction"),
                                    input().withName("table_id").withPlaceholder("Table").isRequired(),
                                    input().withName("client_number").withPlaceholder("Nombre de clients").isRequired(),
                                    button("Créer").withType("submit")
                            ).attr("hx-post", "/api/serveur/" + serveurId + "/transaction")
                                    .attr("hx-target", "#transactions")
                                    .attr("hx-swap", "beforeend"),
                            // transactions list
                            ul(attrs("#transactions"),
                                    each(restaurant.getTransactionManager().getTransactions(serveurId), transaction -> li(attrs(".transaction"),
                                            span("Table " + transaction.tableId() + " - " + transaction.getClientNumber() + " clients"),
                                            a(attrs(".text-tertiary"), "Commander").withHref("/serveur/" + serveurId + "/" + transaction.getId())
                                    ))
                            )
                    )

            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);

        });

        app.get("/serveur/{serveurId}/{transactionId}", ctx -> {
            var serveurId = Integer.parseInt(ctx.pathParam("serveurId"));
            var transactionId = Integer.parseInt(ctx.pathParam("transactionId"));
            var transaction = restaurant.getTransactionManager().getTransaction(transactionId);
            var content = html(
                    Application.createHeadElement(),
                    body(attrs(".bg-background"),
                            Application.HeaderElement(),
                            h1("Serveur " + serveurId),
                            h2("Transaction " + transactionId),
                            ul(attrs(".grid grid-cols-2 gap-4"),
                                    each(restaurant.getRecettes(), recette -> li(attrs(".recette"),
                                            button(attrs(".text-tertiary"), recette.getName())
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

        app.post("/api/serveur/{serveurId}/transaction", ctx -> {
            var serveurId = Integer.parseInt(ctx.pathParam("serveurId"));
            var tableId = Integer.parseInt(ctx.formParam("table_id"));
            var clientNumber = Integer.parseInt(ctx.formParam("client_number"));
            var transaction = restaurant.getTransactionManager().createTransaction(tableId, clientNumber, serveurId);
            Restaurant.getLogger().info("New transaction for serveur {} : table {} - {} clients, {}", serveurId, tableId, clientNumber, transaction);
            ctx.html(li(attrs(".transaction"),
                    span("Table " + transaction.tableId() + " - " + transaction.getClientNumber() + " clients")
            ).render());
        });

        app.put("/api/serveur/{serveurId}/{transactionId}/command", ctx -> {
            var serveurId = Integer.parseInt(ctx.pathParam("serveurId"));
            var transactionId = Integer.parseInt(ctx.pathParam("transactionId"));
            var recetteName = ctx.queryParam("recette_id");
            var recette = restaurant.getRecette(recetteName);
            var transaction = restaurant.getTransactionManager().getTransaction(transactionId);
            transaction.getCommand().addRecette(recette);
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

    private static DivTag commandElement(Command command, int serveurId, int transactionId) {
        return div(attrs("#command"),
                ul(
                        // regrouper les recettes par nom et afficher le nom de la recette et le nombre de fois qu'elle est commandée
                        each(
                                command.getRecettesCommandes().keySet(),
                                recette -> li(
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
