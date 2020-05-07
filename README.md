# DOMUS Rest API

## Configuration

### MySQL Database

On Ubuntu:

| Action                                    | Command                                                               |
| ----------------------------------------  | --------------------------------------------------------------------- |
| Install MySQL                             | `apt install mysql-server` ; `apt install default-libmysqlclient-dev` |
| Access MySQL as admin                     | `sudo mysql -u root`                                                  |
| Create the database                       | `CREATE DATABASE domus;`                                              |
| Create a new user and set it's password   | `CREATE USER 'springboot'@'localhost' IDENTIFIED BY 'domus_20';`           |
| Grant privileges to the new user          | `GRANT ALL PRIVILEGES ON * . * TO 'domus'@'localhost';`               |
| Find out the port where MySQL is running  | `SHOW VARIABLES WHERE Variable_name = 'port';`                        |

After this steps, we will need to configure our REST API to access the new database we just created.

For doing so, in application.properties` file of the REST API Spring Boot Project we need to add the following code:

```
spring.datasource.url=jdbc:mysql://<HOST_URL>/domus
spring.datasource.username=springboot
spring.datasource.password=domus_20
spring.jpa.hibernate.ddl-auto=update
```

Finally, replace **HOST_URL** with the host url where your database is configured.