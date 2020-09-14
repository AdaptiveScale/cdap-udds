package org.example.directives.utils;

import io.cdap.wrangler.api.Row;

import java.util.List;

public class FilterEmptyUtil {
    public static List<Row> execute(List<Row> rows, String column, String type, String fillValue) {
        for (int i = 0; i < rows.size(); i++) {
            int idx = rows.get(i).find(column);
            if (idx != -1) {
                Object object = rows.get(i).getValue(idx);
                if (object instanceof String)
                    if (object.equals("")) {
                        if (type.equalsIgnoreCase("keep"))
                            rows.get(i).addOrSet(column, fillValue);
                        else {
                            rows.remove(rows.get(i));
                            i--;
                        }
                    }
            }
        }

        return rows;
    }
}
