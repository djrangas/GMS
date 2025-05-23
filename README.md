# GMS

```shell
sudo javac -cp ".:/Users/djrangas/Downloads/javacv-platform-1.5.10-bin/*:/Users/djrangas/Downloads/core-3.5.3.jar:/Users/djrangas/Downloads/javase-3.5.3.jar:/Users/djrangas/Downloads/mysql-connector-j-9.2.0/*:/Users/djrangas/Downloads/jcalendar-1.4.jar" Main.java
sudo java -cp ".:/Users/djrangas/Downloads/javacv-platform-1.5.10-bin/*:/Users/djrangas/Downloads/core-3.5.3.jar:/Users/djrangas/Downloads/javase-3.5.3.jar:/Users/djrangas/Downloads/mysql-connector-j-9.2.0/*:/Users/djrangas/Downloads/jcalendar-1.4.jar" Main
```
```sql
CREATE DATABASE gym_db;

USE gym_db;

CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

INSERT INTO users (username, password)
VALUES ('admin', 'root');
```
