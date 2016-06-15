import LunchManCore.Apprentice;
import LunchManCore.Employee;
import LunchManCore.FridayLunch;
import LunchManCore.Restaurant;
import org.junit.Before;
import org.junit.Test;
import services.CSVHelper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVHelperTest {

    private String apprenticeCSV;
    private String scheduleCSV;
    private String scheduleWriteCSV;
    private String restaurantCSV;
    private String employeesCSV;
    private String employeesWriteCSV;

    @Before
    public void setUp() {
        apprenticeCSV = getAbsolutePathOfResource("apprentices.csv");
        scheduleCSV = getAbsolutePathOfResource("schedule.csv");
        restaurantCSV = getAbsolutePathOfResource("restaurants.csv");
        employeesCSV = getAbsolutePathOfResource("employees.csv");
        employeesWriteCSV = getAbsolutePathOfResource("mockWriteEmployees.csv");
        scheduleWriteCSV = getAbsolutePathOfResource("mockWriteSchedule.csv");
    }

    @Test
    public void createsApprenticesFromListOfNamesInCSV() throws Exception {
        List<Apprentice> result = CSVHelper.createApprenticesFromCSV(apprenticeCSV);
        assertEquals("Mollie", result.get(0).getName());
        assertEquals("Nick", result.get(1).getName());
        assertEquals("Ced", result.get(2).getName());
        assertEquals("Priya", result.get(3).getName());
        assertEquals("Rabea", result.get(4).getName());
    }

    @Test
    public void createsRotaFromCSV() throws Exception {
        List<FridayLunch> result = CSVHelper.createScheduleFromCSV(scheduleCSV);
        assertEquals("Mollie", result.get(0).getApprentice().get().getName());
        assertEquals("Nick", result.get(1).getApprentice().get().getName());
        assertEquals("Rabea", result.get(2).getApprentice().get().getName());
        assertEquals("Priya", result.get(3).getApprentice().get().getName());
    }

    @Test
    public void createsRestaurantsFromCSV() throws Exception {
        List<Restaurant> result = CSVHelper.createRestaurantsFromCSV(restaurantCSV);
        assertEquals("Bahn Mi Bay", result.get(0).getName());
        assertEquals("Chillango", result.get(1).getName());
        assertEquals("Deliveroo", result.get(2).getName());
    }

   @Test
   public void updatesSavedCSVSchedule() throws Exception {
       List<FridayLunch> loadedSchedule = CSVHelper.createScheduleFromCSV(scheduleCSV);
       Apprentice rabea = new Apprentice("Rabea");
       loadedSchedule.get(1).assignApprentice(rabea);
       CSVHelper.saveRotaToCSV(loadedSchedule, scheduleWriteCSV);
       List<FridayLunch> result = CSVHelper.createScheduleFromCSV(scheduleWriteCSV);
       assertEquals("Mollie", result.get(0).getApprentice().get().getName());
       assertEquals("Rabea", result.get(1).getApprentice().get().getName());
       assertEquals("Rabea", result.get(2).getApprentice().get().getName());
       assertEquals("Priya", result.get(3).getApprentice().get().getName());
   }

    @Test
    public void createsEmployeesFromCSV() throws IOException {
        List<Employee> employees = CSVHelper.createEmployeesFromCSV(employeesCSV);
        assertEquals("Peter", employees.get(0).getName());
        assertEquals("George", employees.get(1).getName());
        assertEquals("Lisa", employees.get(2).getName());
    }

    @Test
    public void updatesSavedCSVEmployees() throws Exception {
        List<Employee> loadedEmployees = CSVHelper.createEmployeesFromCSV(employeesCSV);
        loadedEmployees.get(1).addOrder("Peri Peri Egg");
        CSVHelper.saveEmployeesToCSV(loadedEmployees, employeesWriteCSV);
        List<Employee> result = CSVHelper.createEmployeesFromCSV(employeesWriteCSV);
        assertEquals("Peri Peri Egg", result.get(1).getOrder().get());
    }

    private String getAbsolutePathOfResource(String name) {
        try {
            return new File(getClass().getClassLoader().getResource(name).toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Resource not found");
        }
    }
}
