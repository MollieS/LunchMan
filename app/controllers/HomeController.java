package controllers;

import LunchManCore.*;
import play.mvc.*;

import services.CSVHelper;
import views.html.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static services.CSVHelper.*;
import static services.CSVHelper.createScheduleFromCSV;

public class HomeController extends Controller {


    private String apprenticeCSV;
    private String scheduleCSV;
    private String restaurantCSV;
    private String employeesCSV;
    private Rota rota;

    public HomeController() {
        apprenticeCSV = new CSVHelper().getAbsolutePathOfResource("apprentices.csv");
        scheduleCSV = new CSVHelper().getAbsolutePathOfResource("schedule.csv");
        restaurantCSV = new CSVHelper().getAbsolutePathOfResource("restaurants.csv");
        employeesCSV = new CSVHelper().getAbsolutePathOfResource("employees.csv");
    }

    public Result index() {
        rota = new Rota(4, LocalDate.now());
        rota.updateSchedule(getSchedule(), getApprentices());
        saveRotaToCSV(rota.getSchedule(), scheduleCSV);

        return ok(index.render("LunchMan", rota.getSchedule(), getRestaurants(), getEmployees()));
    }

    public Result changeSchedule() throws Exception {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        List<FridayLunch> schedule = rota.getSchedule();
        FridayLunch fridayLunch = schedule.get(Integer.valueOf(request.get("position")[0]));
        fridayLunch.assignApprentice(new Apprentice(request.get("newName")[0]));
        saveRotaToCSV(rota.getSchedule(), scheduleCSV);
        return redirect("/");
    }

    public Result assignMenu() throws Exception {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        List<Restaurant> restaurants = createRestaurantsFromCSV(restaurantCSV);
        List<FridayLunch> schedule = rota.getSchedule();
        FridayLunch fridayLunch = schedule.get(0);
        fridayLunch.assignRestaurant(restaurants.get(Integer.valueOf(request.get("restaurant")[0])));
        saveRotaToCSV(rota.getSchedule(), scheduleCSV);
        return redirect("/");
    }

    public Result newOrder() throws IOException {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        List<Employee> employees = getEmployees();
        employees.get(Integer.valueOf(request.get("name")[0])).addOrder(request.get("order")[0]);
        saveEmployeesToCSV(employees, employeesCSV);
        return redirect("/");
    }

    private List<FridayLunch> getSchedule() {
        return createScheduleFromCSV(scheduleCSV);
    }

    private List<Apprentice> getApprentices() {
        return createApprenticesFromCSV(apprenticeCSV);
    }

    private List<Employee> getEmployees() {
        return createEmployeesFromCSV(employeesCSV);
    }

    private List<Restaurant> getRestaurants() {
        return createRestaurantsFromCSV(restaurantCSV);
    }

}
