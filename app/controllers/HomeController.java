package controllers;

import LunchManCore.*;
import play.mvc.*;

import services.BusinessLogic;
import services.CSVRepository;
import views.html.*;

import java.util.Map;


public class HomeController extends Controller {

    CSVRepository csv = new CSVRepository("apprentices.csv", "restaurants.csv", "schedule.csv", "employees.csv");
    BusinessLogic logic = new BusinessLogic(csv);

    public Result index() {
        Rota rota = logic.getCurrentSchedule();
        return ok(index.render("LunchMan", rota.getSchedule(), csv.getRestaurants(), csv.getEmployees()));
    }

    public Result changeSchedule() {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        Integer friday = Integer.valueOf(request.get("position")[0]);
        String newName = request.get("newName")[0];

        logic.assignApprenticeToLunch(friday, newName);

        return redirect("/");
    }

    public Result assignMenu() {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        Integer restaurant = Integer.valueOf(request.get("restaurant")[0]);

        logic.chooseNextFridayMenu(restaurant);

        return redirect("/");
    }

    public Result newOrder() {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        Integer employee = Integer.valueOf(request.get("name")[0]);
        String order = request.get("order")[0];

        logic.placeOrder(employee, order);
        return redirect("/");
    }

}
