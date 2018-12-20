package com.epam.jpm;

import com.epam.jpm.dao.CarDao;
import com.epam.jpm.entity.Car;

public class App {
    public static void main(String[] args) {
        CarDao carDao = new CarDao();
        Car car = new Car();
        car.setManufacturer("Toyota");
        car.setModel("Chaser");
        car.setColor("Green");
        car.setTransmission(Car.Transmission.AT);
        car.setYear(1999);
        car.setValue(2.5);

        carDao.add(car);

        System.out.println(carDao.getAll());
    }
}
