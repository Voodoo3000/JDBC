package com.epam.jpm.util;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Random;

public class RandomGenerator {

    public Date getRandomDate(int start, int end) {
        Random random = new Random();
        int minDay = (int) LocalDate.of(start, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(end, 1, 1).toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);
        LocalDate randomBirthDate = LocalDate.ofEpochDay(randomDay);
        Date date = java.sql.Date.valueOf(randomBirthDate);
        return date;
    }

    public int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

}
