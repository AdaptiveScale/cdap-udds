package org.example.directives.jasir;

import io.cdap.wrangler.api.RecipePipeline;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.test.TestingRig;
import io.cdap.wrangler.test.api.TestRecipe;
import io.cdap.wrangler.test.api.TestRows;
import org.junit.Assert;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.List;

public class RowValidatorTest {

    @Test
    public void testRowCompletion() throws Exception {
        TestRecipe recipe = new TestRecipe();
        recipe.add("parse-as-csv :body ',';");
        recipe.add("drop :body");
        recipe.add("set-headers :a,:b,:c;");
        recipe.add("row-validator");

        TestRows rows = new TestRows();
        rows.add(new Row("body", "1,joltie,"));

        RecipePipeline pipeline = TestingRig.pipeline(RowValidator.class, recipe);
        List<Row> actual = pipeline.execute(rows.toList());

        DecimalFormat df = new DecimalFormat("0.00");

        Assert.assertEquals(Double.parseDouble(df.format((2. * 100) / 3)), actual.get(0).getValue("completion"));
    }
}
