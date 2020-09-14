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
public class ConvertCurrencyTest {
    @Test
    public void testConvertCurrency() throws Exception {
        TestRecipe recipe = new TestRecipe();
        recipe.add("parse-as-csv :body ',';");
        recipe.add("drop :body");
        recipe.add("set-headers :a,:b,:c;");
        recipe.add("set-type :a int");
        recipe.add("convert-currency :a 'USD','EUR';");
        TestRows rows = new TestRows();
        rows.add(new Row("body", "1,joltie,25"));
        rows.add(new Row("body", "1,joltie,31"));

        RecipePipeline pipeline = TestingRig.pipeline((Class<? extends Directive>) ConvertCurrency.class, recipe);
        List<Row> actual = pipeline.execute(rows.toList());


        Assert.assertEquals(2, actual.size());
        Assert.assertEquals(0.85, actual.get(0).getValue("a"));
        Assert.assertEquals(0.85, actual.get(1).getValue("a"));
    }
}