package org.example.directives.ron;

import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.RecipePipeline;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.test.TestingRig;
import io.cdap.wrangler.test.api.TestRecipe;
import io.cdap.wrangler.test.api.TestRows;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DuplicateColumnTest {
    @Test
    public void testDuplicateColumn() throws Exception {
        TestRecipe recipe = new TestRecipe();
        recipe.add("parse-as-csv :body ',';");
        recipe.add("drop :body");
        recipe.add("set-headers :a,:b,:c;");
        recipe.add("duplicate-column :a 'd'");

        TestRows rows = new TestRows();
        rows.add(new Row("body", "root,joltie,mars avenue"));
        rows.add(new Row("body", "joltie,root,venus blvd"));

        RecipePipeline pipeline = TestingRig.pipeline((Class<? extends Directive>) DuplicateColumn.class, recipe);
        List<Row> actual = pipeline.execute(rows.toList());

        Assert.assertEquals(2, actual.size());
        Assert.assertEquals("root", actual.get(0).getValue("d"));
        Assert.assertEquals("joltie", actual.get(1).getValue("d"));
    }
}
