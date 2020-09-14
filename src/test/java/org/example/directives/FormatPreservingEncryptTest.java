package org.example.directives;

import io.cdap.wrangler.api.*;
import io.cdap.wrangler.test.TestingRig;
import io.cdap.wrangler.test.api.TestRecipe;
import io.cdap.wrangler.test.api.TestRows;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class FormatPreservingEncryptTest {
    @Test
    public void testEncryptUsage() throws RecipeException, DirectiveLoadException, DirectiveParseException {
        TestRecipe recipe = new TestRecipe();
        recipe.add("parse-as-csv :body ',';");
        recipe.add("drop :body");
        recipe.add("set-headers :a,:b,:c;");
        recipe.add("fp-encrypt :b");

        TestRows rows = new TestRows();
        rows.add(new Row("body", "root,joltie,mars avenue"));
        rows.add(new Row("body", "joltie,root,venus blvd"));

        RecipePipeline pipeline = TestingRig.pipeline(FormatPreservingEncrypt.class, recipe);
        List<Row> actual = pipeline.execute(rows.toList());

        Assert.assertEquals(2, actual.size());
    }

    public void testEncryptDecrypt() throws Exception {
        TestRecipe recipe = new TestRecipe();
        recipe.add("parse-as-csv :body ',';");
        recipe.add("drop :body");
        recipe.add("set-headers :a,:b,:c;");
        recipe.add("fp-encrypt :b");

        TestRows rows = new TestRows();
        rows.add(new Row("body", "root,joltie,mars avenue"));
        rows.add(new Row("body", "joltie,root,venus blvd"));

        RecipePipeline pipeline = TestingRig.pipeline(FormatPreservingEncrypt.class, recipe);
        List<Row> actual = pipeline.execute(rows.toList());

        Assert.assertEquals(5, actual.get(0).getValue("b").toString().length());
        Assert.assertNotEquals("joltie", actual.get(0).getValue("b").toString());
    }
}
