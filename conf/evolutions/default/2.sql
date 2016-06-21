# Seed bases

# --- !Ups

INSERT INTO apprentices (name) VALUES ('Nick'), ('Mollie'), ('Rabea'), ('Ced'), ('Priya');
INSERT INTO restaurants (name, menulink) VALUES ('Bahn Mi Bay', 'http://www.banhmibay.co.uk'),
                                                  ('Chilango', 'https://ORDER.chilango.co.uk'),
                                                  ('Deliveroo', 'https:// deliveroo.co.uk'),
                                                  ('Hummus Bros', 'http://www.hbros.co.uk/ourfood/'),
                                                  ('Kin', 'http://www.kinstreetfood.com/Menu'),
                                                  ('Leon', 'https://deliveroo.co.uk/menu/london/farringdon / leon - farringdon'),
                                                  ('Moz', 'http://www.moz.london/#!menu/cu9'),
                                                  ('Pronto', 'https://www.pronto.co.uk/menu'),
                                                  ('Three Gents', 'https://gallery.mailchimp.com/0 b0dc611053d98f4ee3a1989a/files/Menu_w_c_20_07_15_HS.pdf'),
                                                  ('Deliverance', 'https://www.deliverance.co.uk/Menu'),
                                                  ('Firezza', 'http://islington.firezza-orders.com/categories/pizzas'),
                                                  ('Jackie Filmer', 'http://www.jackiefilmercatering.com'),
                                                  ('Caterwings', 'https://www.caterwings.co.uk');
INSERT INTO employees (name) VALUES ('Amelia'),
                                    ('Cedric'),
                                    ('CJ'),
                                    ('Christoph'),
                                    ('Daniel'),
                                    ('Enrique'),
                                    ('Felipe'),
                                    ('Georgina'),
                                    ('James'),
                                    ('Jarkyn'),
                                    ('Jim'),
                                    ('Makis'),
                                    ('Mateu'),
                                    ('Mollie'),
                                    ('Nathan'),
                                    ('Nick'),
                                    ('Priya'),
                                    ('Rabea'),
                                    ('Sarah'),
                                    ('skim'),
                                    ('Uku');

# --- !Downs

DELETE FROM apprentices;
DELETE FROM restaurants;
DELETE FROM employees;
