package fr.bafbi.javaproject.application;

import fr.bafbi.javaproject.Restaurant;
import fr.bafbi.javaproject.RestaurantState;
import io.javalin.Javalin;
import org.eclipse.jetty.server.session.JDBCSessionDataStore;

import static j2html.TagCreator.*;

public class ManagerPage {

//    public UlTag stocksElement(Stock stock) {
//        return

    public static void setup(Javalin app, Restaurant restaurant) {

        var equipeComposant = new EquipeComposant(app, restaurant.getEmployeManager());
        RestaurantState state = restaurant.getState();
        var manage_open = button(attrs(".bg-primary p-2"), span(state.toString()))
                .attr("hx-patch", "/api/restaurant/toggle")
                .attr("hx-swap", "outerHTML");


        app.get("/manager", ctx -> {
            var content = html(
                    Application.createHeadElement(),
                    body(attrs(".bg-background"),
                            Application.HeaderElement(),
                            h1("Manager"),
                            div(attrs(".grid grid-cols-2 gap-5"),
                                    div(attrs(".intradiv"),
                                            h2("Stocks : "),
                                            restaurant.getStocks().createStocksElementWButton()),
                                    div(attrs(".intradiv"),
                                            h2("Etat du restaurant : "),
                                            manage_open),
                                    div(attrs(".intradiv col-span-2"),
                                            h2("Gestion des equipes : "),
                                            equipeComposant.element()
                                    )
                            )


                    )

            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);

        });
    }

}
