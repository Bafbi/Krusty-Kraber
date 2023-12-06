package fr.bafbi.javaproject.application;

import fr.bafbi.javaproject.Restaurant;
import fr.bafbi.javaproject.RestaurantState;
import fr.bafbi.javaproject.Stock;
import io.javalin.Javalin;
import j2html.tags.specialized.STag;
import j2html.tags.specialized.UlTag;

import static j2html.TagCreator.*;

public class ManagerPage {

//    public UlTag stocksElement(Stock stock) {
//        return

    public static void setup(Javalin app, Restaurant restaurant) {

        var equipeComposant = new EquipeComposant(app, restaurant.getEquipe());
        RestaurantState state = restaurant.getState();
        var manage_open = button(attrs(".bg-primary p-2"), span(state.toString()))
                .attr("hx-patch", "/api/restaurant/toggle")
                .attr("hx-swap", "outerHTML");


        app.get("/manager", ctx -> {
            var content = html(
                    Application.createHeadElement(),
                    body(attrs(".bg-background"),
                            h1("Manager"),
                            h1("Stocks : "),
                            restaurant.getStocks().createStocksElementWButton(),
                            h1("Etat du restaurant : "),
                            manage_open,
                            h1("Gestion des equipes : "),
                            equipeComposant.element()
                    )

            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);

        });
    }

}
