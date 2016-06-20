package controllers;

import LunchManCore.*;
import play.mvc.*;

import services.CSVRepository;
import views.html.*;

import java.util.Map;


public class HomeController extends Controller {

    private CSVRepository csv = new CSVRepository("apprentices.csv", "restaurants.csv", "schedule.csv", "employees.csv", "guests.csv");
    private CurrentDate currentDate = new CurrentDate();
    private DateFake fake = new DateFake(2017, 1, 7);

    public Result index() {
        LunchManCore core = LunchManCore.create(csv, currentDate);
        return ok(index.render("LunchMan", core.getSchedule(), core.getRestaurants(), core.getEmployees(), core.getGuests()));
    }

    public Result changeSchedule() {
        LunchManCore core = LunchManCore.create(csv, currentDate);
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        Integer friday = Integer.valueOf(request.get("position")[0]);
        String newName = request.get("newName")[0];

        core.assignApprenticeToLunch(friday, newName);

        return redirect("/");
    }

    public Result assignMenu() {
        LunchManCore core = LunchManCore.create(csv, currentDate);
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        Integer restaurant = Integer.valueOf(request.get("restaurant")[0]);

        core.chooseNextFridayMenu(restaurant);

        return redirect("/");
    }

    public Result newOrder() {
        LunchManCore core = LunchManCore.create(csv, currentDate);
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        Integer employee = Integer.valueOf(request.get("name")[0]);
        String order = request.get("order")[0];

        core.placeOrder(employee, order);
        return redirect("/");
    }

    public Result newGuest() {
        LunchManCore core = LunchManCore.create(csv, currentDate);
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        String name = request.get("name")[0];
        String order = request.get("order")[0];

        core.addAGuest(name, order);
        return redirect("/");
    }
}
