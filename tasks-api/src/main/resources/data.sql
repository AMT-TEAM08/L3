INSERT INTO tags (name, description) VALUES
('tag1', 'Description 1'),
('tag2', 'Description 2'),
('tag3', 'Description 3'),
('tag4', 'Description 4');

INSERT INTO users (id, name) VALUES
(1, 'Thomas Bangalter'),
(2, 'Guy-Manuel de Homem-Christo');

INSERT INTO tasks (name, description, due_date, user_id) VALUES
('Task 1', 'Description 1', '2022-01-01', 1),
('Task 2', 'Description 2', '2022-02-01', 1),
('Task 3', 'Description 3', '2022-03-01', 2),
('Task 4', 'Description 4', '2022-04-01', 2);