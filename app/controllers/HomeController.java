package controllers;

import LunchManCore.*;
import play.mvc.*;

import services.CSVRepository;
import views.html.*;

import java.util.Map;


public class HomeController extends Controller {

    CSVRepository csv = new CSVRepository("apprentices.csv", "restaurants.csv", "schedule.csv", "employees.csv");
    LunchManCore core = new LunchManCore(csv);

    public Result index() {
        Rota rota = core.getCurrentSchedule();
        return ok(index.render("LunchMan", rota.getSchedule(), core.getRestaurants(), core.getEmployees()));
    }

    public Result changeSchedule() {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        Integer friday = Integer.valueOf(request.get("position")[0]);
        String newName = request.get("newName")[0];

        core.assignApprenticeToLunch(friday, newName);

        return redirect("/");
    }

    public Result assignMenu() {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        Integer restaurant = Integer.valueOf(request.get("restaurant")[0]);

        core.chooseNextFridayMenu(restaurant);

        return redirect("/");
    }

    public Result newOrder() {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        Integer employee = Integer.valueOf(request.get("name")[0]);
        String order = request.get("order")[0];

        core.placeOrder(employee, order);
        return redirect("/");
    }

}
