# Adds Will to Apprentices and Employees

# --- !Ups

INSERT INTO apprentices (name) VALUES ('Will');
INSERT INTO employees (name) VALUES ('Will');

# --- !Downs

DELETE FROM apprentices WHERE name = 'Will';
DELETE FROM employees WHERE name = 'Will';
