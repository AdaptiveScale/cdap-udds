package org.example.directives.ron;
import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.parser.*;


import java.util.List;
@Plugin(type = Directive.TYPE)
@Name(ReplaceRow.DIRECTIVE_NAME)
@Description("Changes selected rows to the desired replacement.")
public class ReplaceRow implements Directive {
    public static final String DIRECTIVE_NAME = "replace-row";
    private ColumnName column;
    private String value;
    private String replacement;
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
        value = args.value().get(0);
        replacement = args.value().get(1);


    }
    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) {
        for (Row row : rows) {
            int idx = row.find(column.value());

            if (idx != -1) {
                Object object = row.getValue(idx);
                String values = (String) object;
                if (object instanceof String && !value.equals(replacement)) {

                    if (values.equals(value)) {
                        row.setValue(idx, replacement);
                    }
                }
            }
        }
        return rows;
    }
    @Override
    public void destroy() {

    }
}
