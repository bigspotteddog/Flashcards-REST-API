INSERT INTO category(id, name)
    VALUES('1', 'Astronomy');

INSERT INTO study_session(id, category_id, name)
    VALUES('1', '1', 'Solar system');

INSERT INTO flashcard(id, study_session_id, question, answer)
    VALUES('1', '1', 'What kind of star is the sun?', 'Yellow dwarf');