# AdeORM
### BY AARON PARKER, DAVID GARCIA, ELVIS LEE

## Introduction

AdeORM is a custom object relational mapping (ORM) framework. The framework allows developers interact with 
the relational data source without writing JCBC and SQL statements.

In this version of release, the framework provides Data Manipulation and Transaction Control functions.
The framework also supports connection pooling.

## Major Functions Provided

> ### Data Manipulation
> * **ADD** - add a record by passing a POJO
> * **UPDATE** - set values of a record
> * **DELETE** - delete a record by passing a POJO or primary key
> * **GET** - retrieve records from a table or joint tables by primary key or column value, supports "and\or" filtering and ordering

> ### Data Transaction
> * **BEGIN** - start a transaction
> * **COMMIT** - commit a transaction
> * **ROLLBACK** - rollback a transaction
> * **CLOSE** - close a transaction


## Installation

The repository contains all the source files with a jar file. The jar file can be imported as external library or installed in Maven and add dependency in POM.
```
<dependency>
<groupId>dev.ade.project</groupId>
<artifactId>adeOrm</artifactId>
<version>1.1</version>
</dependency>
```
## How to use
### Mapping POJO to DB Table
Various annotations are provided to mark POJO classes and fields mapping with the corresponding tables and columns in a database.
~~~

@TableName(tableName = "users")
public class User {

    @ColumnName(columnName = "first_name")
    private String firstName;

    @ColumnName(columnName = "last_name")
    private String lastName;

    @ColumnName(columnName = "gender")
    private char gender;

    @PrimaryKey
    @ColumnName(columnName = "username")
    private String username;

    @ColumnName(columnName = "user_password")
    private String userPassword;
~~~

### Construct and Initialize
~~~
// save USERNAME and PASSWORD of database in System environment variables.
String url = "jdbc:postgresql://....amazonaws.com:5432/postgres";
AdeOrm AdeOrm = new AdeOrm(User.class);
adeOrm.setConnection(url);
~~~

### CRUD actions
There are many overloaded methods (3 adds, 3 updates, 2 deletes, 8 gets) to provide flexibility for developers to manipulate data.
~~~
adeOrm.add(new User("Brandon", "Bauer", "M", "brandon", "password"));
adeOrm.update("userPassword", "NewPassword");
adeOrm.delete("username", "brandon");
User user = adeOrm.get("username", "brandon");
~~~

### Transaction
Developers can group multiple CRUD actions into one transaction to ensure that the database is always in a consistent
state.
~~~
try {
    adeOrm.open();
    adeOrm.update(...);
    adeOrm.update(...);
    adeOrm.commit();
} catch (Exception e) {
    adeOrm.rollback();
} finally {
    adeOrm.close();
}
~~~

## To-Do

The following things would be nice to do:
* Add methods to support DDL and DCL.
* Add aggregate function support.
* Extend get methods functionality for more complicated filtering cases.

## License

The project was as a practice of applying Java Reflection and Annotations to create a custom library. Our team created the AdeOrm within two weeks, there is a lot of room for improvement. 
Feel free to use it and make contribution.