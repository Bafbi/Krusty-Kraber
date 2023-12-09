package fr.bafbi.javaproject.application;

import fr.bafbi.javaproject.Command;
import fr.bafbi.javaproject.Recette;
import fr.bafbi.javaproject.Restaurant;
import fr.bafbi.javaproject.jobs.Cuisinier;
import io.javalin.Javalin;
import j2html.tags.specialized.DivTag;

import java.util.Optional;

import static j2html.TagCreator.*;

public class CuisinePage {

    public static void setup(Javalin app, Restaurant restaurant) {

        app.get("/cuisine", ctx -> {
            var content = html(
                    Application.createHeadElement(),
                    body(attrs(".bg-background"),
                            Application.HeaderElement(),
                            h1("Cuisine"),
                            ul(attrs(".grid grid-cols-2 gap-4"),
                                    each(restaurant.getEmployeManager().getEmployes(Cuisinier.class), cuisinier -> li(attrs(".cuisinier"),
                                            a(attrs(".text-tertiary"), cuisinier.getNom()).withHref("/cuisine/" + cuisinier.getId())
                                    ))
                            )
                    )

            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);

        });

        app.get("cuisine/{cuisinerId}", ctx -> {
            var cuisinerId = Integer.parseInt(ctx.pathParam("cuisinerId"));

            var content = html(
                    Application.createHeadElement(),
                    body(attrs(".bg-background"),
                            Application.HeaderElement(),
                            h1("Cuisinier " + cuisinerId),
                            // show commands
                            ul(attrs("#commands"),
                                    each(restaurant.getTransactionManager().getCommandsAndId(), command -> li(attrs(".command"),
                                            div(
                                                h2("Commande N°" + command.component2().toString()),
                                                ul(attrs(".recettes"),
                                                    each(command.component1().getRecettes().keySet(), recette -> li(attrs(".recette"),
                                                            recetteElement(command.component1(), recette, command.component2())
                                                    ))
                                            ))
                                    ))
                            )
                    )
            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);
        });

        app.post("api/cuisine/command/{transactionId}/ready", ctx -> {
            var transactionId = Integer.parseInt(ctx.pathParam("transactionId"));
            var recetteId = ctx.queryParam("recetteId");
            var command = restaurant.getTransactionManager().getTransaction(transactionId).getCommand();
            command.prepareRecette(recetteId);
            ctx.html(recetteElement(command, command.getRecette(recetteId), transactionId).render());
        });
    }

    private static DivTag recetteElement(Command command, Recette recette, int transactionId) {
        return div(attrs(".recette"),
                span(recette.getName()),
                !command.isRecetteReady(recette) ? span(" (x" + Optional.ofNullable(command.getRecettes().get(recette).component2()).orElse(0) + "/x" + command.getRecettes().get(recette).component1() + ")") : span("Ready (x" + command.getRecettes().get(recette).component1() + ")"),
                !command.isRecetteReady(recette) ? button("x1 Prêt")
                        .attr("hx-post", "/api/cuisine/command/" + transactionId + "/ready?recetteId=" + recette.getId())
                        .attr("hx-params", "recetteId")
                        .attr("hx-target", "closest .recette")
                        .attr("hx-swap", "outerHTML") : null
        );
    }

/*        app.post("cuisine/command/{commandId}/ready", ctx -> {
            var commandId = Integer.parseInt(ctx.pathParam("commandId"));
            var cuisinerId = Integer.parseInt(ctx.queryParam("cuisinierId"));
            var command = restaurant.getTransactionManager().getCommand(commandId);
            cuisinier.ready(command);
            ctx.redirect("/cuisine/" + cuisinerId);
        });*/
    }






