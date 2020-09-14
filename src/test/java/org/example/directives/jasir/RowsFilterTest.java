package org.example.directives.jasir;

import io.cdap.wrangler.api.RecipePipeline;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.test.TestingRig;
import io.cdap.wrangler.test.api.TestRecipe;
import io.cdap.wrangler.test.api.TestRows;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class RowsFilterTest {

    @Test
    public void rowsFilterTest() throws Exception {
        TestRecipe recipe = new TestRecipe();
        recipe.add("parse-as-csv :body ',';");
        recipe.add("drop :body");
        recipe.add("set-headers :a,:b,:c;");
        recipe.add("rows-filter :c 'smaller','30';");

        TestRows rows = new TestRows();
        rows.add(new Row("body", "1,joltie,25"));
        rows.add(new Row("body", "3,joltie,31"));

        RecipePipeline pipeline = TestingRig.pipeline(RowsFilter.class, recipe);
        List<Row> actual = pipeline.execute(rows.toList());

        Assert.assertEquals(1, actual.size());
    }
}
