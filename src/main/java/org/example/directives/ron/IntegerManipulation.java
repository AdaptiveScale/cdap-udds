package org.example.directives.ron;
import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.parser.*;
import org.example.directives.utils.IntegerManipulationUtil;

import java.util.List;
@Plugin(type = Directive.TYPE)
@Name(IntegerManipulation.DIRECTIVE_NAME)
@Description("Manipulates integer")
public class IntegerManipulation implements Directive {
    public static final String DIRECTIVE_NAME = "int-manipulation";
    private ColumnName column;
    private Integer value;
    private String operator;

    @Override
    public UsageDefinition define() {

        UsageDefinition.Builder builder = UsageDefinition.builder(DIRECTIVE_NAME);
        builder.define("column", TokenType.COLUMN_NAME);
        builder.define("args", TokenType.TEXT_LIST);
        return builder.build();
    }
    @Override
    public void initialize(Arguments arguments) throws DirectiveParseException {
        column = arguments.value("column");

        TextList args = arguments.value("args");
        if(args.value().isEmpty() || args.value().size() < 2)
            throw new DirectiveParseException(String.format("2 args expected, %s provided.", args.value().size()));
        operator = args.value().get(0);
        String value = args.value().get(1);
        try {
            this.value = Integer.parseInt(value);
        } catch (NumberFormatException e){
            throw new DirectiveParseException("Value should be a number");
        }


    }
    @Override
    public List<Row> execute(List<Row> rows , ExecutorContext context) {

        return IntegerManipulationUtil.execute(rows, column, operator, value);

    }
    @Override
    public void destroy() {

    }
}
