package controllers;

import play.mvc.*;

import views.html.*;

import java.util.Arrays;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    public Result index() {
        List<String> apprentices = Arrays.asList("Priya", "Mollie", "Nick", "Rabea", "Ced");
        return ok(index.render("LunchMan", apprentices));
    }

}
