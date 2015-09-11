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