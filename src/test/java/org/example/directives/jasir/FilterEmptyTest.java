package org.example.directives.jasir;

import io.cdap.wrangler.api.RecipePipeline;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.test.TestingRig;
import io.cdap.wrangler.test.api.TestRecipe;
import io.cdap.wrangler.test.api.TestRows;
import org.example.directives.jasir.FilterEmpty;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class FilterEmptyTest {

    @Test
    public void testFilterEmpty() throws Exception {
        TestRecipe recipe = new TestRecipe();
        recipe.add("parse-as-csv :body ',';");
        recipe.add("drop :body;");
        recipe.add("set-headers :a,:b,:c;");
        recipe.add("filter-empty :b 'keep','empty';");

        TestRows rows = new TestRows();
        rows.add(new Row("body", "root,1,mars avenue"));
        rows.add(new Row("body", "joltie,,venus blvd"));
        rows.add(new Row("body", "joltie,hello,venus blvd"));
        RecipePipeline pipeline = TestingRig.pipeline(FilterEmpty.class, recipe);
        List<Row> actual = pipeline.execute(rows.toList());

        Assert.assertEquals("empty", actual.get(1).getValue("b"));
    }
}
