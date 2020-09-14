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

package org.example.directives.ron;

import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.Text;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.parser.UsageDefinition;

import java.util.List;


@Plugin(type = Directive.TYPE)
@Name(DuplicateColumn.DIRECTIVE_NAME)
@Description("Duplicates column with new name.")
public final class DuplicateColumn implements Directive {
    public static final String DIRECTIVE_NAME = "duplicate-column";
    private String column;
    private String name;

    @Override
    public UsageDefinition define() {

        UsageDefinition.Builder builder = UsageDefinition.builder(DIRECTIVE_NAME);
        builder.define("column", TokenType.COLUMN_NAME);
        builder.define("name", TokenType.TEXT);
        return builder.build();
    }

    @Override
    public void initialize(Arguments args) {

        column = ((ColumnName) args.value("column")).value();
        name = ((Text) args.value("name")).value();
    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) {
        for (Row row : rows) {
            int idx = row.find(column);
            if (idx != -1) {
                Object object = row.getValue(idx);
                row.addOrSet(name,object);
            }
        }
        return rows;
    }

    @Override
    public void destroy() {

    }
}
