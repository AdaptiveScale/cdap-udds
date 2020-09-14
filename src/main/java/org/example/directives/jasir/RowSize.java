package org.example.directives.jasir;

import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.parser.UsageDefinition;
import org.example.directives.utils.RowSizeUtil;

import java.util.List;

@Plugin(type = Directive.TYPE)
@Name(RowSize.DIRECTIVE_NAME)
@Description("Generates the size of the entire row in bytes")
public class RowSize implements Directive {
    public static final String DIRECTIVE_NAME = "row-size";


    @Override
    public UsageDefinition define() {
        UsageDefinition.Builder builder = UsageDefinition.builder(DIRECTIVE_NAME);
        return builder.build();
    }

    @Override
    public void initialize(Arguments arguments) throws DirectiveParseException {

    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext executorContext) throws DirectiveExecutionException, ErrorRowException, ReportErrorAndProceed {
        int size = 0;
        for (Row row : rows){

            List<Pair<String, Object>> fields = row.getFields();
            for (Pair<String, Object> field : fields)
            {
                if(field.getSecond() instanceof Integer)
                    size += 4;
                else if(field.getSecond() instanceof Double)
                    size += 8;
                else
                    if(!field.getSecond().equals(""))
                        size += RowSizeUtil.getStringSize((String) field.getSecond());
            }

            row.addOrSet("size", size);
            size = 0;
        }
        return rows;
    }

    @Override
    public void destroy() {

    }
}
