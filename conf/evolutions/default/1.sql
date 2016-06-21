# Add stuff

# --- !Ups

create table apprentices (name varchar(255));
create table schedule (date text, apprentice varchar(255), restaurant varchar(255), menulink varchar(255));
create table restaurants (name varchar(255), menulink varchar(255));
create table employees (name varchar(255), foodorder varchar(255));
create table guests (name varchar(255), foodorder varchar(255) NOT NULL);

# --- !Downs

DROP TABLE apprentices;
DROP TABLE schedule;
DROP TABLE restaurants;
DROP TABLE employees;
DROP TABLE guests;
