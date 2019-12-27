BEGIN TRANSACTION;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id serial PRIMARY KEY,
  user_name varchar(255) NOT NULL UNIQUE,     -- Username
  password varchar(32) NOT NULL,      -- Password
  salt varchar(256) NOT NULL,          -- Password Salt
  role varchar(255) NOT NULL default('user')
);

CREATE TABLE subjects (
   subject_id serial PRIMARY KEY,
   user_id int NOT NULL,
   subject_name VARCHAR(255) NOT NULL,
   subject_icon_path VARCHAR(255),
   CONSTRAINT fk_userId FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE deck (
  deck_id serial,
  subject_id int NOT NULL,
  deck_name varchar(255) NOT NULL,
  is_public Boolean,
  CONSTRAINT pk_deck PRIMARY KEY (deck_id),
  CONSTRAINT fk_subject_id FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE
);

CREATE TABLE card (
card_id serial,
deck_id int NOT NULL,
question TEXT NOT NULL,
answer TEXT NOT NULL,
image_path VARCHAR(255),
CONSTRAINT pk_card PRIMARY KEY (card_id),
CONSTRAINT fk_deck FOREIGN KEY (deck_id) REFERENCES deck(deck_id) ON DELETE CASCADE
);


COMMIT TRANSACTION;
