package org.example.directives.utils;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.ColumnName;
import java.util.List;
public class IntegerManipulationUtil {
    public static List<Row> execute(List<Row> rows, ColumnName column, String operator, Integer value) {
        if (operator.equals("+"))
            for (Row row : rows) {
                int idx = row.find(column.value());

                if (idx != -1) {
                    Object object = row.getValue(idx);
                    Integer values = (Integer) object;
                    if (object instanceof Integer) {
                        row.setValue(idx, values + value);

                    }
                }
            }

        if (operator.equals("-"))
            for (Row row : rows) {
                int idx = row.find(column.value());

                if (idx != -1) {
                    Object object = row.getValue(idx);
                    Integer values = (Integer) object;
                    if (object instanceof Integer) {
                        row.setValue(idx, values - value);

                    }
                }
            }

        if (operator.equals("*"))
            for (Row row : rows) {
                int idx = row.find(column.value());

                if (idx != -1) {
                    Object object = row.getValue(idx);
                    Integer values = (Integer) object;
                    if (object instanceof Integer) {
                        row.setValue(idx, values * value);

                    }
                }
            }

        return rows;
    }


}