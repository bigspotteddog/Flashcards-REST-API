INSERT INTO category(id, name) VALUES('1', 'Music'),
                                     ('2', 'Finance');

INSERT INTO study_session(id, category_id, name) VALUES('1', '1', 'Guitar'),
                                                       ('2', '2', 'Stock exchange');

INSERT INTO flashcard(id, study_session_id, question, answer)
    VALUES('1', '1', 'What is the first tone of the scale?', 'The first tone is called C'),
          ('2', '2', 'What is a broker?', 'A person that facilitates transactions');