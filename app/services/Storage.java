package services;

import LunchManCore.*;

import java.util.List;

public interface Storage {

    List<FridayLunch> getSchedule();

    List<Apprentice> getApprentices() ;

    List<Employee> getEmployees();

    List<Restaurant> getRestaurants();

    void saveEmployees(List<Employee> employees);

    void saveSchedule(List<FridayLunch> lunches);

}
