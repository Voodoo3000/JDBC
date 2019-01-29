package com.epam.jpm;

import com.epam.jpm.data.MetadataProvider;
import com.epam.jpm.generator.RandomGenerator;
import com.epam.jpm.generator.RowGenerator;
import com.epam.jpm.generator.TableGenerator;
import org.apache.log4j.Logger;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class);
    private static final int MIN_COLUMN_QUANTITY = 5;
    private static final int MAX_COLUMN_QUANTITY = 15;

    public static void main(String[] args) {
        RandomGenerator randomGenerator = new RandomGenerator();
        TableGenerator tableGenerator = new TableGenerator();
        RowGenerator rowGenerator = new RowGenerator();
        MetadataProvider provider = new MetadataProvider();

        /*
        * Initiates creating random tables
        */
        for(int num : randomGenerator.getSetOfRandomNumbers(10,1, 50)) {
            tableGenerator.createRandomTable(num, randomGenerator.columnGenerator(MIN_COLUMN_QUANTITY, MAX_COLUMN_QUANTITY));
        }

        LOGGER.info(provider.getTableNames());
        for (String tableName : provider.getTableNames()){
            LOGGER.info(provider.getTableColumnMetadataMap(tableName));
        }

        /*
        * Initiates creating random rows
        */
        rowGenerator.createRandomRowsInTables();
    }
}
