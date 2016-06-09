package controllers;

import LunchManCore.Apprentice;
import LunchManCore.Rota;
import play.mvc.*;

import services.CSVHelper;
import views.html.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private String apprenticeCSV = "/Users/molliestephenson/Java/LunchMan/csvs/apprentices.csv";
    private String scheduleCSV = "/Users/molliestephenson/Java/LunchMan/csvs/schedule.csv";

    private List<Apprentice> apprentices = new ArrayList<>();
    private Rota rota = new Rota(4, LocalDate.now());

    public Result index() {
        try {
            CSVHelper.createApprenticesFromCSV(apprentices, apprenticeCSV);
            CSVHelper.loadScheduleFromCSV(rota, scheduleCSV);
            rota.updateSchedule(apprentices);
            CSVHelper.saveRotaToCSV(rota, scheduleCSV);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ok(index.render("LunchMan", rota.getSchedule()));
    }

}
