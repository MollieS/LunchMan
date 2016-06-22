# Seed bases

# --- !Ups

DELETE FROM restaurants WHERE name = 'Bahn Mi Bay';
DELETE FROM restaurants WHERE name = 'Deliveroo';
DELETE FROM restaurants WHERE name = 'Leon';
DELETE FROM restaurants WHERE name = 'Three Gents';

INSERT INTO restaurants (name, menulink) VALUES ('Deliveroo', 'https://deliveroo.co.uk'),
('Leon', 'https://deliveroo.co.uk/menu/london/farringdon/leon-farringdon'),
('Three Gents', 'https://gallery.mailchimp.com/0b0dc611053d98f4ee3a1989a/files/Menu_w_c_20_07_15_HS.pdf'),
('Bahn Mi Bay', 'http://www.takeeateasy.co.uk/en/delivery-london/restaurant/banh-mi-bay---holborn')


# --- !Downs

DELETE FROM apprentices;
DELETE FROM restaurants;
DELETE FROM employees;
