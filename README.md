# Custom UDDs

* Filter Empty
* Round Numbers
* Rows Filter
* Row Size
* Row Validator
* Convert Currency
* Duplicate Column
* Integer Manipulation
* Replace Row
* Split Text




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
convert-currency is a UDD that converts a currency to another chosen currency. This UDD takes three arguments: the column name, what currency is tha data in that column and in what currency to convert it. It accepts only USD,EUR and CAD.

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
# Split Text
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

 
  


