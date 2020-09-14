package org.example.directives.jasir;

import io.cdap.wrangler.api.RecipePipeline;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.test.TestingRig;
import io.cdap.wrangler.test.api.TestRecipe;
import io.cdap.wrangler.test.api.TestRows;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class RoundNumberTest {
    @Test
    public void testRoundNumber() throws Exception {
        TestRecipe recipe = new TestRecipe();
        recipe.add("parse-as-csv :body ',';");
        recipe.add("set-headers :a,:b,:c;");
        recipe.add("drop :body;");
        recipe.add("round-numbers :c");

        TestRows rows = new TestRows();
        rows.add(new Row("body", "root,1.6,mars avenue"));
        rows.add(new Row("body", "joltie,1.3,venus blvd"));
        rows.add(new Row("body", "joltie,3.4,venus blvd"));

        RecipePipeline pipeline = TestingRig.pipeline(RoundNumbers.class, recipe);
        List<Row> actual = pipeline.execute(rows.toList());

        Assert.assertEquals( "2", actual.get(0).getValue("c"));
        Assert.assertEquals("1", actual.get(1).getValue("c"));
    }
}
