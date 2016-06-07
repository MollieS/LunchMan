import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.HomeController;
import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;

import static play.test.Helpers.*;
import static org.junit.Assert.*;


/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class HomeControllerTest {

    @Test
    public void indexPage() {
        HomeController homeController = new HomeController();
        Result result = homeController.index();
        assertTrue(contentAsString(result).contains("LunchMan"));
    }

    @Test
    public void indexPageShowsApprenticeNames() {
        HomeController homeController = new HomeController();
        Result result = homeController.index();
        assertTrue(contentAsString(result).contains("Priya"));
        assertTrue(contentAsString(result).contains("Mollie"));
        assertTrue(contentAsString(result).contains("Nick"));
        assertTrue(contentAsString(result).contains("Rabea"));
        assertTrue(contentAsString(result).contains("Ced"));
    }
    
}
