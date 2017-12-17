#!/bin/bash
#install sbt
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
sudo apt-get update
sudo apt-get install sbt
sbt compile
#install mysql
sudo apt-get install mysql-server
sudo apt-get install mysql-client
#inisialise db
password=1
mysql -u root "-p$password" -e "CREATE DATABASE IF NOT EXISTS purchase"
mysql -u root "-p$password" -D "purchase" -e "CREATE TABLE IF NOT EXISTS MYTABLE( id INT(10) NOT NULL AUTO_INCREMENT, login VARCHAR(255), password VARCHAR(255), PRIMARY KEY (id));"
mysql -u root "-p$password" -D "purchase" -e "CREATE TABLE IF NOT EXISTS PURCHASES(id INT(10) NOT NULL AUTO_INCREMENT, user_id INT(10) NOT NULL, name VARCHAR(255), price DOUBLE NOT NULL, date DATE, place VARCHAR(255), itemtype VARCHAR(255), PRIMARY KEY (id), FOREIGN KEY (user_id) REFERENCES MYTABLE(id));"
mysql -u root "-p$password" -D "purchase" -e "INSERT INTO MYTABLE(id, login, password) VALUES (1, 'loginv', 'passwordv')"
mysql -u root "-p$password" -D "purchase" -e "INSERT INTO MYTABLE(id, login, password) VALUES (2, 'loginv1', 'passwordv1')"
