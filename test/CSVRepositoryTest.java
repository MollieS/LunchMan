import LunchManCore.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import services.CSVRepository;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVRepositoryTest {

    private Storage csvStorage;
    private List<Employee> loadedEmployees;
    private List<FridayLunch> loadedSchedule;

    @Before
    public void setUp() {
        csvStorage = new CSVRepository("apprentices.csv", "restaurants.csv", "schedule.csv", "employees.csv");
        loadedEmployees = csvStorage.getEmployees();
        loadedSchedule = csvStorage.getSchedule();
    }

    @After
    public void tearDown() {
        csvStorage.saveEmployees(loadedEmployees);
        csvStorage.saveSchedule(loadedSchedule);
    }

    @Test
    public void createsApprenticesFromListOfNamesInCSV() throws Exception {
        List<Apprentice> result = csvStorage.getApprentices();
        assertEquals("Mollie", result.get(0).getName());
        assertEquals("Nick", result.get(1).getName());
    }

    @Test
    public void createsRotaFromCSV() throws Exception {
        List<FridayLunch> result = csvStorage.getSchedule();
        assertEquals("Mollie", result.get(0).getApprentice().get().getName());
        assertEquals("Nick", result.get(1).getApprentice().get().getName());
    }

    @Test
    public void createsRestaurantsFromCSV() throws Exception {
        List<Restaurant> result = csvStorage.getRestaurants();
        assertEquals("Bahn Mi Bay", result.get(0).getName());
        assertEquals("Chillango", result.get(1).getName());
    }

    @Test
    public void createsEmployeesFromCSV() throws IOException {
        List<Employee> employees = csvStorage.getEmployees();
        assertEquals("Peter", employees.get(0).getName());
    }

   @Test
   public void updatesSavedCSVSchedule() throws Exception {
       List<FridayLunch> schedule = csvStorage.getSchedule();
       Apprentice rabea = new Apprentice("Rabea");
       schedule.get(1).assignApprentice(rabea);
       csvStorage.saveSchedule(schedule);
       List<FridayLunch> result = csvStorage.getSchedule();
       assertEquals("Rabea", result.get(1).getApprentice().get().getName());
   }


    @Test
    public void updatesSavedCSVEmployees() throws Exception {
        List<Employee> employees = csvStorage.getEmployees();
        employees.get(1).addOrder("Peri Peri Egg");
        csvStorage.saveEmployees(employees);
        List<Employee> result = csvStorage.getEmployees();
        assertEquals("Peri Peri Egg", result.get(1).getOrder().get());
    }

}
