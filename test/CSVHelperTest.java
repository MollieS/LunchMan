import LunchManCore.Apprentice;
import LunchManCore.FridayLunch;
import LunchManCore.Restaurant;
import org.junit.Test;
import services.CSVHelper;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVHelperTest {

    @Test
    public void createsApprenticesFromListOfNamesInCSV() throws Exception {
        String mockApprenticesCSV = "/Users/molliestephenson/Java/LunchMan/test/mockApprentices.csv";
        List<Apprentice> result = CSVHelper.createApprenticesFromCSV(mockApprenticesCSV);
        assertEquals("Mollie", result.get(0).getName());
        assertEquals("Nick", result.get(1).getName());
        assertEquals("Ced", result.get(2).getName());
        assertEquals("Priya", result.get(3).getName());
        assertEquals("Rabea", result.get(4).getName());
    }

    @Test
    public void createsRotaFromCSV() throws Exception {
        String mockSchedule = "/Users/molliestephenson/Java/LunchMan/test/mockSchedule.csv";
        List<FridayLunch> result = CSVHelper.createScheduleFromCSV(mockSchedule);
        assertEquals("Mollie", result.get(0).getApprentice().get().getName());
        assertEquals("Nick", result.get(1).getApprentice().get().getName());
        assertEquals("Rabea", result.get(2).getApprentice().get().getName());
        assertEquals("Priya", result.get(3).getApprentice().get().getName());
    }

    @Test
    public void createsRestaurantsFromCSV() throws Exception {
        String mockRestaurants = "/Users/molliestephenson/Java/LunchMan/test/mockRestaurants.csv";
        List<Restaurant> result = CSVHelper.createRestaurantsFromCSV(mockRestaurants);
        assertEquals("Bahn Mi Bay", result.get(0).getName());
        assertEquals("Chillango", result.get(1).getName());
        assertEquals("Deliveroo", result.get(2).getName());
    }

   @Test
   public void updatesSavedCSVSchedule() throws Exception {
       String mockSchedule = "/Users/molliestephenson/Java/LunchMan/test/mockSchedule.csv";
       String mockWriteCSV = "/Users/molliestephenson/Java/LunchMan/test/mockWriteCSV.csv";
       List<FridayLunch> loadedSchedule = CSVHelper.createScheduleFromCSV(mockSchedule);
       Apprentice rabea = new Apprentice("Rabea");
       loadedSchedule.get(1).assignApprentice(rabea);
       CSVHelper.saveRotaToCSV(loadedSchedule, mockWriteCSV);
       List<FridayLunch> result = CSVHelper.createScheduleFromCSV(mockWriteCSV);
       assertEquals("Mollie", result.get(0).getApprentice().get().getName());
       assertEquals("Rabea", result.get(1).getApprentice().get().getName());
       assertEquals("Rabea", result.get(2).getApprentice().get().getName());
       assertEquals("Priya", result.get(3).getApprentice().get().getName());
   }
}
