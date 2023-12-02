package fr.bafbi.javaproject.application;

import fr.bafbi.javaproject.Cuisine;
import fr.bafbi.javaproject.Recette;
import io.javalin.Javalin;
import j2html.tags.specialized.DivTag;
import j2html.tags.specialized.PTag;

import static j2html.TagCreator.*;

public class CuisineHandler {

    private final Javalin javalin;
    private final Cuisine cuisine;

    public CuisineHandler(Javalin javalin, Cuisine cuisine) {
        this.javalin = javalin;
        this.cuisine = cuisine;
        this.setup();
    }

    public void setup() {
        javalin.get("/cuisine", ctx -> {
            var content = html(
                    Application.createHeadElement(),
                    body(

                            h1("Cuisine"),
                            button("Cuisiner")
                                    .attr("hx-post", "/cuisiner")
                                    .attr("hx-target", "#cuisine")
                                    .attr("hx-swap", "outerHTML")
                                    .withId("cuisine"),
                            each(cuisine.getCommands(), transaction ->
                                    div(attrs(".command"),
                                            h2(transaction.toString()),
                                            each(transaction.getRecettes(), this::recetteElement
                                            )
                                    )

                            ),
                            cuisine.getStocks().createStocksElement()
                    )

            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);

        });

        javalin.post("/cuisiner", ctx -> {
            var newCounter = createCounterElement();
            ctx.html(newCounter.render());
        });
    }

    private PTag createCounterElement() {
        return p("Cuisiné")
                .withId("cuisine");
    }

    private DivTag recetteElement(Recette recette) {
        return div(
                attrs("#recette"),
                h2(recette.getName()),
                each(recette.getIngredients().toList(), ingredient ->
                        div(attrs(".ingredient")

                        )
                )
        );
    }

}
