import LunchManCore.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import services.CSVRepository;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVRepositoryTest {

    private Storage storage;
    private List<Employee> loadedEmployees;
    private List<FridayLunch> loadedSchedule;
    private List<Guest> loadedGuests;

    @Before
    public void setUp() {
        storage = new CSVRepository("apprentices.csv", "restaurants.csv", "schedule.csv", "employees.csv", "guests.csv");
        loadedEmployees = storage.getEmployees();
        loadedSchedule = storage.getSchedule();
        loadedGuests = storage.getGuests();
    }

    @After
    public void tearDown() {
        storage.saveEmployees(loadedEmployees);
        storage.saveSchedule(loadedSchedule);
        storage.saveGuests(loadedGuests);
    }

    @Test
    public void createsApprenticesFromListOfNamesInCSV() throws Exception {
        List<Apprentice> result = storage.getApprentices();
        assertEquals("Mollie", result.get(0).getName());
        assertEquals("Nick", result.get(1).getName());
    }

    @Test
    public void createsRotaFromCSV() throws Exception {
        List<FridayLunch> result = storage.getSchedule();
        assertEquals("Mollie", result.get(0).getApprentice().get().getName());
        assertEquals("Nick", result.get(1).getApprentice().get().getName());
    }

    @Test
    public void createsRestaurantsFromCSV() throws Exception {
        List<Restaurant> result = storage.getRestaurants();
        assertEquals("Bahn Mi Bay", result.get(0).getName());
        assertEquals("Chillango", result.get(1).getName());
    }

    @Test
    public void createsEmployeesFromCSV() throws IOException {
        List<Employee> employees = storage.getEmployees();
        assertEquals("Peter", employees.get(0).getName());
    }

    @Test
    public void createsGuestsFromCSV() throws IOException {
        List<Guest> guests = storage.getGuests();
        assertEquals("Tom", guests.get(0).getName());
    }

   @Test
   public void updatesSavedCSVSchedule() throws Exception {
       List<FridayLunch> schedule = storage.getSchedule();
       Apprentice rabea = new Apprentice("Rabea");
       schedule.get(1).assignApprentice(rabea);
       storage.saveSchedule(schedule);
       List<FridayLunch> result = storage.getSchedule();
       assertEquals("Rabea", result.get(1).getApprentice().get().getName());
   }


    @Test
    public void updatesSavedCSVEmployees() throws Exception {
        List<Employee> employees = storage.getEmployees();
        employees.get(1).addOrder("Peri Peri Egg");
        storage.saveEmployees(employees);
        List<Employee> result = storage.getEmployees();
        assertEquals("Peri Peri Egg", result.get(1).getOrder().get());
    }

    @Test
    public void updatesSavedCSVGuests() throws Exception {
        List<Guest> guests = storage.getGuests();
        Guest gary = new Guest("Gary", "Peri Peri Egg");
        guests.add(gary);
        storage.saveGuests(guests);
        List<Guest> result = storage.getGuests();
        assertEquals("Gary", result.get(1).getName());
    }

}
