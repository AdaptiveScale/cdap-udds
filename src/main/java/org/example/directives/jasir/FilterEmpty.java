package org.example.directives.jasir;

import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.parser.*;
import org.example.directives.utils.FilterEmptyUtil;

import java.util.List;

@Plugin(type = Directive.TYPE)
@Name(FilterEmpty.DIRECTIVE_NAME)
@Description("Filter empty fields!")
public class FilterEmpty implements Directive {
    public static final String DIRECTIVE_NAME = "filter-empty";
    private String column;
    private String type;
    private String fillValue;


    @Override
    public UsageDefinition define() {
        UsageDefinition.Builder builder = UsageDefinition.builder(DIRECTIVE_NAME);
        builder.define("column", TokenType.COLUMN_NAME);
        builder.define("args", TokenType.TEXT_LIST, Optional.TRUE);
        return builder.build();
    }

    @Override
    public void initialize(Arguments arguments) throws DirectiveParseException {
        column = ((ColumnName) arguments.value("column")).value();
        TextList args = arguments.value("args");

        if(args != null)
            if (args.value().size() == 2) {
                String type = args.value().get(0);

                if (type.equalsIgnoreCase("keep"))
                    this.type = type;
                else throw new DirectiveParseException(
                        String.format("Invalid filtering type \"%s\" specified. Allowed only \"keep\" or empty!", type)
                );

                String fillValue = args.value().get(1);
                if (fillValue.equals("")) {
                    throw new DirectiveParseException(
                            String.format("Invalid value '%s' specified.", fillValue)
                    );
                }
                this.fillValue = fillValue;
            }

    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext executorContext) throws DirectiveExecutionException, ErrorRowException, ReportErrorAndProceed {
        if(type != null)
            return FilterEmptyUtil.filterEmptyKeep(rows, column, type, fillValue);
        return FilterEmptyUtil.filterEmpty(rows, column);
    }

    @Override
    public void destroy() {

    }
}
