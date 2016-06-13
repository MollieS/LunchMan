package services;

import LunchManCore.Apprentice;
import LunchManCore.FridayLunch;
import LunchManCore.Rota;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CSVHelper {

    public static List<FridayLunch> createScheduleFromCSV(List<FridayLunch> lunches, String csvFilename) throws IOException {
        return createSchedule(lunches, loadCSV(csvFilename));
    }

    public static List<FridayLunch> createSchedule(List<FridayLunch> lunches, List<String[]> fridayLunches) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (String[] lunch : fridayLunches) {
            FridayLunch fridayLunch = new FridayLunch(LocalDate.parse(lunch[1], formatter));
            fridayLunch.assignApprentice(new Apprentice(lunch[0]));
            lunches.add(fridayLunch);
        }
        return lunches;
    }

    public static void saveRotaToCSV(List<FridayLunch> schedule, String csvFilename) throws Exception {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilename));
        for (FridayLunch lunch : schedule) {
            String record = "";
            record += lunch.getApprentice().get().getName();
            record += ",";
            record += lunch.getDate();
            String[] recordArray = record.split(",");
            csvWriter.writeNext(recordArray);
        }
        csvWriter.close();
    }

    public static List<Apprentice> createApprenticesFromCSV(List<Apprentice> apprentices, String csvFilename) throws Exception {
        return createApprentices(apprentices, loadCSV(csvFilename));
    }

    private static List<Apprentice> createApprentices(List<Apprentice> apprentices, List<String[]> names) {
        for (String[] name : names) {
            apprentices.add(new Apprentice(name[0]));
        }
        return apprentices;
    }

    private static List<String[]> loadCSV(String csvPath) throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(csvPath));
        List<String[]> result = csvReader.readAll();
        csvReader.close();
        return result;
    }

}
