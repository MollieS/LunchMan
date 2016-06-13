package services;

import LunchManCore.Apprentice;
import LunchManCore.FridayLunch;
import LunchManCore.Restaurant;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {

    public static List<FridayLunch> createScheduleFromCSV(String csvFilename) throws IOException {
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

    public static List<Apprentice> createApprenticesFromCSV(String csvFilename) throws IOException {
        List<String[]> names = loadCSV(csvFilename);
        List<Apprentice> apprentices = new ArrayList<>();
        for (String[] name : names) {
            apprentices.add(new Apprentice(name[0]));
        }
        return apprentices;
    }

    public static List<Restaurant> createRestaurantsFromCSV(String csvFilename) throws IOException {
        List<String[]> restaurantList = loadCSV(csvFilename);
        List<Restaurant> restaurants = new ArrayList<>();
        for (String[] restaurant : restaurantList) {
            restaurants.add(new Restaurant(restaurant[0], restaurant[1]));
        }
        return restaurants;
    }

    public static void saveRotaToCSV(List<FridayLunch> schedule, String csvFilename) throws Exception {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilename));
        for (FridayLunch lunch : schedule) {
            String[] record = new String[4];
            record[0] =  lunch.getApprentice().get().getName();
            record[1] = lunch.getDate().toString();
            if (lunch.getRestaurant().isPresent()) {
                record[2] = lunch.getRestaurant().get().getName();
                record[3] = lunch.getRestaurant().get().getMenuLink();
            }
            csvWriter.writeNext(record);
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
