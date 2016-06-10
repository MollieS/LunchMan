package services;

import LunchManCore.Apprentice;
import LunchManCore.FridayLunch;
import LunchManCore.Restaurant;
import LunchManCore.Rota;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.awt.geom.IllegalPathStateException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {

    public static List<FridayLunch> createScheduleFromCSV(String csvFilename) throws IOException {
        return createSchedule(csvFilename);
    }

    public static List<Apprentice> createApprenticesFromCSV(String csvFilename) throws Exception {
        return createApprentices(loadCSV(csvFilename));
    }

    public static List<Restaurant> createRestaurantsFromCSV(String csvFilename) throws IOException {
        return createRestaurants(loadCSV(csvFilename));
    }

    public static List<FridayLunch> createSchedule(String csvFilename) throws IOException {
        List<String[]> fridayLunches = loadCSV(csvFilename);
        List<FridayLunch> lunches = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (String[] lunch : fridayLunches) {
            FridayLunch fridayLunch = new FridayLunch(LocalDate.parse(lunch[1], formatter));
            fridayLunch.assignApprentice(new Apprentice(lunch[0]));
            lunches.add(fridayLunch);
        }
        if (!fridayLunches.isEmpty() && fridayLunches.get(0).length > 2) {
            lunches.get(0).assignRestaurant(new Restaurant(fridayLunches.get(0)[2], fridayLunches.get(0)[3]));
        }
        return lunches;
    }

    private static List<Apprentice> createApprentices(List<String[]> names) {
        List<Apprentice> apprentices = new ArrayList<>();
        for (String[] name : names) {
            apprentices.add(new Apprentice(name[0]));
        }
        return apprentices;
    }

    private static List<Restaurant> createRestaurants(List<String[]> restaurantList) {
        List<Restaurant> restaurants = new ArrayList<>();
        for (String[] restaurant : restaurantList) {
            restaurants.add(new Restaurant(restaurant[0], restaurant[1]));
        }
        return restaurants;
    }

    public static void saveRotaToCSV(List<FridayLunch> schedule, String csvFilename) throws Exception {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilename));
        for (FridayLunch lunch : schedule) {
            String record = "";
            record += lunch.getApprentice().get().getName();
            record += ",";
            record += lunch.getDate();
            if (lunch.getRestaurant().isPresent()) {
                record += ",";
                record += lunch.getRestaurant().get().getName();
                record += ",";
                record += lunch.getRestaurant().get().getMenuLink();
            }
            String[] recordArray = record.split(",");
            csvWriter.writeNext(recordArray);
        }
        csvWriter.close();
    }


    private static List<String[]> loadCSV(String csvPath) throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(csvPath));
        List<String[]> result = csvReader.readAll();
        csvReader.close();
        return result;
    }
}
