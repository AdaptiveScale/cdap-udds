package org.example.directives.jasir;

import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.parser.*;
import org.example.directives.utils.RowsFilterUtil;

import java.util.List;

@Plugin(type = Directive.TYPE)
@Name(RowsFilter.DIRECTIVE_NAME)
@Description("Computes the length of the string.")
public class RowsFilter implements Directive {

    public static final String DIRECTIVE_NAME = "rows-filter";
    private ColumnName column;
    private String operator;
    private Integer limit;


    @Override
    public UsageDefinition define() {
        // Usage : rows-filter :column operator;
        UsageDefinition.Builder builder = UsageDefinition.builder(DIRECTIVE_NAME);
        builder.define("column", TokenType.COLUMN_NAME);
        builder.define("args", TokenType.TEXT_LIST);
        return builder.build();
    }

    @Override
    public void initialize(Arguments arguments) throws DirectiveParseException {
        column = arguments.value("column");
        TextList args = arguments.value("args");

        if (args.value().isEmpty() || args.value().size() < 2)
            throw new DirectiveParseException(String.format("2 args expected, %s provided.", args.value().size()));


        String operator = args.value().get(0);
        if (!operator.equalsIgnoreCase("smaller") && !operator.equalsIgnoreCase("bigger"))
            throw new DirectiveParseException(
                    String.format("Invalid operator '%s' specified. Allowed only \"smaller\", \"bigger\"", operator)
            );
        this.operator = operator;

        String limit = args.value().get(1);
        try {
            this.limit = Integer.parseInt(limit);
        } catch (NumberFormatException e) {
            throw new DirectiveParseException("Invalid age number specified!");
        }
    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext executorContext) throws DirectiveExecutionException, ErrorRowException, ReportErrorAndProceed {
        return RowsFilterUtil.execute(rows, column, operator, limit);
    }

    @Override
    public void destroy() {
        //no-op
    }
}
