package services;

import LunchManCore.*;
import com.google.inject.Inject;
import play.db.Database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PostgresRepository implements Storage {

    private Database db;

    @Inject
    public PostgresRepository(Database db) {
        this.db = db;
    }

    @Override
    public List<FridayLunch> getSchedule() {
        List<FridayLunch> schedule = new ArrayList<FridayLunch>();
        Connection con = db.getConnection();
        try {
            ResultSet lunches = con.prepareStatement("select * from schedule;").executeQuery();
            while (lunches.next()) {
                FridayLunch lunch = new FridayLunch(LocalDate.parse(lunches.getString("date")));
                lunch.assignApprentice(new Apprentice(lunches.getString("apprentice")));
                if (lunches.getString("restaurant") != null) {
                    lunch.assignRestaurant(new Restaurant(lunches.getString("restaurant"), lunches.getString("menulink")));
                }
                schedule.add(lunch);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedule;
    }

    @Override
    public List<Apprentice> getApprentices() {
        List<Apprentice> apprentices = new ArrayList<Apprentice>();
        Connection con = db.getConnection();
        try {
            ResultSet names = con.prepareStatement("select * from apprentices;").executeQuery();
            while (names.next()) {
                apprentices.add(new Apprentice(names.getString("name")));
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apprentices;
    }

    @Override
    public List<Employee> getEmployees() {
            List<Employee> employees = new ArrayList<Employee>();
        Connection con = db.getConnection();
            try {
                ResultSet names = con.prepareStatement("select * from employees;").executeQuery();
                while (names.next()) {
                    Employee employee = new Employee(names.getString("name"));
                    employee.addOrder(names.getString("foodorder"));
                    employees.add(employee);
                }
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return employees;
    }

    @Override
    public List<Restaurant> getRestaurants() {
        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        Connection con = db.getConnection();
        try {
            ResultSet names = con.prepareStatement("select * from restaurants;").executeQuery();
            while (names.next()) {
                restaurants.add(new Restaurant(names.getString("name"), names.getString("menulink")));
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    @Override
    public List<Guest> getGuests() {
        List<Guest> guests = new ArrayList<Guest>();
        Connection con = db.getConnection();
        try {
            ResultSet names = con.prepareStatement("select * from guests;").executeQuery();
            while (names.next()) {
                guests.add(new Guest(names.getString("name"), names.getString("foodorder")));
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return guests;
    }

    @Override
    public void saveGuests(List<Guest> list) {
        Connection con = db.getConnection();
        try {
            con.prepareStatement("delete from guests;").executeUpdate();
            for (Guest guest : list) {
                String updateGuestsString = "insert into guests (name, foodorder) VALUES (?, ?);";
                PreparedStatement updateGuests = con.prepareStatement(updateGuestsString);
                updateGuests.setString(1, guest.getName());
                updateGuests.setString(2, guest.getOrder());
                updateGuests.executeUpdate();
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveApprentices(List<Apprentice> list) {

    }

    @Override
    public void saveRestaurants(List<Restaurant> list) {

    }

    @Override
    public void saveSchedule(List<FridayLunch> list) {
        Connection con = db.getConnection();
        try {
            con.prepareStatement("delete from schedule;").executeUpdate();
            for (FridayLunch lunch : list) {
                String updateString = "insert into schedule (date, apprentice) VALUES (?, ?);";
                PreparedStatement updateSchedule = con.prepareStatement(updateString);
                updateSchedule.setString(1, String.valueOf(lunch.getDate()));
                updateSchedule.setString(2, lunch.getApprentice().get().getName());
                updateSchedule.executeUpdate();
                if (lunch.getRestaurant().isPresent()) {
                    String updateRestaurantString = "update schedule set restaurant = ?, menulink = ? WHERE date = ?;";
                    PreparedStatement updateScheduleRestaurant = con.prepareStatement(updateRestaurantString);
                    updateScheduleRestaurant.setString(1, lunch.getRestaurant().get().getName());
                    updateScheduleRestaurant.setString(2, lunch.getRestaurant().get().getMenuLink());
                    updateScheduleRestaurant.setString(3, String.valueOf(lunch.getDate()));
                    updateScheduleRestaurant.executeUpdate();

                }
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveEmployees(List<Employee> list) {
        Connection con = db.getConnection();
        try {
            con.prepareStatement("delete from employees;").executeUpdate();
            for (Employee employee : list) {
                String updateString = "insert into employees (name) VALUES (?);";
                PreparedStatement updateSchedule = con.prepareStatement(updateString);
                updateSchedule.setString(1, employee.getName());
                updateSchedule.executeUpdate();
                if (employee.getOrder().isPresent()) {
                    String updateOrderString = "update employees set foodorder = ? WHERE name = ?;";
                    PreparedStatement updateOrder = con.prepareStatement(updateOrderString);
                    updateOrder.setString(1, employee.getOrder().get());
                    updateOrder.setString(2, employee.getName());
                    updateOrder.executeUpdate();
                }
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
