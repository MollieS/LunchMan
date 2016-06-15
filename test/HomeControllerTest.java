import LunchManCore.FridayLunch;
import controllers.HomeController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.*;
import play.api.Play;
import play.inject.ApplicationLifecycle;
import play.inject.ConfigurationProvider;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import services.CSVHelper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.*;
import static services.CSVHelper.createScheduleFromCSV;


public class HomeControllerTest extends WithApplication{

    private String scheduleCSV;
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
        scheduleCSV = getAbsolutePathOfResource("schedule.csv");
        loadedSchedule = createScheduleFromCSV(scheduleCSV);
        homeController = new HomeController();
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
        Result finalCheck = homeController.index();
        assertTrue(contentAsString(finalCheck).contains("Ced"));
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
        Map restaurantForm = new HashMap<String, String>();
        restaurantForm.put("restaurant", "2");
        Result homePage = homeController.index();
        Result postResult = invokeWithContext(Helpers.fakeRequest().bodyForm(restaurantForm),
                () -> homeController.assignMenu());
        Map form = new HashMap<String, String>();
        form.put("name", "1");
        form.put("order", "Peri Peri Chicken");
        Result result = homeController.index();
        assertTrue(contentAsString(result).contains("Please add your order:"));
        Result orderResult = invokeWithContext(Helpers.fakeRequest().bodyForm(form),
                () -> homeController.newOrder());
        assertEquals(SEE_OTHER, orderResult.status());
        assertEquals("/", orderResult.header("Location").get());
        Result finalCheck = homeController.index();
        assertTrue(contentAsString(finalCheck).contains("Peri Peri Chicken"));
    }

    private String getAbsolutePathOfResource(String name) {
        try {
            return new File(getClass().getClassLoader().getResource(name).toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Resource not found");
        }
    }

}
