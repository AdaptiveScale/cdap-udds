package org.example.directives.jasir;

import io.cdap.wrangler.api.RecipePipeline;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.test.TestingRig;
import io.cdap.wrangler.test.api.TestRecipe;
import io.cdap.wrangler.test.api.TestRows;
import org.example.directives.utils.RowSizeUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class RowSizeTest {

    @Test
    public void testRowSize() throws Exception {
        TestRecipe recipe = new TestRecipe();
        recipe.add("parse-as-csv :body ',';");
        recipe.add("drop :body");
        recipe.add("set-headers :a,:b,:c;");
        recipe.add("row-size");

        TestRows rows = new TestRows();
        rows.add(new Row("body", "1,joltie,25"));
        rows.add(new Row("body", "3,joltie,31"));

        RecipePipeline pipeline = TestingRig.pipeline(RowSize.class, recipe);
        List<Row> actual = pipeline.execute(rows.toList());


        int strSize = RowSizeUtil.getStringSize("1");
        strSize += RowSizeUtil.getStringSize("joltie");
        strSize += RowSizeUtil.getStringSize("25");



        Assert.assertEquals(strSize, actual.get(0).getValue("size"));
    }
}
