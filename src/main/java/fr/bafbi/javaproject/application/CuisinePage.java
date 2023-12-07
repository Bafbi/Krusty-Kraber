package fr.bafbi.javaproject.application;

import fr.bafbi.javaproject.Cuisine;
import fr.bafbi.javaproject.Restaurant;
import fr.bafbi.javaproject.jobs.Cuisinier;
import io.javalin.Javalin;

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
                                            ul(attrs(".recettes"),
                                                    each(command.component1().getRecettes().keySet(), recette -> li(attrs(".recette"),
                                                            span(recette.getName()),
                                                            span(" (x" + Optional.ofNullable(command.component1().getRecettes().get(recette).component2()).orElse(0) + "/x" + command.component1().getRecettes().get(recette).component1() + ")")
                                                    ))
                                            )
                                    ))
                            )
                    )
            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);
        });

/*        app.post("cuisine/command/{commandId}/ready", ctx -> {
            var commandId = Integer.parseInt(ctx.pathParam("commandId"));
            var cuisinerId = Integer.parseInt(ctx.queryParam("cuisinierId"));
            var command = restaurant.getTransactionManager().getCommand(commandId);
            cuisinier.ready(command);
            ctx.redirect("/cuisine/" + cuisinerId);
        });*/
    }





}
