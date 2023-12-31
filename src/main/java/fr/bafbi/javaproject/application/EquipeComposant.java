package fr.bafbi.javaproject.application;

import fr.bafbi.javaproject.EmployeManager;
import io.javalin.Javalin;
import j2html.tags.specialized.ButtonTag;
import j2html.tags.specialized.DivTag;
import j2html.tags.specialized.UlTag;

import static j2html.TagCreator.*;

public class EquipeComposant {

    private final Javalin app;
    private final EmployeManager employeManager;

    public EquipeComposant(Javalin app, EmployeManager employeManager) {
        this.app = app;
        this.employeManager = employeManager;
        this.register();
    }

    private static ButtonTag planificationElement(boolean planifie, int employeId) {
        return button(span("Plannifie : "), span(planifie ? "Oui" : "Non"))
                .attr("hx-patch", "/api/equipe/" + employeId + "/toggle")
                .attr("hx-swap", "outerHTML");

    }

    public void register() {
        app.patch("/api/equipe/{employeId}/toggle", ctx -> {
            int employeId = Integer.parseInt(ctx.pathParam("employeId"));
            var isPlanifie = employeManager.isPlanifie(employeId);
            if (isPlanifie) {
                employeManager.annuler(employeId);
            } else {
                employeManager.planifier(employeId);
            }
            ctx.html(html(planificationElement(!isPlanifie, employeId)).render());
        });
    }

    public DivTag element() {
        return div(attrs("#equipe .grid grid-cols-3 gap-4"),
                each(employeManager.getEmployes(), employe -> div(attrs(".employe"), employe.element(), planificationElement(employeManager.isPlanifie(employe.getId()), employe.getId()))));
    }
}