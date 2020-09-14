package org.example.directives.utils;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.ColumnName;
import java.util.List;

public class RowsFilterUtil {

    public static List<Row> execute(List<Row> rows, ColumnName column, String operator, Integer limit) {

        if (operator.equals("bigger"))
          for (int i=0; i<rows.size(); i++){
              int idx = rows.get(i).find(column.value());
              if (idx != -1) {
                  Object object = rows.get(i).getValue(idx);
                  if(!object.equals("")) {
                      double value;
                      if (object instanceof Integer) {
                        value = (Integer) object;
                      }
                      else value = Double.parseDouble((String) object);

                      if (value < limit) {
                          rows.remove(rows.get(i));
                          i--;
                      }
                  }
              }
          }

        if (operator.equals("smaller"))
            for (int i=0; i<rows.size(); i++){
                int idx = rows.get(i).find(column.value());
                if (idx != -1) {
                    Object object = rows.get(i).getValue(idx);
                    if(!object.equals("")) {
                        double value;
                        if (object instanceof Integer) {
                            value = (Integer) object;
                        }
                        else value = Double.parseDouble((String) object);

                        if (value >= limit) {
                            rows.remove(rows.get(i));
                            i--;
                        }
                    }
                }
            }

        return rows;

    }
}
