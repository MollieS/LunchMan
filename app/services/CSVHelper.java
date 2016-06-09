package services;

import LunchManCore.Apprentice;
import LunchManCore.FridayLunch;
import LunchManCore.Rota;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CSVHelper {

    public static void loadScheduleFromCSV(Rota rota, String csvFilename) throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(csvFilename));
        createSchedule(rota, csvReader.readAll());
        csvReader.close();
    }

    public static void createSchedule(Rota rota, List<String[]> fridayLunches) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (String[] lunch : fridayLunches) {
            rota.clear();
            rota.assign(new FridayLunch(LocalDate.parse(lunch[1], formatter)), new Apprentice(lunch[0]));
        }
    }

    public static void saveRotaToCSV(Rota rota, String csvFilename) throws Exception {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilename));
        for (FridayLunch lunch : rota.getSchedule()) {
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
        return createApprentices(apprentices, loadListOfNamesFromCSV(csvFilename));
    }

    public static List<Apprentice> createApprentices(List<Apprentice> apprentices, List<String[]> names) {
        for (String[] name : names) {
            apprentices.add(new Apprentice(name[0]));
        }
        return apprentices;
    }

    public static List<String[]> loadListOfNamesFromCSV(String csvPath) throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(csvPath));
        List<String[]> result = csvReader.readAll();
        csvReader.close();
        return result;
    }
}
