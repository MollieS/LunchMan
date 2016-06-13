import LunchManCore.Apprentice;
import LunchManCore.FridayLunch;
import org.junit.Test;
import services.CSVHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVHelperTest {

    @Test
    public void createsApprenticesFromListOfNamesInCSV() throws Exception {
        String mockApprenticesCSV = "/Users/molliestephenson/Java/LunchMan/test/mockApprentices.csv";
        List<Apprentice> result = CSVHelper.createApprenticesFromCSV(new ArrayList<>(), mockApprenticesCSV);
        assertEquals("Mollie", result.get(0).getName());
        assertEquals("Nick", result.get(1).getName());
        assertEquals("Ced", result.get(2).getName());
        assertEquals("Priya", result.get(3).getName());
        assertEquals("Rabea", result.get(4).getName());
    }

    @Test
    public void createsRotaFromCSV() throws Exception {
        String mockSchedule = "/Users/molliestephenson/Java/LunchMan/test/mockSchedule.csv";
        List<FridayLunch> result = CSVHelper.createScheduleFromCSV(new ArrayList<>(), mockSchedule);
        assertEquals("Mollie", result.get(0).getApprentice().get().getName());
        assertEquals("Nick", result.get(1).getApprentice().get().getName());
        assertEquals("Ced", result.get(2).getApprentice().get().getName());
        assertEquals("Priya", result.get(3).getApprentice().get().getName());
    }

   @Test
   public void updatesSavedCSVSchedule() throws Exception {
       String mockSchedule = "/Users/molliestephenson/Java/LunchMan/test/mockSchedule.csv";
       String mockWriteCSV = "/Users/molliestephenson/Java/LunchMan/test/mockWriteCSV.csv";
       List<FridayLunch> loadedSchedule = CSVHelper.createScheduleFromCSV(new ArrayList<>(), mockSchedule);
       Apprentice rabea = new Apprentice("Rabea");
       loadedSchedule.get(1).assignApprentice(rabea);
       CSVHelper.saveRotaToCSV(loadedSchedule, mockWriteCSV);
       List<FridayLunch> result = CSVHelper.createScheduleFromCSV(new ArrayList<>(), mockWriteCSV);
       assertEquals("Mollie", result.get(0).getApprentice().get().getName());
       assertEquals("Rabea", result.get(1).getApprentice().get().getName());
       assertEquals("Ced", result.get(2).getApprentice().get().getName());
       assertEquals("Priya", result.get(3).getApprentice().get().getName());
   }
}
