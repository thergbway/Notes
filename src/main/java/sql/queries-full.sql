DROP TABLE IF EXISTS logs;
DROP TABLE IF EXISTS notesTags;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS notes;
DROP TABLE IF EXISTS tokens;
DROP TABLE IF EXISTS users;

CREATE TABLE logs
(
  creation_time TIMESTAMP WITHOUT TIME ZONE,
  message       VARCHAR(400)
);

CREATE TABLE tags
(
  id   SERIAL PRIMARY KEY,
  text VARCHAR(200) NOT NULL UNIQUE
);

CREATE TABLE users
(
  id         SERIAL PRIMARY KEY,
  login      VARCHAR(100) NOT NULL UNIQUE,
  password   VARCHAR(100) NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name  VARCHAR(100) NOT NULL
);

CREATE TABLE tokens
(
  id               SERIAL PRIMARY KEY,
  user_id          INTEGER                     NOT NULL REFERENCES users (id),
  valid_until_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  token            VARCHAR(100)                NOT NULL UNIQUE
);

CREATE TABLE notes
(
  id            SERIAL PRIMARY KEY,
  title         VARCHAR(50),
  content       VARCHAR(1000000),
  creation_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  change_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  user_id       INTEGER                     NOT NULL REFERENCES users (id)
);

CREATE TABLE notesTags
(
  id      SERIAL PRIMARY KEY,
  note_id INTEGER NOT NULL REFERENCES notes (id),
  tag_id  INTEGER NOT NULL REFERENCES tags (id)
);

CREATE OR REPLACE FUNCTION add_log_message_for_notes()
  RETURNS TRIGGER AS $$
DECLARE
  mstr   VARCHAR(30);
  astr   VARCHAR(100);
  retstr VARCHAR(254);
BEGIN
  IF TG_OP = 'INSERT'
  THEN
    astr = NEW.title;
    mstr := 'Add new note: ';
    retstr := mstr || astr;
    INSERT INTO logs (message, creation_time) VALUES (retstr, NOW());
    RETURN NEW;
  ELSIF TG_OP = 'UPDATE'
    THEN
      astr = NEW.title;
      mstr := 'Update note: ';
      retstr := mstr || astr;
      INSERT INTO logs (message, creation_time) VALUES (retstr, NOW());
      RETURN NEW;
  ELSIF TG_OP = 'DELETE'
    THEN
      astr = OLD.title;
      mstr := 'Remove note: ';
      retstr := mstr || astr;
      INSERT INTO logs (message, creation_time) VALUES (retstr, NOW());
      RETURN OLD;
  END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER notes_logger
AFTER INSERT OR UPDATE OR DELETE ON notes
FOR EACH ROW EXECUTE PROCEDURE
  add_log_message_for_notes();

INSERT INTO public.users (login, password, first_name, last_name) VALUES ('admin', 'admin', 'Ivan', 'Ivanov');
INSERT INTO public.users (login, password, first_name, last_name) VALUES ('petr', 'petrov', 'Пётр', 'Петров');

INSERT INTO public.notes (title, content, creation_time, change_time, user_id) VALUES
  ('My first note', 'Hello, This is Ivan and it is my first note', '2015-05-31 04:13:24.936000',
   '2015-05-31 04:13:34.442000', 1);
INSERT INTO public.notes (title, content, creation_time, change_time, user_id)
VALUES ('Вторая записка', 'Привет, это снова я - Иван', '2015-06-30 04:14:06.825000', '2015-06-30 04:14:17.176000', 1);
INSERT INTO public.notes (title, content, creation_time, change_time, user_id) VALUES
  ('Great trip', 'Sup guys, today I had a very good trip', '2015-06-13 04:15:06.087000', '2015-06-29 04:15:14.016000',
   1);
INSERT INTO public.notes (title, content, creation_time, change_time, user_id) VALUES
  ('Football', 'Hello, I am a very good footballer', '2015-05-07 04:15:43.659000', '2015-05-07 04:15:49.005000', 1);
