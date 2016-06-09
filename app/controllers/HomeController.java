package controllers;

import LunchManCore.Apprentice;
import LunchManCore.Rota;
import com.opencsv.CSVReader;
import play.mvc.*;

import views.html.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
        String filepath = new File("").getAbsolutePath();
        String csvFilename = filepath.concat("/csvs/apprentices.csv");
        CSVReader csvReader = null;
        List<String[]> names;
        try {
            csvReader = new CSVReader(new FileReader(csvFilename));
            names = csvReader.readAll();
            for (String[] token : names) {
                apprentices.add(new Apprentice(token[0]));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                csvReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        rota.updateSchedule(apprentices);
        return ok(index.render("LunchMan", rota.getSchedule()));
    }

    private void createApprentices(List<String> names) {
        for (String name : names) {
            apprentices.add(new Apprentice(name));
        }
    }

}
