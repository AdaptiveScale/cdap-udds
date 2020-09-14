package org.example.directives;

import com.idealista.fpe.FormatPreservingEncryption;
import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.parser.UsageDefinition;
import org.example.directives.utils.FormatPreservingUtil;

import java.util.List;

@Plugin(type = Directive.TYPE)
@Name(FormatPreservingDecrypt.DIRECTIVE_NAME)
@Description("Format Preserving Decrypt")
public class FormatPreservingDecrypt implements Directive {

    public static final String DIRECTIVE_NAME = "fp-decrypt";

    private String column;
    private FormatPreservingEncryption formatPreservingEncryption;

    @Override
    public UsageDefinition define() {
        // Usage: fp-decrypt :column
        UsageDefinition.Builder builder = UsageDefinition.builder(DIRECTIVE_NAME);
        builder.define("column", TokenType.COLUMN_NAME);
        return builder.build();
    }

    @Override
    public void initialize(Arguments arguments) throws DirectiveParseException {
        column = ((ColumnName) arguments.value("column")).value();
        formatPreservingEncryption = FormatPreservingUtil.defaultFormatPreservingEncryption();
    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext executorContext) throws DirectiveExecutionException, ErrorRowException, ReportErrorAndProceed {
        for (Row row : rows) {
            int idx = row.find(column);
            if (idx != -1) {
                Object object = row.getValue(idx);
                String value = (String) object;
                String cipherText = formatPreservingEncryption.decrypt(value, FormatPreservingUtil.TWEAK);
                row.setValue(idx, cipherText);

            }
        }
        return rows;
    }

    @Override
    public void destroy() {
        //no-op
    }
}
