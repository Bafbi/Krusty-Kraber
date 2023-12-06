package fr.bafbi.javaproject.application;

import fr.bafbi.javaproject.Restaurant;
import io.javalin.Javalin;

import static j2html.TagCreator.*;

public class ManagerPage {

    public static void setup(Javalin app, Restaurant restaurant) {

        var equipeComposant = new EquipeComposant(app, restaurant.getEmployeManager());

        app.get("/manager", ctx -> {
            var content = html(
                    Application.createHeadElement(),
                    body(attrs(".bg-background"),
                            h1("Manager"),
                            equipeComposant.element()
                    )

            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);

        });
    }

}
