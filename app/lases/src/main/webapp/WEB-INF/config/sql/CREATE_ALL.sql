CREATE TABLE "user" (
	id SERIAL PRIMARY KEY NOT NULL,
	email_address VARCHAR(70) UNIQUE NOT NULL,
	is_administrator BOOLEAN NOT NULL,
	firstname VARCHAR(50) NOT NULL,
	lastname VARCHAR(50) NOT NULL,
	title VARCHAR(20),
	employer VARCHAR(100),
	birthdate DATE,
	avatar_thumbnail BYTEA,
	is_registered BOOLEAN NOT NULL,
	-- 128 Bit, Base64 encoded
	password_hash VARCHAR(24),
	-- 16 Byte Salt, Base64 encoded
	password_salt VARCHAR(24)
	CONSTRAINT valid_name CHECK (length(firstname) >= 1 AND length(lastname) >= 1),
	CONSTRAINT valid_birthdate CHECK (birthdate < CURRENT_DATE),
	CONSTRAINT email_address_pattern CHECK (email_address LIKE '_%@_%._%')
);

CREATE UNIQUE INDEX email_unique_case_insensitive ON "user" (LOWER(email_address));

CREATE TABLE scientific_forum(
	id SERIAL PRIMARY KEY NOT NULL,
	name VARCHAR(200) UNIQUE NOT NULL,
	description VARCHAR(500),
	url VARCHAR(150),
	review_manual VARCHAR(500),
	timestamp_deadline TIMESTAMP
);

CREATE TYPE submission_state AS ENUM (
	'SUBMITTED',
	'REVISION_REQUIRED',
	'ACCEPTED',
	'REJECTED'
);

CREATE TABLE submission (
	id SERIAL PRIMARY KEY NOT NULL,
	title VARCHAR(200) NOT NULL,
	state submission_state NOT NULL,
	timestamp_submission TIMESTAMP,
	requires_revision BOOLEAN NOT NULL DEFAULT FALSE,
	timestamp_deadline_revision TIMESTAMP,
	author_id INTEGER REFERENCES "user"(id) ON DELETE CASCADE,
	editor_id INTEGER REFERENCES "user"(id) ON DELETE SET NULL,
	forum_id INTEGER REFERENCES scientific_forum(id) ON DELETE CASCADE
);

CREATE TABLE science_field(
	name VARCHAR(30) PRIMARY KEY NOT NULL
);

CREATE TABLE interests (
	user_id INTEGER NOT NULL,
	science_field_name VARCHAR NOT NULL,
	PRIMARY KEY (user_id, science_field_name),
	FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE,
	FOREIGN KEY (science_field_name) REFERENCES science_field(name) ON DELETE CASCADE
);

CREATE TABLE topics(
	science_field_name VARCHAR NOT NULL,
	scientific_forum_id INTEGER NOT NULL,
	PRIMARY KEY (science_field_name, scientific_forum_id),
	FOREIGN KEY (science_field_name) REFERENCES science_field(name) ON DELETE CASCADE,
	FOREIGN KEY (scientific_forum_id) REFERENCES scientific_forum(id) ON DELETE CASCADE
);

CREATE TABLE member_of(
	editor_id INTEGER NOT NULL,
	scientific_forum_id INTEGER NOT NULL,
	PRIMARY KEY (editor_id, scientific_forum_id),
	FOREIGN KEY (editor_id) REFERENCES "user"(id) ON DELETE CASCADE,
	FOREIGN KEY (scientific_forum_id) REFERENCES scientific_forum(id) ON DELETE CASCADE
);

CREATE TABLE co_authored(
	user_id INTEGER NOT NULL,
	submission_id INTEGER NOT NULL,
	PRIMARY KEY (user_id, submission_id),
	FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE,
	FOREIGN KEY (submission_id) REFERENCES submission(id) ON DELETE CASCADE
);

CREATE TYPE review_task_state AS ENUM (
	'NO_DECISION',
	'ACCEPTED',
	'REJECTED'
);

CREATE TABLE reviewed_by(
	reviewer_id INTEGER NOT NULL,
	submission_id INTEGER NOT NULL,
	has_accepted review_task_state,
	timestamp_deadline TIMESTAMP,
	PRIMARY KEY (reviewer_id, submission_id),
	FOREIGN KEY (reviewer_id) REFERENCES "user"(id) ON DELETE CASCADE,
	FOREIGN KEY (submission_id) REFERENCES submission(id) ON DELETE CASCADE
);

CREATE TABLE paper (
	version INTEGER NOT NULL,
	submission_id SERIAL NOT NULL,
	timestamp_upload TIMESTAMP NOT NULL DEFAULT NOW(),
	is_visible BOOLEAN NOT NULL,
	pdf_file BYTEA NOT NULL,
	PRIMARY KEY (submission_id, version),
	FOREIGN KEY (submission_id) REFERENCES submission(id) ON DELETE CASCADE
);

CREATE TABLE review (
	reviewer_id INTEGER REFERENCES "user"(id) ON DELETE CASCADE,
	version INTEGER NOT NULL,
	submission_id INTEGER NOT NULL,
	timestamp_upload TIMESTAMP NOT NULL DEFAULT NOW(),
	is_visible BOOLEAN NOT NULL,
	is_recommended BOOLEAN NOT NULL,
	comment VARCHAR,
	pdf_file BYTEA,
	PRIMARY KEY (submission_id, version, reviewer_id),
	FOREIGN KEY (submission_id, version) REFERENCES paper(submission_id, version) ON DELETE CASCADE
);

CREATE TABLE verification (
	id INTEGER NOT NULL,
	validation_random VARCHAR,
	is_verified BOOLEAN,
	timestamp_validation_started TIMESTAMP,
	unvalidated_email_address VARCHAR,
  	PRIMARY KEY (id),
	FOREIGN KEY (id) REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE system(
	id INTEGER PRIMARY KEY,
	company_name VARCHAR(100),
	welcome_heading VARCHAR(200),
	welcome_description VARCHAR(500),
	css_theme VARCHAR(20),
	imprint VARCHAR,
	logo_image BYTEA
);

create view user_data as
select u.id, u.email_address, u.firstname, u.lastname, u.title, u.birthdate, u.employer, u.is_registered, count(distinct s.id) as count_submissions,
(
case
when u.is_administrator then 'admin'
when exists (select * from member_of mo where mo.editor_id = u.id) then 'editor'
when exists (select * from reviewed_by rb where rb.reviewer_id = u.id) then 'reviewer'
else 'none'
end) as user_role
from "user" u
left JOIN submission s
on u.id = s.author_id
left join co_authored ca
on (u.id = ca.user_id and ca.submission_id = s.id)
group by u.id;

-- create basic admin user, to allow login.
-- email: admin@example.com password: admin1!ADMIN

INSERT INTO "user"(id, email_address, is_administrator, firstname, lastname, is_registered, password_hash, password_salt)
VALUES (-1, 'admin@example.com', TRUE, 'Administrator', 'Administrator', TRUE, 'CWDkErZ4M1iW4LJjHB8kAA==', '7aj1fcaMrRpJrtS9ZNsIsQ==');

INSERT INTO verification(id, is_verified)
VALUES (-1, TRUE);

-- create system settings.

INSERT INTO system
VALUES (0, 'LasEs', 'Welcome to LasEs', 'World Class Review Managment System', 'orange', '', NULL);
