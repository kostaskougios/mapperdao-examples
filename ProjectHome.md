A set of examples for the [MapperDao ORM library for Scala](https://code.google.com/p/mapperdao/)

Please download via the Download link or through git:

```
git clone https://code.google.com/p/mapperdao-examples/
```

All examples include an eclipse scala project and run configurations. Maven can also be used to build them:

```
mvn package
```

## mysecrets: mapperdao example for lift framework ##

The download contains a lift web application which uses mapperdao to persist domain classes. The application is about users storing their secrets and sharing them with other users. It contains login, registration, secret editing and listing pages. It currently demonstrates many-to-one, one-to-many and many-to-many relationships between entities and how lift forms integrate with mapper dao to create, update and delete data. Runs on mysql and postgresql. Eclipse project and jetty run configs are also provided.

[Browse the source code](https://code.google.com/p/mapperdao-examples/source/browse/#svn%2Ftrunk%2Fmysecrets%2Fsrc%2Fmain%2Fscala%2Fcom%2Frits%2Fmysecrets)

## productcatalogue : mapperdao example for circumflex web framework ##

A circumflex web framework example, the source code can be browsed [here](https://code.google.com/p/mapperdao-examples/source/browse/#svn%2Ftrunk%2Fproductcatalogue). A product catalog implementation, each product containing attributes, categories and prices per currency. Demonstrates pagination and creating/updating entities with oneToMany, manyToMany and self referenced relationships. Rungs against mysql, postgresql, oracle, sqlserver. Eclipse project and run configs are also provided.

[Browse the source code](https://code.google.com/p/mapperdao-examples/source/browse/#svn%2Ftrunk%2Fproductcatalogue%2Fsrc%2Fmain%2Fscala%2Fcom%2Frits%2Fmodel)

## other mapperdao examples ##

Examples:

  * dellstore : using the [dellstore2 database](http://www.pgfoundry.org/projects/dbsamples/) for postgresql, this example maps the tables to entities, performs queries and inserts/updates the data. (dellstore package).

  * employees: using the [employees database](https://launchpad.net/test-db/), this example connects to mysql and maps entities with the tables. (employees package)

[Browse the source code](https://code.google.com/p/mapperdao-examples/source/browse/#svn%2Ftrunk%2Fexamples)

[MapperDao Documentation](https://code.google.com/p/mapperdao/wiki/TableOfContents)