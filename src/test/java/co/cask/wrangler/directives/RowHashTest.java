/*
 *  Copyright © 2017 Cask Data, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */

package co.cask.wrangler.directives;

import co.cask.wrangler.api.RecipePipeline;
import co.cask.wrangler.api.Row;
import co.cask.wrangler.test.TestingRig;
import co.cask.wrangler.test.api.TestRecipe;
import co.cask.wrangler.test.api.TestRows;
import org.example.directives.RowHash;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * Tests {@link RowHash}
 */
public class RowHashTest {
  @Ignore
  @Test
  public void testBasicRowHash() throws Exception {
    TestRecipe recipe = new TestRecipe();
    recipe.add("parse-as-csv :body ',';");
    recipe.add("set-headers :a,:b,:c;");
    recipe.add("drop :body;");
    recipe.add("row-hash :hash md5");

    TestRows rows = new TestRows();
    rows.add(new Row("body", "root,joltie,mars avenue"));
    rows.add(new Row("body", "joltie,root,venus blvd"));

    RecipePipeline pipeline = TestingRig.pipeline(RowHash.class, recipe);
    List<Row> actual = pipeline.execute(rows.toList());

    Assert.assertEquals(2, actual.size());
  }
}