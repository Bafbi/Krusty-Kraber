package fr.bafbi.javaproject.application;

import fr.bafbi.javaproject.Ingredient;
import fr.bafbi.javaproject.Restaurant;
import fr.bafbi.javaproject.RestaurantState;
import fr.bafbi.javaproject.Stock;
import fr.bafbi.javaproject.jobs.*;
import io.javalin.Javalin;
import j2html.tags.DomContent;

import java.util.Arrays;

import static j2html.TagCreator.*;

public class ManagerPage {

//    public UlTag stocksElement(Stock stock) {
//        return

    public static void setup(Javalin app, Restaurant restaurant) {

        var equipeComposant = new EquipeComposant(app, restaurant.getEmployeManager());

        app.get("/manager", ctx -> {
            var content = html(
                    Application.createHeadElement(),
                    body(attrs(".bg-background"),
                            Application.HeaderElement(),
                            h1("Manager"),
                            div(attrs(".grid grid-cols-4 gap-5"),
                                    div(attrs(".intradiv col-span-3"),
                                            h2(attrs(".font-bold .text-center"), "Stocks"),
                                            stocksElement(restaurant.getStocks())),
                                    div(attrs(".intradiv text-center"),
                                            div(
                                                    h2(attrs(".font-bold .text-center"), "Etat du restaurant"),
                                                    stateElement(restaurant.getState())
                                            ),
                                            div(
                                                    h2(attrs(".font-bold .text-center"), "Refaire les Stocks"),
                                                    button("Acheter")
                                                            .attr("hx-post", "/api/stock/refill")
                                                            .attr("hx-swap", "outerHTML")
                                                            .attr("hx-target", "#stocks"))),
                                    div(attrs(".intradiv col-span-4"),
                                            h2(attrs(".font-bold .text-center"), "Gestion des equipes"),
                                            equipeComposant.element(),
                                            // new employees
                                            form(
                                                    input().withType("text").withName("nom").withPlaceholder("Nom"),
                                                    input().withType("text").withName("prenom").withPlaceholder("Prenom"),
                                                    input().withType("number").withName("salaire").withPlaceholder("Salaire"),
                                                    select(attrs(".block .w-full .mt-1 .rounded-md .shadow-sm .border-gray-300 .bg-white .focus:border-primary .focus:ring .focus:ring-primary .focus:ring-opacity-50"),
                                                            option("Serveur").withValue("Serveur"),
                                                            option("Cuisinier").withValue("Cuisinier"),
                                                            option("Barman").withValue("Barman"),
                                                            option("Manager").withValue("Manager")
                                                    ).withName("type"),
                                                    button("Recruter"))
                                                    .attr("hx-post", "/api/employe")
                                                    .attr("hx-swap", "outerHTML")
                                                    .attr("hx-target", "#equipe")
                                    ),


                                    // factures
                                    div(attrs(".intradiv col-span-4"),
                                            h2(attrs(".font-bold .text-center"), "Factures"),
                                            div(attrs(".grid grid-cols-2 gap-5"),
                                                    each(restaurant.getFactures().keySet(), date -> div(
                                                            h3(date.toLocaleString()),
                                                            ul(
                                                                    each(restaurant.getFactures().get(date), facture -> li(
                                                                                    facture.element()
                                                                            )
                                                                    )
                                                            )
                                                    ))
                                            )
                                    )

                            ))

            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);

        });

        app.post("/api/employe", ctx -> {
            var nom = ctx.formParam("nom");
            var prenom = ctx.formParam("prenom");
            var salaire = Integer.parseInt(ctx.formParam("salaire"));
            var type = ctx.formParam("type");
            Class<? extends Employe> employeClass = switch (type) {
                case "Serveur" -> Serveur.class;
                case "Cuisinier" -> Cuisinier.class;
                case "Barman" -> Barman.class;
                case "Manager" -> Manager.class;
                default -> null;
            };
            restaurant.getEmployeManager().recruter(nom, prenom, salaire, employeClass);
            ctx.html(equipeComposant.element().render());
        });

        app.patch("/api/restaurant/toggle", ctx -> {
            int newState = restaurant.getState().ordinal() + 1;
            restaurant.setState(RestaurantState.values()[newState % 3]);
            ctx.html(stateElement(restaurant.getState()).render()
            );
        });

        app.put("/api/stock", ctx -> {
            var ingredient = Ingredient.valueOf(ctx.queryParam("ingredient"));
            restaurant.getStocks().addStock(ingredient, 1);
            ctx.html(restaurant.getStocks().element(ingredient).render());
        });

        app.delete("/api/stock", ctx -> {
            var ingredient = Ingredient.valueOf(ctx.queryParam("ingredient"));
            restaurant.getStocks().takeStock(ingredient, 1);
            ctx.html(restaurant.getStocks().element(ingredient).render());
        });

        app.post("/api/stock/refill", ctx -> {
            var diff = restaurant.getStocks().refillStocks();
            ctx.html(stocksElement(restaurant.getStocks()).render());
        });
    }

    private static DomContent stateElement(RestaurantState state) {
        return
                button(attrs(".bg-primary p-2"), state.toString())
                        .attr("hx-patch", "/api/restaurant/toggle")
                        .attr("hx-swap", "outerHTML");
    }

    private static DomContent stocksElement(Stock stock) {
        return div(attrs("#stocks"),
                ul(attrs(".grid grid-cols-2 gap-4 p-5"),
                        each(Arrays.stream(Ingredient.values()).toList(), ingredient -> li(attrs(".bg-surface-variant px-5 py-2 flex flex-row gap-5 align-center justify-between rounded-md"),
                                        stock.element(ingredient),
                                        div(attrs(".flex flex-row gap-5"),
                                                button(attrs(".bg-primary"), span("+1"))
                                                        .attr("hx-put", "/api/stock?ingredient=" + ingredient.name())
                                                        .attr("hx-swap", "outerHTML")
                                                        .attr("hx-target", "previous #stock-" + ingredient.name()),
                                                button(attrs(".bg-primary"), span("-1"))
                                                        .attr("hx-delete", "/api/stock?ingredient=" + ingredient.name())
                                                        .attr("hx-swap", "outerHTML")
                                                        .attr("hx-target", "previous #stock-" + ingredient.name())
                                        )
                                )
                        )
                ),
                stock.getListeCourse() != null ? div(attrs(".bg-surface-variant px-5 py-2 flex flex-row gap-5 align-center justify-between rounded-md"),
                        h3("Liste de course"),
                        ul(
                                each(stock.getListeCourse().entrySet(), entry -> li(entry.getKey().name() + " : " + entry.getValue()))
                        )
                ) : div()
        );
    }

}
