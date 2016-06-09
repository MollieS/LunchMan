import LunchManCore.Apprentice;
import org.junit.Test;
import services.CSVHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVHelperTest {

    @Test
    public void createsApprenticesFromListOfNames() {
        List<Apprentice> apprentices = new ArrayList<>();
        String[] name = {"Mollie"};
        String[] name2 = {"Nick"};
        List<String[]> names = Arrays.asList(name, name2);
        List<Apprentice> newApprentices = CSVHelper.createApprentices(apprentices, names);
        assertEquals("Mollie", newApprentices.get(0).getName());
    }

    @Test
    public void createsListOfNamesFromCSVOfNames() throws IOException {
        String mockApprenticesCSV = "/Users/molliestephenson/Java/LunchMan/test/mockApprentices.csv";
        List<String[]> result = CSVHelper.loadListOfNamesFromCSV(mockApprenticesCSV);
        assertEquals(result.get(0)[0], "Mollie");
        assertEquals(result.get(1)[0], "Nick");
        assertEquals(result.get(2)[0], "Ced");
        assertEquals(result.get(3)[0], "Priya");
        assertEquals(result.get(4)[0], "Rabea");
    }

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
}
