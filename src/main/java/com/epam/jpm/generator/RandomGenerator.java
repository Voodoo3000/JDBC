package com.epam.jpm.generator;

import com.epam.jpm.data.ValueType;

import java.util.*;

public class RandomGenerator {

    /*
    * Gets random number from set range.
    */
    public int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

    /*
    * Gets random value from enum class.
    */
    private  <T extends Enum<T>> T randomEnum(Class<T> clazz) {
        final T[] enumConstants = clazz.getEnumConstants();
        final Random rnd = new Random();
        final int index = rnd.nextInt(enumConstants.length);
        return enumConstants[index];
    }

    /*
    * Generates sql string with random column quantity and random type
    */
    public String columnGenerator(int minColumnQuantity, int maxColumnQuantity) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1;  i < randBetween(minColumnQuantity, maxColumnQuantity); i++) {
            sb.append("RANDOM_COLUMN").append(i).append(" ").append(randomEnum(ValueType.class)).append(", ");
        }
        String str = sb.substring(0, sb.length()-2).replaceAll("VARCHAR", "VARCHAR(255)");
        return str;
    }

    /*
    * Provides with random size set of random and unique numbers
    */
    public Set<Integer> getSetOfRandomNumbers(int tableQuantity, int start, int end) {
        Set<Integer> setOfNumbers = new TreeSet<>();
        while(setOfNumbers.size() < tableQuantity) {
            setOfNumbers.add(randBetween(start, end));
        }
        return setOfNumbers;
    }
}
