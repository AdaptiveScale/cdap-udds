package org.example.directives.jasir;

import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.parser.UsageDefinition;

import java.text.DecimalFormat;
import java.util.List;

@Plugin(type = Directive.TYPE)
@Name(RowValidator.DIRECTIVE_NAME)
@Description("Calculate % of row completion")
public class RowValidator implements Directive {
    public static final String DIRECTIVE_NAME = "row-validator";

    @Override
    public UsageDefinition define() {
        // Usage : row-validator
        UsageDefinition.Builder builder = UsageDefinition.builder(DIRECTIVE_NAME);
        return builder.build();
    }

    @Override
    public void initialize(Arguments arguments) throws DirectiveParseException {

    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext executorContext) throws DirectiveExecutionException, ErrorRowException, ReportErrorAndProceed {

        for (Row row : rows){
            List<Pair<String, Object>> fields = row.getFields();
            int fieldLen = fields.size();
            double emptyFields = 0;
            for (Pair<String, Object> field : fields)
            {
                if(!field.getSecond().equals(""))
                    emptyFields += 1;
            }

            DecimalFormat df = new DecimalFormat("0.00");
            double completion = Double.parseDouble(df.format((emptyFields * 100) / fieldLen));

            row.addOrSet("completion", completion);
        }
        return rows;
    }

    @Override
    public void destroy() {

    }
}
