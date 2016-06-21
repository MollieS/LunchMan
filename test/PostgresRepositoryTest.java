import LunchManCore.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolution;
import play.db.evolutions.Evolutions;
import services.PostgresRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PostgresRepositoryTest {


    private Storage storage;
    private Database db;
    private List<Employee> loadedEmployees;
    private List<FridayLunch> loadedSchedule;
    private List<Guest> loadedGuests;

    @Before
    public void setUp() throws SQLException {
        db = Databases.createFrom("org.postgresql.Driver", "jdbc:postgresql://localhost/lunchman_test");
        storage = new PostgresRepository(db);
        Evolutions.applyEvolutions(db, Evolutions.forDefault(new Evolution(
                1,
                "create table apprentices (name varchar(255));" +
                "create table schedule (date text, apprentice varchar(255), restaurant varchar(255), menulink varchar(255));" +
                "create table restaurants (name varchar(255), menulink varchar(255));" +
                "create table employees (name varchar(255), foodorder varchar(255));" +
                "create table guests (name varchar(255), foodorder varchar(255) NOT NULL);",
                "drop table apprentices;" +
                "drop table schedule;" +
                "drop table restaurants;" +
                "drop table employees;" +
                "drop table guests;"
        )));
        Connection connection = db.getConnection();
        connection.prepareStatement("INSERT INTO apprentices (name) VALUES ('Mollie'), ('Nick');").execute();
        connection.prepareStatement("INSERT INTO schedule (date, apprentice) VALUES ('2016-06-17', 'Mollie'), ('2016-06-24', 'Nick');").execute();
        connection.prepareStatement("INSERT INTO employees (name) VALUES ('Mateu'), ('skim');").execute();
        connection.prepareStatement("INSERT INTO restaurants (name, menulink) VALUES ('Bahn Mi Bay', 'http://www.banhmibay.co.uk'), ('Chillango', 'https://order.chilango.co.uk');").execute();
        connection.prepareStatement("INSERT INTO guests (name, foodorder) VALUES ('Tom', 'Pizza');").execute();

    }

    @After
    public void tearDown() {
        Evolutions.cleanupEvolutions(db);
        db.shutdown();
    }

    @Test
    public void createsApprenticesFromListOfNamesInStorage() throws Exception {
        List<Apprentice> result = storage.getApprentices();
        assertEquals("Mollie", result.get(0).getName());
        assertEquals("Nick", result.get(1).getName());
    }

    @Test
    public void createsRotaFromStorage() throws Exception {
        List<FridayLunch> result = storage.getSchedule();
        assertEquals("Mollie", result.get(0).getApprentice().get().getName());
        assertEquals("Nick", result.get(1).getApprentice().get().getName());
    }

    @Test
    public void createsRestaurantsFromStorage() throws Exception {
        List<Restaurant> result = storage.getRestaurants();
        assertEquals("Bahn Mi Bay", result.get(0).getName());
        assertEquals("Chillango", result.get(1).getName());
    }

    @Test
    public void createsEmployeesFromStorage() {
        List<Employee> employees = storage.getEmployees();
        assertEquals("Mateu", employees.get(0).getName());
    }

    @Test
    public void createsGuestsFromStorage() throws IOException {
        List<Guest> guests = storage.getGuests();
        assertEquals("Tom", guests.get(0).getName());
    }

    @Test
    public void updatesSavedSchedule() throws Exception {
        List<FridayLunch> schedule = storage.getSchedule();
        Apprentice rabea = new Apprentice("Rabea");
        schedule.get(1).assignApprentice(rabea);
        storage.saveSchedule(schedule);
        List<FridayLunch> result = storage.getSchedule();
        assertEquals("Rabea", result.get(1).getApprentice().get().getName());
    }


    @Test
    public void updatesSavedEmployees() throws Exception {
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
