to run the application, please

1.	create the database by running postgresql.database.creation.sql , login
	in postgresql as "mysecrets" user and run postgresql.ddl.sql to create
	the tables
	
2.	from the command line, execute : 

mvn jetty:run

3.	after the dependencies are downloaded and jetty starts, please point your
	browser to http://localhost:8080/

4.	Register a user and login.