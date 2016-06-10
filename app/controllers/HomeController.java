package controllers;

import LunchManCore.Apprentice;
import LunchManCore.FridayLunch;
import LunchManCore.Rota;
import play.mvc.*;

import services.CSVHelper;
import views.html.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static services.CSVHelper.createScheduleFromCSV;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private String apprenticeCSV = "/Users/molliestephenson/Java/LunchMan/csvs/apprentices.csv";
    private String scheduleCSV = "/Users/molliestephenson/Java/LunchMan/csvs/schedule.csv";

    private Rota rota = new Rota(4, LocalDate.now());

    public HomeController() {}

    public HomeController(String apprenticeCSV, String scheduleCSV) {
        this.apprenticeCSV = apprenticeCSV;
        this.scheduleCSV = scheduleCSV;
    }

    public Result index() {
        List<Apprentice> apprentices = new ArrayList<>();
        try {
            CSVHelper.createApprenticesFromCSV(apprentices, apprenticeCSV);
            List<FridayLunch> loadedSchedule = createScheduleFromCSV(new ArrayList<>(), scheduleCSV);
            rota.setSchedule(loadedSchedule);
            rota.updateSchedule(apprentices);
            CSVHelper.saveRotaToCSV(rota.getSchedule(), scheduleCSV);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ok(index.render("LunchMan", rota.getSchedule()));
    }

    public Result changeSchedule() throws Exception {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        List<FridayLunch> schedule = rota.getSchedule();
        FridayLunch fridayLunch = schedule.get(Integer.valueOf(request.get("position")[0]));
        fridayLunch.assignApprentice(new Apprentice(request.get("newName")[0]));
        CSVHelper.saveRotaToCSV(rota.getSchedule(), scheduleCSV);
        return redirect("/");
    }
}
