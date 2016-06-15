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
        scheduleCSV = new CSVHelper().getAbsolutePathOfResource("schedule.csv");
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
        assertEquals(OK, result.status());
    }

    @Test
    public void indexPageShowsApprenticeNames() {
        Result result = homeController.index();
        assertTrue(contentAsString(result).contains("LunchMan"));
        assertTrue(contentAsString(result).contains("Priya"));
        assertTrue(contentAsString(result).contains("Mollie"));
        assertTrue(contentAsString(result).contains("Nick"));
        assertTrue(contentAsString(result).contains("Rabea"));
    }

    @Test
    public void changeApprenticeRedirectsToIndex() throws Exception {
        Map form = new HashMap<String, String>();
        form.put("position", "0");
        form.put("newName", "Ced");

        Result postResult = invokeWithContext(Helpers.fakeRequest().bodyForm(form), () -> homeController.changeSchedule());

        assertEquals(SEE_OTHER, postResult.status());
        assertEquals("/", postResult.header("Location").get());
    }

    @Test
    public void canChangeShownApprenticeForADay() throws Exception {
        Map form = new HashMap<String, String>();
        form.put("position", "0");
        form.put("newName", "Ced");

       invokeWithContext(Helpers.fakeRequest().bodyForm(form), () -> homeController.changeSchedule());

        Result result = homeController.index();
        assertTrue(contentAsString(result).contains("Ced"));
    }

    @Test
    public void asksForMenuChoice() throws Exception {
        Result result = homeController.index();
        assertTrue(contentAsString(result).contains("Please choose a menu:"));
    }

    @Test
    public void assignMenuRedirectsToIndex() throws Exception {
        Map form = new HashMap<String, String>();
        form.put("restaurant", "2");

        Result postResult = invokeWithContext(Helpers.fakeRequest().bodyForm(form),
                () -> homeController.assignMenu());

        assertEquals(SEE_OTHER, postResult.status());
        assertEquals("/", postResult.header("Location").get());
    }

    @Test
    public void canAssignAMenuToAFridayLunch() throws Exception {
        Map form = new HashMap<String, String>();
        form.put("restaurant", "2");

        invokeWithContext(Helpers.fakeRequest().bodyForm(form),
                () -> homeController.assignMenu());

        Result result = homeController.index();
        assertTrue(contentAsString(result).contains("Deliveroo"));
    }

    @Test
    public void asksForOrderChoice() throws Exception {
        chooseMenu();

        Result result = homeController.index();

        assertTrue(contentAsString(result).contains("Please add your order:"));
    }

    @Test
    public void addingOrderRedirectsToIndex() throws Exception {
        chooseMenu();
        Map form = new HashMap<String, String>();
        form.put("name", "1");
        form.put("order", "Peri Peri Chicken");

        Result orderResult = invokeWithContext(Helpers.fakeRequest().bodyForm(form),
                () -> homeController.newOrder());

        assertEquals(SEE_OTHER, orderResult.status());
        assertEquals("/", orderResult.header("Location").get());
    }


    @Test
    public void canAddAnOrderToAFridayLunch() throws Exception {
        chooseMenu();
        Map form = new HashMap<String, String>();
        form.put("name", "1");
        form.put("order", "Peri Peri Chicken");

        invokeWithContext(Helpers.fakeRequest().bodyForm(form),
                () -> homeController.newOrder());

        Result result = homeController.index();
        assertTrue(contentAsString(result).contains("Peri Peri Chicken"));
    }

    private void chooseMenu() {
        Map restaurantForm = new HashMap<String, String>();
        restaurantForm.put("restaurant", "2");
        invokeWithContext(Helpers.fakeRequest().bodyForm(restaurantForm),
                () -> homeController.assignMenu());
    }
}
