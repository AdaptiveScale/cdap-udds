package org.example.directives.utils;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.ColumnName;
import java.util.List;
public class ConvertCurrencyUtil {
    public static List<Row> execute(List<Row> rows, ColumnName column, String currency, String convertTo) {
        if (currency.equals("USD"))
            for (Row row : rows) {
                int idx = row.find(column.value());

                if (idx != -1 && convertTo.equals("EUR")) {
                    Object object = row.getValue(idx);
                    Integer values = (Integer) object;
                    if (object instanceof Integer) {
                        row.setValue(idx, values * 0.85);

                    }
                }
                else if (idx != -1 && convertTo.equals("CAD")) {
                    Object object = row.getValue(idx);
                    Integer values = (Integer) object;
                    if (object instanceof Integer) {
                        row.setValue(idx, values * 1.32);

                    }
                }

            }
        if (currency.equals("EUR"))
            for (Row row : rows) {
                int idx = row.find(column.value());

                if (idx != -1 && convertTo.equals("USD")) {
                    Object object = row.getValue(idx);
                    Integer values = (Integer) object;
                    if (object instanceof Integer) {
                        row.setValue(idx, values * 1.18);

                    }
                }
                if (idx != -1 && convertTo.equals("CAD")) {
                    Object object = row.getValue(idx);
                    Integer values = (Integer) object;
                    if (object instanceof Integer) {
                        row.setValue(idx, values * 1.56);

                    }
                }
            }
        if (currency.equals("CAD"))
            for (Row row : rows) {
                int idx = row.find(column.value());

                if (idx != -1 && convertTo.equals("USD")) {
                    Object object = row.getValue(idx);
                    Integer values = (Integer) object;
                    if (object instanceof Integer) {
                        row.setValue(idx, values * 0.76);

                    }
                }
                if (idx != -1 && convertTo.equals("EUR")) {
                    Object object = row.getValue(idx);
                    Integer values = (Integer) object;
                    if (object instanceof Integer) {
                        row.setValue(idx, values * 0.64);

                    }
                }
            }


        return rows;
    }


}