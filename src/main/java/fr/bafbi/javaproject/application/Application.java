package fr.bafbi.javaproject.application;

import fr.bafbi.javaproject.Restaurant;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import j2html.tags.specialized.H2Tag;
import j2html.tags.specialized.HeadTag;

import java.util.concurrent.atomic.AtomicInteger;

import static j2html.TagCreator.*;

public class Application {

    private final Restaurant restaurant;

    public Application(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public static HeadTag createHeadElement() {
        return head(
                script().withSrc("/webjars/htmx.org/1.9.2/dist/htmx.min.js"),
                link().withRel("stylesheet").withHref("/css/project.css")
        );
    }

    public void run() {
        // Create a new Javalin instance.
        // By default it will run on port 8080, but can be changed.
        var app = Javalin.create(javalinConfig -> {
            javalinConfig.staticFiles.enableWebjars();
            javalinConfig.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/css";
                staticFileConfig.directory = "build/resources/css/";
                staticFileConfig.location = Location.EXTERNAL;
            });
        });


        // Handle a GET request to the path "/".
        var count = new AtomicInteger();
        app.get("/", ctx ->

        {
            var content = html(

                    createHeadElement(),
                    body(
                            h1("Hello world"),
                            createCounterElement(count.get()),
                            button("Increment")
                                    .attr("hx-post", "/increment")
                                    .attr("hx-target", "#counter")
                                    .attr("hx-swap", "outerHTML")
                                    .withClasses("p-4", "bg-blue-500", "text-white", "rounded-lg")
                    )

            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);
        });

        app.post("/increment", ctx ->

        {
            var newCounter = createCounterElement(count.incrementAndGet());
            ctx.html(newCounter.render());
        });

        // Handle a GET request to the path "/cuisine".
        var cuisineHandler = new CuisineHandler(app, restaurant.getCuisine());
        ManagerPage.setup(app, restaurant);

        app.start();
    }

    // Create a H2 tag with an id.
    private H2Tag createCounterElement(int count) {
        return h2("count: " + count)
                .withId("counter").withClasses("my-title");
    }

}


