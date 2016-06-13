import LunchManCore.FridayLunch;
import controllers.HomeController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import services.CSVHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.*;
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
    private String restaurantCSV;
    private List<FridayLunch> loadedSchedule;
    private HomeController homeController;

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
        restaurantCSV = "/Users/molliestephenson/Java/LunchMan/test/mockRestaurants.csv";
        loadedSchedule = createScheduleFromCSV(scheduleCSV);
        homeController = new HomeController(apprenticeCSV, scheduleCSV, restaurantCSV);


    }

    @After
    public void tearDown() throws Exception {
        CSVHelper.saveRotaToCSV(loadedSchedule, scheduleCSV);
    }

    @Test
    public void indexPage() {
        Result result = homeController.index();
        assertTrue(contentAsString(result).contains("LunchMan"));
    }

    @Test
    public void indexPageShowsApprenticeNames() {
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

    @Test
    public void canAssignAMenuToAFridayLunch() throws Exception {
        Map form = new HashMap<String, String>();
        form.put("restaurant", "2");
        Result result = homeController.index();
        assertTrue(contentAsString(result).contains("Please choose a menu:"));
        Result postResult = invokeWithContext(Helpers.fakeRequest().bodyForm(form),
                () -> homeController.assignMenu());
        assertEquals(SEE_OTHER, postResult.status());
        assertEquals("/", postResult.header("Location").get());
        Result finalCheck = homeController.index();
        assertTrue(contentAsString(finalCheck).contains("Deliveroo"));
    }

    @Test
    public void canAddAnOrderToAFridayLunch() throws Exception {
        Map form = new HashMap<String, String>();
        form.put("name", "Nick");
        form.put("order", "Peri Peri Chicken");
        Result result = homeController.index();
        assertTrue(contentAsString(result).contains("Please add your order:"));
        Result postResult = invokeWithContext(Helpers.fakeRequest().bodyForm(form),
                () -> homeController.assignMenu());
        assertEquals(SEE_OTHER, postResult.status());
        assertEquals("/", postResult.header("Location").get());
        Result finalCheck = homeController.index();
        assertTrue(contentAsString(finalCheck).contains("Peri Peri Chicken"));
    }

}
