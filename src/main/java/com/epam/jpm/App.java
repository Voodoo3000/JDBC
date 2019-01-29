package com.epam.jpm;

import com.epam.jpm.data.MetadataProvider;
import com.epam.jpm.tool.TableCopyingTool;

public class App {

    public static void main(String[] args) {
        TableCopyingTool tool = new TableCopyingTool();
        MetadataProvider provider = new MetadataProvider();

        for (String tableName : provider.getTableNames()) tool.copyTable(tableName);
    }
}
