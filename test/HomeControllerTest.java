import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import LunchManCore.FridayLunch;
import controllers.HomeController;
import org.junit.*;

import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.*;
import play.test.*;
import services.CSVHelper;

import static play.test.Helpers.*;
import static org.junit.Assert.*;
import static services.CSVHelper.createScheduleFromCSV;


/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class HomeControllerTest extends WithApplication{

    private String apprenticeCSV;
    private String scheduleCSV;
    private List<FridayLunch> loadedSchedule;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .configure("play.http.router", "router.Routes")
                .build();
    }

    @Before
    public void setUp() throws IOException {
        apprenticeCSV = "/Users/molliestephenson/Java/LunchMan/test/mockApprentices.csv";
        scheduleCSV = "/Users/molliestephenson/Java/LunchMan/test/mockSchedule.csv";
        loadedSchedule = createScheduleFromCSV(new ArrayList<>(), scheduleCSV);

    }

    @After
    public void tearDown() throws Exception {
        CSVHelper.saveRotaToCSV(loadedSchedule, scheduleCSV);
    }

    @Test
    public void indexPage() {
        HomeController homeController = new HomeController(apprenticeCSV, scheduleCSV);
        Result result = homeController.index();
        assertTrue(contentAsString(result).contains("LunchMan"));
    }

    @Test
    public void indexPageShowsApprenticeNames() {
        HomeController homeController = new HomeController(apprenticeCSV, scheduleCSV);
        Result result = homeController.index();
        assertTrue(contentAsString(result).contains("Priya"));
        assertTrue(contentAsString(result).contains("Mollie"));
        assertTrue(contentAsString(result).contains("Nick"));
        assertTrue(contentAsString(result).contains("Rabea"));
    }

    @Test
    public void canChangeShownApprenticeForADay() throws Exception {
        Map form = new HashMap<String, String>();
        form.put("position", "0");
        form.put("newName", "Ced");
        HomeController homeController = new HomeController(apprenticeCSV, scheduleCSV);
        Result result = homeController.index();
        assertTrue(contentAsString(result).contains("Mollie"));
        assertTrue(contentAsString(result).contains("Nick"));
        assertTrue(contentAsString(result).contains("Rabea"));
        assertTrue(contentAsString(result).contains("Priya"));
        Result postResult = invokeWithContext(Helpers.fakeRequest().bodyForm(form),
                () -> homeController.changeSchedule());
        assertEquals(SEE_OTHER, postResult.status());
        assertEquals("/", postResult.header("Location").get());
        Result finalCheck = homeController.index();
        assertTrue(contentAsString(finalCheck).contains("Ced"));
        assertTrue(contentAsString(finalCheck).contains("Nick"));
        assertTrue(contentAsString(finalCheck).contains("Rabea"));
        assertTrue(contentAsString(finalCheck).contains("Priya"));
    }

}
