package org.example.directives.jasir;

import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.parser.UsageDefinition;

import java.util.List;

@Plugin(type=Directive.TYPE)
@Name(RoundNumbers.DIRECTIVE_NAME)
@Description("Round decimal numbers")
public class RoundNumbers implements Directive {
    public static final String DIRECTIVE_NAME = "round-numbers";
    private String column;

    @Override
    public UsageDefinition define() {
        UsageDefinition.Builder builder = UsageDefinition.builder(DIRECTIVE_NAME);
        builder.define("column", TokenType.COLUMN_NAME);
        return builder.build();
    }

    @Override
    public void initialize(Arguments arguments) throws DirectiveParseException {
        column = ((ColumnName) arguments.value("column")).value();
    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext executorContext) throws DirectiveExecutionException, ErrorRowException, ReportErrorAndProceed {
        for(Row row : rows) {
            int idx = row.find(column);
            if(idx != -1) {
                Object object = row.getValue(idx);
                if(object instanceof String) {
                    String value = (String) object;
                    if(!value.equals(""))
                        try {
                           double dValue = Double.parseDouble((String) object);
                           value = String.valueOf(Math.round(dValue));
                        }
                        catch (NumberFormatException e) {
                            throw new DirectiveExecutionException("Provided string cannot be converted to number");
                        }
                    row.setValue(idx, value);
                }
            }
        }
        return rows;
    }

    @Override
    public void destroy() {

    }
}