INSERT INTO public.notes (title, content, creation_time, change_time, user_id)
VALUES ('University', 'Hello My university is LETI', '2015-05-25 04:16:27.770000', '2015-05-29 04:16:31.644000', 2);
INSERT INTO public.notes (title, content, creation_time, change_time, user_id) VALUES
  ('Good weather', 'Wow guys! Today is a very good weather! :)', '2015-05-29 04:18:44.666000',
   '2015-05-29 04:18:49.828000', 2);

INSERT INTO public.tags (text) VALUES ('good_start');
INSERT INTO public.tags (text) VALUES ('amazing');
INSERT INTO public.tags (text) VALUES ('русский');
INSERT INTO public.tags (text) VALUES ('bicycle');
INSERT INTO public.tags (text) VALUES ('footballer');
INSERT INTO public.tags (text) VALUES ('good_team');
INSERT INTO public.tags (text) VALUES ('LETI');
INSERT INTO public.tags (text) VALUES ('4_years');
INSERT INTO public.tags (text) VALUES ('warm');
INSERT INTO public.tags (text) VALUES ('summer_time');

INSERT INTO public.notestags (note_id, tag_id) VALUES (1, 1);
INSERT INTO public.notestags (note_id, tag_id) VALUES (1, 2);
INSERT INTO public.notestags (note_id, tag_id) VALUES (2, 3);
INSERT INTO public.notestags (note_id, tag_id) VALUES (3, 4);
INSERT INTO public.notestags (note_id, tag_id) VALUES (4, 5);
INSERT INTO public.notestags (note_id, tag_id) VALUES (4, 6);
INSERT INTO public.notestags (note_id, tag_id) VALUES (5, 7);
INSERT INTO public.notestags (note_id, tag_id) VALUES (5, 8);
INSERT INTO public.notestags (note_id, tag_id) VALUES (6, 9);
INSERT INTO public.notestags (note_id, tag_id) VALUES (6, 10);
INSERT INTO public.notestags (note_id, tag_id) VALUES (2, 2);
INSERT INTO public.notestags (note_id, tag_id) VALUES (3, 2);
INSERT INTO public.notestags (note_id, tag_id) VALUES (4, 2);
INSERT INTO public.notestags (note_id, tag_id) VALUES (5, 2);
INSERT INTO public.notestags (note_id, tag_id) VALUES (6, 2);

-- ==============================

drop OWNED BY notes_user;
drop user notes_user;

create user notes_user with password '123';

grant select on notes to notes_user;
grant select on users to notes_user;
grant select on tokens to notes_user;
grant select on notesTags to notes_user;
grant select on tags to notes_user;

grant update on notes to notes_user;
grant update on users to notes_user;
grant update on tokens to notes_user;
grant update on notesTags to notes_user;
grant update on tags to notes_user;
grant update on logs to notes_user;

grant insert on notes to notes_user;
grant insert on users to notes_user;
grant insert on tokens to notes_user;
grant insert on notesTags to notes_user;
grant insert on tags to notes_user;
grant insert on logs to notes_user;

grant delete on notes to notes_user;
grant delete on users to notes_user;
grant delete on tokens to notes_user;
grant delete on notesTags to notes_user;
grant delete on tags to notes_user;

grant usage, select on sequence notes_id_seq to notes_user;
grant usage, select on sequence users_id_seq to notes_user;
grant usage, select on sequence tokens_id_seq to notes_user;
grant usage, select on sequence notestags_id_seq to notes_user;
grant usage, select on sequence tags_id_seq to notes_user;

-- ===========================


create index notes_id_index on notes(id);
create index users_id_index on users(id);
create index logs_creation_time_index on logs(creation_time);
create index token_id_index on tokens(id);
create index note_tags_id_index on notesTags(id);
create index tags_id_index on tags(id);