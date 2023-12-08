package fr.bafbi.javaproject.application;

import fr.bafbi.javaproject.Boisson;
import fr.bafbi.javaproject.Command;
import fr.bafbi.javaproject.Recette;
import fr.bafbi.javaproject.Restaurant;
import fr.bafbi.javaproject.jobs.Barman;
import io.javalin.Javalin;
import j2html.tags.specialized.DivTag;

import java.util.Optional;

import static j2html.TagCreator.*;

public class BarPage {

    public static void setup(Javalin app, Restaurant restaurant) {

        // mosty ressemble to CuisinePage
        app.get("/bar", ctx -> {
            var content = html(
                    Application.createHeadElement(),
                    body(attrs(".bg-background"),
                            Application.HeaderElement(),
                            h1("Bar"),
                            ul(attrs(".grid grid-cols-2 gap-4"),
                                    each(restaurant.getEmployeManager().getEmployes(Barman.class), barman -> li(attrs(".barman"),
                                            a(attrs(".text-tertiary"), barman.getNom()).withHref("/bar/" + barman.getId())
                                    ))
                            )
                    )

            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);

        });

        app.get("bar/{barmanId}", ctx -> {
            var barmanId = Integer.parseInt(ctx.pathParam("barmanId"));

            var content = html(
                    Application.createHeadElement(),
                    body(attrs(".bg-background"),
                            Application.HeaderElement(),
                            h1("Barman " + barmanId),
                            // show commands
                            ul(attrs("#commands"),
                                    each(restaurant.getTransactionManager().getCommandsAndId(), command -> li(attrs(".command"),
                                            ul(attrs(".boissons"),
                                                    each(command.component1().getBoissons().keySet(), boisson -> li(attrs(".boisson"),
                                                            boissonElement(command.component1(), boisson, command.component2())
                                                    ))
                                            )
                                    ))
                            )
                    )
            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);
        });

        app.patch("/api/restaurant/{transactionId}/boisson", ctx -> {
            var transactionId = Integer.parseInt(ctx.pathParam("transactionId"));
            var boisson = Boisson.valueOf(ctx.queryParam("boisson"));
            var command = restaurant.getTransactionManager().getTransaction(transactionId).getCommand();
            command.prepareBoisson(boisson);
            ctx.html(boissonElement(command, boisson, transactionId).render());
        });

    }

    private static DivTag boissonElement(Command command, Boisson boisson, int transactionId) {
        return div(attrs(".boisson"),
                span(boisson.name()),
                !command.isBoissonReady(boisson) ? span(" (x" + Optional.ofNullable(command.getBoissons().get(boisson).component2()).orElse(0) + "/x" + command.getBoissons().get(boisson).component1() + ")") : span("Ready (x" + command.getBoissons().get(boisson).component1() + ")"),
                !command.isBoissonReady(boisson) ? button("x1 Prêt")
                        .attr("hx-patch", "/api/restaurant/" + transactionId + "/boisson?boisson=" + boisson.name())
                        .attr("hx-params", "boisson")
                        .attr("hx-target", "closest .boisson")
                        .attr("hx-swap", "outerHTML") : null
        );
    }

}
