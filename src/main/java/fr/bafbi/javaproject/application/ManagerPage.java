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
                            div(attrs(".grid grid-cols-4 gap-5"),
                                    div(attrs(".intradiv col-span-3"),
                                            h2(attrs(".font-bold .text-center"),"Stocks"),
                                            restaurant.getStocks().createStocksElementWButton()),
                                    div(attrs(".intradiv text-center"),
                                            h2(attrs(".font-bold .text-center"),"Etat du restaurant"),
                                            manage_open,
                                            h2(attrs(".font-bold .text-center"),"Refaire les Stocks"),
                                            button("Acheter")),
                                    div(attrs(".intradiv col-span-4"),
                                            h2(attrs(".font-bold .text-center"),"Gestion des equipes"),
                                            equipeComposant.element()
                                    )
                            )


                    )

            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);

        });

        app.patch("/api/restaurant/toggle", ctx -> {
            int newState = restaurant.getState().ordinal()+1;
            restaurant.setState(RestaurantState.values()[newState%3]);
            ctx.html(
                    button(attrs(".bg-primary p-2"), span(restaurant.getState().toString()))
                    .attr("hx-patch", "/api/restaurant/toggle")
                    .attr("hx-swap", "outerHTML").render()
            );
        });
    }

}
