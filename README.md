#### Clone this repo to get started 

<img  alt="Not Available in Cask Market" src="https://cdap-users.herokuapp.com/assets/cm-notavailable.svg"/> [![Join CDAP community](https://cdap-users.herokuapp.com/badge.svg?t=wrangler)](https://cdap-users.herokuapp.com?t=1)

# Introduction

CDAP provides extensive support for user defined directives (UDDs) as a way to specify custom processing for DataPrep in CDAP. CDAP UDDs can currently be implemented in Java.

CDAP provides the most extensive support for Java functions. Java functions are also more efficient because they are implemented in the same language as CDAP and DataPrep and, also because additional interfaces and integrations with other CDAP subsystems are supported.

User Defined Directives, also known as UDDs, allow you to create custom functions to transform records within CDAP DataPrep a.k.a Wrangler. CDAP comes with a comprehensive library of functions. There are however some omissions, and some specific cases for which UDDs are the solution.

UDDs, similar to User-defined Functions (UDFs) have a long history of usefulness in SQL-derived languages and other data processing and query systems.  While the framework can be rich in their expressiveness, there is just no way they can anticipate all the things a developer wants to do.  Thus, the custom UDF has become commonplace in our data manipulation toolbox. In order to support customization or extension, CDAP now has the ability to build your own functions for manipulating data through UDDs.

Developing CDAP DataPrep UDDs is by no means any rocket science, and is an effective way of solving problems that could either be downright impossible, or does not meet your requirements or very akward to solve. 

## Developing UDD

There is one simple interface for developing your customized directive. The simple interface `co.cask.wrangler.api.Directive` can be used for developing user defined directive.

### Simple API

Building a UDD with the simpler UDD API involves nothing more than writing a class with three functions (evaluate) and few annotations. Here is an example:

```
@Plugin(type = UDD.Type)
@Name(SimpleUDD.NAME)
@Categories(categories = { "example", "simple" })
@Description("My first simple user defined directive")
public SimpleUDD implements Directive {
  public static final String NAME = "my-simple-udd";
  
  public UsageDefinition define() {
    ...
  }
  
  public void initialize(Arguments args) throws DirectiveParseException {
    ...
  }
  
  public List<Row> execute(List<Row> rows, RecipeContext context) throws RecipeException, ErrorRowException {
    ...
  }
}
```

### Testing a simple UDD

Because the UDD is a simple three functions class, you can test it with regular testing tools, like JUnit.

```
public class SimpleUDDTest {

  @Test
  public void testSimpleUDD() throws Exception {
    TestRecipe recipe = new TestRecipe();
    recipe("parse-as-csv :body ',';");
    recipe("drop :body;");
    recipe("rename :body_1 :simpledata;");
    recipe("!my-simple-udd ...");
    
    TestRows rows = new TestRows();
    rows.add(new Row("body", "root,joltie,mars avenue"));
    RecipePipeline pipeline = TestingRig.pipeline(RowHash.class, recipe);
    List<Row> actual = pipeline.execute(rows.toList());
  }
}
```

### Building a UDD Plugin

There is nothing much to be done here - this example repository includes a maven POM file that is pre-configured for building the directive JAR. All that a developer does is build the project using the following command. 

```
  mvn clean package
```

This would generate two files

  * Artifact - `my-simple-udd-1.0-SNAPSHOT.jar`
  * Artifact Configuration `my-simple-udd-1.0-SNAPSHOT.json`
  
### Deploying Plugin

There are multiple ways in which the custom directive can be deployed to CDAP. The two popular ways are through using CDAP CLI (command line interface) and CDAP UI.

#### CDAP CLI

In order to deploy the directive through CLI, start the CDAP CLI and use the `load artifact` command to load the plugin artifact into CDAP. 

```
$ $CDAP_HOME/bin/cdap cli
cdap > load artifact my-simple-udd-1.0-SNAPSHOT.jar config-file my-simple-udd-1.0-SNAPSHOT.json
```

#### CDAP UI
![alt text](https://github.com/hydrator/example-directive/blob/develop/docs/directive-plugin.gif "Logo Title Text 1")

## Example

We will now walk through the creation of a user defined directive(udd) called `text-reverse` that takes one argument: Column Name -- it is the name of the column in a `Row` that needs to be reversed. The resulting row will have the Column Name specified in the input have reversed string of characters.

```
 text-reverse :address
 text-reverse :id
```

Here is the implementation of the above UDD. 

```
@Plugin(type = UDD.Type)
@Name(TextReverse.NAME)
@Categories(categories = {"text-manipulation"})
@Description("Reverses the column value")
public final class TextReverse implements UDD {
  public static final String NAME = "text-reverse";
  private String column;
  
  public UsageDefinition define() {
    UsageDefinition.Builder builder = UsageDefinition.builder(NAME);
    builder.define("column", TokenType.COLUMN_NAME);
    return builder.build();
  }
  
  public void initialize(Arguments args) throws DirectiveParseException {
    this.column = ((ColumnName) args.value("column").value();
  }
  
  public List<Row> execute(List<Row> rows, RecipeContext context) throws RecipeException, ErrorRowException {
    for(Row row : rows) {
      int idx = row.find(column);
      if (idx != -1) {
        Object object = row.getValue(idx);
        if (object instanceof String) {
          String value = (String) object;
          row.setValue(idx, new StringBuffer(value).reverse().toString());
        }
      }
    }
    return rows;
  }
}
```

### Code Walk Through

#### Annontations

Following annotations are required for the plugin. If any of these are missing, the plugin or the directive will not be loaded. 

* `@Plugin` defines the type of plugin it is. For all UDDs it's set to `UDD.Type`.
* `@Name` defines the name of the plugin and as well as the directive name. 
* `@Description` provides a short description for the plugin and as well as for the directive. 

#### Call Pattern

The call pattern of UDD is the following :

* **DEFINE** : During configure time either in the CDAP Pipeline Transform or Data Prep Service, the `define()` method is invoked only once to retrieve the information of the usage. The usage defines the specification of the arguments that this directive is going to accept. In our example of `text-reverse`, the directive accepts only one argument and that is of type `TokenType.COLUMN_NAME`.
* **INITIALIZE** : During the initialization just before pumping in `Row`s through the directive, the `initialize()` method is invoked. This method is passed the arguments that are parsed by the system. It also provides the apportunity for the UDD writer to validate and throw exception if the value is not as expected.
* **EXECUTE** : Once the pipeline has been setup, the `Row` is passed into the `execute()` method to transform. 

### Testing

Here is the JUnit class that couldn't be any simpler. 

```
  @Test
  public void testBasicReverse() throws Exception {
    TestRecipe recipe = new TestRecipe();
    recipe.add("parse-as-csv :body ',';");
    recipe.add("set-headers :a,:b,:c;");
    recipe.add("text-reverse :b");

    TestRows rows = new TestRows();
    rows.add(new Row("body", "root,joltie,mars avenue"));
    rows.add(new Row("body", "joltie,root,venus blvd"));

    RecipePipeline pipeline = TestingRig.pipeline(TextReverse.class, recipe);
    List<Row> actual = pipeline.execute(rows.toList());

    Assert.assertEquals(2, actual.size());
    Assert.assertEquals("eitloj", actual.get(0).getValue("b"));
    Assert.assertEquals("toor", actual.get(1).getValue("b"));
  }
```


# Filter Empty

Description
-----------
Filter empty columns in the dataset.

Use case
-----------
To avoid empty columns in the dataset, you can either delete the entire row containing empty column, or set a given value to the empty fields.

Example
-----------
```
filter-empty :column
filter-empty :column 'keep','fillValue';
```

# Round Numbers

Description
-----------
Round decimal numbers to the nearest Integer.

Use case
-----------
Used to convert values of a specific column of any type (floating-point, string) to the nearest Integer.

Example
-----------
```
round-numbers :column
```

# Rows Filter

Description
-----------
Filter rows according to the given criteria.

Use case
-----------
Used to filter rows containing numeric data according to the given type (bigger, smaller) and given value (integer) to be compared with.

Example
-----------
```
rows-filter :column 'smaller','30';
```

# Row Size

Description
-----------
Calculate size of a row.

Use case
-----------
Used to calculate the size in bytes, of a row containing data of type string and stores the value in a new column.

Example
-----------
```
row-size
```

# Row Validator

Description
-----------
Validate date consistency of a row.

Use case
-----------
Used to calculate the data consistency of a row, by checking the percentage of non-empty columns.

Example
-----------
```
row-validator
```

# Convert Currency
Description
-----------
convert-currency is a UDD that converts a currency to another chosen 
currency. This UDD takes three arguments: the column name, what currency is tha data 
in that column and in what currency to convert it. It accepts only USD,EUR and CAD.
Use case
--------- 
Used to convert currency's from one to another.
Example
-------
For example, we have the column Money which is in US Dollars, and 
we want to convert it to EURO
````
convert-currency :Money 'USD','EUR'
````
# Duplicate Column
Description
-----------
duplicate-column is a UDD that duplicates a chosen column with
a new name, the rows are unchanged.
Use case
--------- 
Used when you need the same data but labeled in a different name.
Example
--------
For example, we have the column name StartDate, but we want to have another column 
with same data but with a new name StartDateSalary.
````
duplicate-column :StartDate 'StartDateSalary'
````
# Integer Manipulation
Description
----------- 
int-manipulation is a UDD that allows you to manipulate with number
in a chosen column. You can add, subtract, multiply, division. 
Use case
--------- 
Used when you need to manipulate numbers in a given column.
Example
--------
For example, we have a column with name Price, and we want to increase each row by ten.
````
int-manipulation :Price '+','10'
````
# Replace Row
Description
----------- 
replace-row is a UDD that allows you to change the values of rows based on
their previous values.
Use case
--------- 
Used when you need to change values of chosen rows to another desired value.
Example
--------
For example, we have a column with a name Weekday, and we want to change rows
with value Sunday to a new value Saturday.
````
replace-row :Weekday 'Sunday','Saturday'
````
# Split text
Description
----------- 
split-text is a UDD that allows you to split  the text of rows in
a chosen column in two parts and add them in two new different columns.
Use case
--------- 
Used when you need to split text for example emails splitting them in
username and domain.
Example
--------
For example, we have a column with name Email, and we want to separate the username 
from the domain. 
````
split-text :Email '@'
````

## That's it! Happy UDD'ing.

 
  


