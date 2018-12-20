package com.epam.jpm.dao;

import com.epam.jpm.connection.MyConnection;
import com.epam.jpm.entity.Car;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarDao {
    private static final Logger LOGGER = Logger.getLogger(CarDao.class);
    private MyConnection connection = MyConnection.getInstance();

    public List<Car> getAll() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT ID, MANUFACTURER, MODEL, COLOR, TRANSMISSION, YEAR, VALUE FROM car";
        try {
            Statement stmt = connection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Car car = new Car();
                car.setId(rs.getInt("ID"));
                car.setManufacturer(rs.getString("MANUFACTURER"));
                car.setModel(rs.getString("MODEL"));
                car.setColor(rs.getString("COLOR"));
                car.setTransmission(Car.Transmission.valueOf(rs.getString("TRANSMISSION")));
                car.setYear(rs.getInt("YEAR"));
                car.setValue(rs.getDouble("VALUE"));
                cars.add(car);
            }
            stmt.close();
            rs.close();
            connection.getConnection().close();
        }  catch (SQLException e ) {
            LOGGER.error("Get all cars SQLException", e);
        }
        return cars;
    }

    public void add(Car car) {
        String sql = "INSERT INTO car(MANUFACTURER, MODEL, COLOR, TRANSMISSION, YEAR, VALUE) VALUES(?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, car.getManufacturer());
            preparedStatement.setString(2, car.getModel());
            preparedStatement.setString(3, car.getColor());
            preparedStatement.setString(4, String.valueOf(car.getTransmission()));
            preparedStatement.setInt(5, car.getYear());
            preparedStatement.setDouble(6, car.getValue());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.getConnection().close();
        } catch (SQLException e) {
            LOGGER.error("Car adding or updating SQLException", e);
        }
    }
}
