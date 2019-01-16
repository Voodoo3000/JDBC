package com.epam.jpm;

import com.epam.jpm.dao.DaoProcedure;
import com.epam.jpm.executor.SQLCommandExecutor;
import org.apache.log4j.Logger;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class);

    public static void main(String[] args) {
        DaoProcedure procedure = new DaoProcedure();
        SQLCommandExecutor executor = new SQLCommandExecutor();
       /* Car car = new Car();
        car.setManufacturer("Toyota");
        car.setModel("Mark II");
        car.setColor("White");
        car.setTransmission(Car.Transmission.AT);
        car.setYear(1997);
        car.setValue(3.0);

        procedure.callStoredProcedureAddCar(car);*/

       /*LOGGER.info(procedure.callStoredProcedureGetAllCar());

       LOGGER.info(procedure.callStoredProcedureGetCarById(3));*/

       String addCarStoredProcedure = "CREATE PROCEDURE add_car(IN carManufacturer VARCHAR(255), IN carModel VARCHAR(255), " +
                  "IN colorOfCar VARCHAR(20), IN carTransmission VARCHAR(10), IN yearOfCar INT, IN engineValue DOUBLE) " +
                  "BEGIN " +
                  " INSERT INTO CAR (MANUFACTURER, MODEL, COLOR, TRANSMISSION, YEAR, VALUE) VALUES (carManufacturer, carModel, colorOfCar, carTransmission, yearOfCar, engineValue); " +
                  "END ";

       String getAllCarStoredProcedure = "CREATE PROCEDURE get_all_car() " +
                  "BEGIN " +
                  " SELECT * FROM Car; " +
                  "END ";

       String getByCarIdStoredProcedure = "CREATE PROCEDURE get_car_by_id(IN carId INT) " +
                    "BEGIN " +
                    " SELECT * FROM Car WHERE ID = carId; " +
                    "END ";

       String dropAddCarStoredProcedure = "DROP PROCEDURE add_car";
       String dropGetAllCarStoredProcedure = "DROP PROCEDURE get_all_car";
       String dropGetCarByIdStoredProcedure = "DROP PROCEDURE get_car_by_id";

       executor.createAndDropProcedureInDB(addCarStoredProcedure);
    }
}
