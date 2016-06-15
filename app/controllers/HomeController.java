package controllers;

import LunchManCore.*;
import play.mvc.*;

import services.CSVHelper;
import views.html.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static services.CSVHelper.*;

public class HomeController extends Controller {


    private String apprenticeCSV;
    private String scheduleCSV;
    private String restaurantCSV;
    private String employeesCSV;

    public HomeController() {
        apprenticeCSV = new CSVHelper().getAbsolutePathOfResource("apprentices.csv");
        scheduleCSV = new CSVHelper().getAbsolutePathOfResource("schedule.csv");
        restaurantCSV = new CSVHelper().getAbsolutePathOfResource("restaurants.csv");
        employeesCSV = new CSVHelper().getAbsolutePathOfResource("employees.csv");
    }

    public Result index() {
        Rota rota = getCurrentSchedule();
        return ok(index.render("LunchMan", rota.getSchedule(), getRestaurants(), getEmployees()));
    }

    public Result changeSchedule() {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        Integer friday = Integer.valueOf(request.get("position")[0]);
        String newName = request.get("newName")[0];

        assignApprenticeToLunch(friday, newName);

        return redirect("/");
    }

    public Result assignMenu() {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        Integer restaurant = Integer.valueOf(request.get("restaurant")[0]);

        chooseNextFridayMenu(restaurant);

        return redirect("/");
    }


    public Result newOrder() {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        Integer employee = Integer.valueOf(request.get("name")[0]);
        String order = request.get("order")[0];

        placeOrder(employee, order);
        return redirect("/");
    }

    private void placeOrder(Integer employee, String order) {
        List<Employee> employees = getEmployees();
        employees.get(employee).addOrder(order);
        saveEmployees(employees);
    }

    private void chooseNextFridayMenu(Integer restaurant) {
        List<Restaurant> restaurants = getRestaurants();
        Rota rota = getCurrentSchedule();
        FridayLunch fridayLunch = rota.getSchedule().get(0);
        fridayLunch.assignRestaurant(restaurants.get(restaurant));
        saveSchedule(rota);
    }

    private Rota getCurrentSchedule() {
        Rota rota = new Rota(4, LocalDate.now());
        rota.updateSchedule(getSchedule(), getApprentices());
        saveSchedule(rota);
        return rota;
    }

    private void assignApprenticeToLunch(Integer schedulePosition, String newName) {
        Rota rota = getCurrentSchedule();
        FridayLunch fridayLunch = rota.getSchedule().get(schedulePosition);
        fridayLunch.assignApprentice(new Apprentice(newName));
        saveSchedule(rota);
    }


    private void saveEmployees(List<Employee> employees) {
        saveEmployeesToCSV(employees, employeesCSV);
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

    private void saveSchedule(Rota rota) {
        saveRotaToCSV(rota.getSchedule(), scheduleCSV);
    }

}
