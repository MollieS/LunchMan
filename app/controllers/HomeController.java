package controllers;

import LunchManCore.*;
import com.google.inject.Inject;
import play.mvc.*;

import services.CSVHelper;
import views.html.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static services.CSVHelper.createScheduleFromCSV;

public class HomeController extends Controller {


    private String apprenticeCSV;
    private String scheduleCSV;
    private String restaurantCSV;
    private String employeesCSV;

    private Rota rota = new Rota(4, LocalDate.now());

    public HomeController() {
        apprenticeCSV = new CSVHelper().getAbsolutePathOfResource("apprentices.csv");
        scheduleCSV = new CSVHelper().getAbsolutePathOfResource("schedule.csv");
        restaurantCSV = new CSVHelper().getAbsolutePathOfResource("restaurants.csv");
        employeesCSV = new CSVHelper().getAbsolutePathOfResource("employees.csv");
    }

    public Result index() {
        List<Apprentice> apprentices = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        List<Restaurant> restaurants = new ArrayList<>();
        try {
            restaurants = CSVHelper.createRestaurantsFromCSV(restaurantCSV);
            employees = CSVHelper.createEmployeesFromCSV(employeesCSV);
            CSVHelper.createApprenticesFromCSV(apprenticeCSV);
            List<FridayLunch> loadedSchedule = createScheduleFromCSV(scheduleCSV);
            rota.setSchedule(loadedSchedule);
            rota.updateSchedule(apprentices);
            CSVHelper.saveRotaToCSV(rota.getSchedule(), scheduleCSV);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ok(index.render("LunchMan", rota.getSchedule(), restaurants, employees));
    }

    public Result changeSchedule() throws Exception {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        List<FridayLunch> schedule = rota.getSchedule();
        FridayLunch fridayLunch = schedule.get(Integer.valueOf(request.get("position")[0]));
        fridayLunch.assignApprentice(new Apprentice(request.get("newName")[0]));
        CSVHelper.saveRotaToCSV(rota.getSchedule(), scheduleCSV);
        return redirect("/");
    }

    public Result assignMenu() throws Exception {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        List<Restaurant> restaurants = CSVHelper.createRestaurantsFromCSV(restaurantCSV);
        List<FridayLunch> schedule = rota.getSchedule();
        FridayLunch fridayLunch = schedule.get(0);
        fridayLunch.assignRestaurant(restaurants.get(Integer.valueOf(request.get("restaurant")[0])));
        CSVHelper.saveRotaToCSV(rota.getSchedule(), scheduleCSV);
        return redirect("/");
    }

    public Result newOrder() throws IOException {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        List<Employee> employees = CSVHelper.createEmployeesFromCSV(employeesCSV);
        employees.get(Integer.valueOf(request.get("name")[0])).addOrder(request.get("order")[0]);
        CSVHelper.saveEmployeesToCSV(employees, employeesCSV);
        return redirect("/");
    }
}
