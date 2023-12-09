package fr.bafbi.javaproject.application;

import fr.bafbi.javaproject.Restaurant;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import j2html.tags.specialized.H2Tag;
import j2html.tags.specialized.HeadTag;
import j2html.tags.specialized.HeaderTag;

import java.util.concurrent.atomic.AtomicInteger;

import static j2html.TagCreator.*;

public class Application {

    private final Restaurant restaurant;

    public Application(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public static HeadTag createHeadElement() {
        return head(
                meta().withCharset("utf-8"),
//                meta().withCharset("Windows-1252"),
                script().withSrc("/webjars/htmx.org/1.9.2/dist/htmx.min.js"),
                link().withRel("stylesheet").withHref("/css/project.css")
        );
    }

    public void run() {
        // Create a new Javalin instance.
        // By default it will run on port 8080, but can be changed.
        var app = Javalin.create(javalinConfig -> {

            javalinConfig.http.defaultContentType = "text/html;charset=UTF-8";
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
                            HeaderElement(),
                            h1("Bienvenue au restaurant"),
                            p("Nombre de visiteurs : " + restaurant.getTransactionManager().getTransactionCount())
                    )

            );
            var rendered = "<!DOCTYPE html>\n" + content.render();
            ctx.html(rendered);
        });


        // Handle a GET request to the path "/cuisine".
        ManagerPage.setup(app, restaurant);
        ServeurPage.setup(app, restaurant);
        CuisinePage.setup(app, restaurant);
        BarPage.setup(app, restaurant);

        app.start();
    }

    public static HeaderTag HeaderElement() {
        return header(
                nav(attrs(".flex .flex-row .justify-between .items-center .bg-background .p-4"),
                        a("Home").withHref("/"),
                        a("Cuisine").withHref("/cuisine"),
                        a("Manager").withHref("/manager"),
                        a("Serveur").withHref("/serveur"),
                        a("Bar").withHref("/bar")
                )
        );
    }

}


