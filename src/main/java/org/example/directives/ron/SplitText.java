package org.example.directives.ron;

import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.parser.*;

import java.util.List;


@Plugin(type = Directive.TYPE)
@Name(SplitText.DIRECTIVE_NAME)
@Description("Splitting the text..")
public final class SplitText implements Directive {
    public static final String DIRECTIVE_NAME = "split-text";
    private String column;
    private String regex;


    @Override
    public UsageDefinition define() {

        UsageDefinition.Builder builder = UsageDefinition.builder(DIRECTIVE_NAME);
        builder.define("column", TokenType.COLUMN_NAME);
        builder.define("regex", TokenType.TEXT);
        return builder.build();
    }

    @Override
    public void initialize(Arguments args) {

        column = ((ColumnName) args.value("column")).value();
        regex = ((Text) args.value("regex")).value();
    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) {
        for (Row row : rows) {
            int idx = row.find(column);
            if (idx != -1) {
                Object object = row.getValue(idx);

                if (object instanceof String) {
                    String value = (String) object;
                    row.add("New Column 1",value.split(regex,2)[0]);
                    row.add("New Column 2",value.split(regex,2)[1]);
                }
            }
        }

        return rows;
    }

    @Override
    public void destroy() {

    }
}
