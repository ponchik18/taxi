INSERT INTO passenger (id, first_name, last_name, email, phone) VALUES (5, 'John', 'Doe', 'john.doe@example.com', '+375111111119') ON CONFLICT (id) DO NOTHING;;
INSERT INTO passenger (id, first_name, last_name, email, phone) VALUES (6, 'Jane', 'Doe', 'jane.doe@example.com', '+375111111112') ON CONFLICT (id) DO NOTHING;;
INSERT INTO passenger (id, first_name, last_name, email, phone) VALUES (7, 'Alice', 'Smith', 'alice.smith@example.com', '+375111111113') ON CONFLICT (id) DO NOTHING;;
INSERT INTO passenger (id, first_name, last_name, email, phone) VALUES (8, 'Bob', 'Johnson', 'bob.johnson@example.com', '+375111111114') ON CONFLICT (id) DO NOTHING;;
INSERT INTO passenger (id, first_name, last_name, email, phone) VALUES (9, 'Eva', 'Williams', 'eva.williams@example.com', '+375111111115') ON CONFLICT (id) DO NOTHING;;
INSERT INTO passenger (id, first_name, last_name, email, phone) VALUES (10, 'Charlie', 'Brown', 'charlie.brown@example.com', '+375111111116') ON CONFLICT (id) DO NOTHING;;
INSERT INTO passenger (id, first_name, last_name, email, phone) VALUES (11, 'Grace', 'Miller', 'grace.miller@example.com', '+375111111117') ON CONFLICT (id) DO NOTHING;;