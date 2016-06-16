package services;

import LunchManCore.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVRepository implements Storage {

    private String apprenticeCSV;
    private String scheduleCSV;
    private String restaurantCSV;
    private String employeesCSV;
    private String guestsCSV;

    public CSVRepository(String apprenticeCSV, String restaurantCSV, String scheduleCSV, String employeesCSV, String guestsCSV) {
        this.apprenticeCSV = getAbsolutePathOfResource(apprenticeCSV);
        this.scheduleCSV = getAbsolutePathOfResource(scheduleCSV);
        this.restaurantCSV = getAbsolutePathOfResource(restaurantCSV);
        this.employeesCSV = getAbsolutePathOfResource(employeesCSV);
        this.guestsCSV = getAbsolutePathOfResource(guestsCSV);
    }

    public List<Apprentice> getApprentices() {
        List<String[]> names = loadCSV(apprenticeCSV);
        List<Apprentice> apprentices = new ArrayList<>();
        for (String[] name : names) {
            apprentices.add(new Apprentice(name[0]));
        }
        return apprentices;
    }

    public List<Employee> getEmployees() {
        List<String[]> names = loadCSV(employeesCSV);
        List<Employee> employees = new ArrayList<>();
        for (String[] name : names) {
            Employee employee = new Employee(name[0]);
            employees.add(employee);
            if (name.length > 1) {
                employee.addOrder(name[1]);
            }
        }
        return employees;
    }

    public List<Guest> getGuests() {
        List<String[]> guestList = loadCSV(guestsCSV);
        List<Guest> guests = new ArrayList<>();
        for (String[] guestListEntry : guestList) {
            Guest guest = new Guest(guestListEntry[0], guestListEntry[1]);
            guests.add(guest);
        }
        return guests;
    }

    public List<Restaurant> getRestaurants() {
        List<String[]> restaurantList = loadCSV(restaurantCSV);
        List<Restaurant> restaurants = new ArrayList<>();
        for (String[] restaurant : restaurantList) {
            restaurants.add(new Restaurant(restaurant[0], restaurant[1]));
        }
        return restaurants;
    }

    public List<FridayLunch> getSchedule() {
        List<String[]> fridayLunches = loadCSV(scheduleCSV);
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

    public void saveSchedule(List<FridayLunch> lunches) {
        CSVWriter csvWriter = null;
        try {
            csvWriter = new CSVWriter(new FileWriter(scheduleCSV));
            for (FridayLunch lunch : lunches) {
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
        } catch (IOException e) {
            throw new RuntimeException("Cannot load CSV");
        }
    }

    public void saveEmployees(List<Employee> employees) {
        CSVWriter csvWriter = null;
        try {
            csvWriter = new CSVWriter(new FileWriter(employeesCSV));
            for (Employee employee : employees) {
                String record = "";
                record += employee.getName();
                if (employee.getOrder().isPresent()) {
                    record += ",";
                    record += employee.getOrder().get();
                }
                String[] recordArray = record.split(",");
                csvWriter.writeNext(recordArray);
            }
            csvWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load CSV");
        }
    }

    public void saveGuests(List<Guest> guests) {
        CSVWriter csvWriter = null;
        try {
            csvWriter = new CSVWriter(new FileWriter(guestsCSV));
            for (Guest guest : guests) {
                String record = "";
                record += guest.getName();
                record += ",";
                record += guest.getOrder();
                String[] recordArray = record.split(",");
                csvWriter.writeNext(recordArray);
            }
            csvWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load CSV");
        }
    }

    private static List<String[]> loadCSV(String csvPath) {
        System.out.println("Loading CSV from: " + csvPath);
        List<String[]> result;
        try {
            CSVReader csvReader = new CSVReader(new FileReader(csvPath));
            result = csvReader.readAll();
            csvReader.close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load CSV");
        }
        return result;
    }

    private String getAbsolutePathOfResource(String name) {
        try {
            return new File(getClass().getClassLoader().getResource("resources/" + name).toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Resource not found");
        }
    }
}
