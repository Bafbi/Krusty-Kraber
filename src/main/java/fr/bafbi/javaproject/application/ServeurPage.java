package fr.bafbi.javaproject.application;

import fr.bafbi.javaproject.Restaurant;
import fr.bafbi.javaproject.Transaction;
import fr.bafbi.javaproject.jobs.Serveur;
import io.javalin.Javalin;

import static j2html.TagCreator.*;

public class ServeurPage {

    public static void setup(Javalin app, Restaurant restaurant) {

        app.get("/serveur", ctx -> {
            var content = html(
                    Application.createHeadElement(),
                    body(attrs(".bg-background"),
                            h1(attrs(".text-lg "), "Serveur"),
                            ul(attrs(".grid grid-cols-2 gap-4"),
                                    each(restaurant.getEquipe().getEmployes(Serveur.class), serveur -> li(attrs(".serveur"),
                                            a(attrs(".text-tertiary"), serveur.getNom()).withHref("/serveur/" + serveur.getId())
                                    ))
                            )
                    )

            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);

        });

        app.get("/serveur/{serveurId}/{transactionId}", ctx -> {
            var serveurId = Integer.parseInt(ctx.pathParam("serveurId"));

            //log transactions
            Restaurant.getLogger().info("Transactions for serveur {} : {}", serveurId, restaurant.getTransaction());


            var content = html(
                    Application.createHeadElement(),
                    body(attrs(".bg-background"),
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
                                    each(restaurant.getTransaction(serveurId), transaction -> li(attrs(".transaction"),
                                            span("Table " + transaction.tableId() + " - " + transaction.getClientNumber() + " clients")
                                    ))
                            )
                    )

            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);

        });

        app.post("/api/serveur/{serveurId}/transaction", ctx -> {
            var serveurId = Integer.parseInt(ctx.pathParam("serveurId"));
            var tableId = Integer.parseInt(ctx.formParam("table_id"));
            var clientNumber = Integer.parseInt(ctx.formParam("client_number"));
            var transaction = new Transaction(tableId, clientNumber, serveurId);
            restaurant.getTransaction().add(transaction);
            Restaurant.getLogger().info("New transaction for serveur {} : table {} - {} clients, {}", serveurId, tableId, clientNumber, transaction);
            ctx.html(li(attrs(".transaction"),
                    span("Table " + transaction.tableId() + " - " + transaction.getClientNumber() + " clients")
            ).render());
        });

    }

}
