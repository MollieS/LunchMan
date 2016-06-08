package controllers;

import LunchManCore.Apprentice;
import LunchManCore.Rota;
import play.mvc.*;

import views.html.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private List<Apprentice> apprentices = new ArrayList<>();
    private Rota rota = new Rota(4, LocalDate.now());

    public Result index() {
        List<String> apprenticeNames = Arrays.asList("Priya", "Mollie", "Nick", "Rabea", "Ced");
        createApprentices(apprenticeNames);
        rota.updateSchedule(apprentices);
        return ok(index.render("LunchMan", rota.getSchedule()));
    }

    private void createApprentices(List<String> names) {
        for (String name : names) {
            apprentices.add(new Apprentice(name));
        }
    }

}
