package com.epam.jpm.dao;

import com.epam.jpm.connection.MyConnection;
import com.epam.jpm.entity.Car;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoProcedure {
    private static final Logger LOGGER = Logger.getLogger(DaoProcedure.class);
    private MyConnection connection = MyConnection.getInstance();
    private CallableStatement procedure;

    public void callStoredProcedureAddCar(Car car) {
        try {
            procedure = connection.getConnection().prepareCall("{ call add_car(?, ?, ?, ?, ?, ?) }");
            procedure.setString(1, car.getManufacturer());
            procedure.setString(2, car.getModel());
            procedure.setString(3, car.getColor());
            procedure.setString(4, String.valueOf(car.getTransmission()));
            procedure.setInt(5, car.getYear());
            procedure.setDouble(6, car.getValue());
            procedure.execute();
        } catch (SQLException e) {
            LOGGER.error("Call car adding or updating stored procedure SQLException", e);
        }
    }

    public List<Car> callStoredProcedureGetAllCar() {
        List<Car> cars = new ArrayList<>();
        Car car;
        try {
            procedure = connection.getConnection().prepareCall("{ call get_all_car() }");
            procedure.execute();
            while (procedure.getResultSet().next()) {
                car = new Car();
                car.setId(procedure.getResultSet().getInt("ID"));
                car.setManufacturer(procedure.getResultSet().getString("MANUFACTURER"));
                car.setModel(procedure.getResultSet().getString("MODEL"));
                car.setColor(procedure.getResultSet().getString("COLOR"));
                car.setTransmission(Car.Transmission.valueOf(procedure.getResultSet().getString("TRANSMISSION")));
                car.setYear(procedure.getResultSet().getInt("YEAR"));
                car.setValue(procedure.getResultSet().getDouble("VALUE"));
                cars.add(car);
            }
        } catch (SQLException e) {
            LOGGER.error("Get all car stored procedure SQLException", e);
        }
        return cars;
    }

    public Car callStoredProcedureGetCarById(int carId) {
        Car car = null;
        try {
            procedure = connection.getConnection().prepareCall("{ call get_car_by_id(?) }");
            procedure.setInt(1, carId);
            procedure.execute();
            if (procedure.getResultSet().next()) {
                car = new Car();
                car.setId(procedure.getResultSet().getInt("ID"));
                car.setManufacturer(procedure.getResultSet().getString("MANUFACTURER"));
                car.setModel(procedure.getResultSet().getString("MODEL"));
                car.setColor(procedure.getResultSet().getString("COLOR"));
                car.setTransmission(Car.Transmission.valueOf(procedure.getResultSet().getString("TRANSMISSION")));
                car.setYear(procedure.getResultSet().getInt("YEAR"));
                car.setValue(procedure.getResultSet().getDouble("VALUE"));
            }
        } catch (SQLException e) {
            LOGGER.error("Get car by id stored procedure SQLException", e);
        }
        return car;
    }
}
