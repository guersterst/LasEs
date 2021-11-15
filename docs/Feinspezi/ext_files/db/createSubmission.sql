CREATE TYPE submission_state AS ENUM (
	'SUBMITTED',
	'REVISION_REQUIRED',
	'ACCEPTED',
	'REJECTED'
);

CREATE TABLE submission (
	id SERIAL PRIMARY KEY,
	title VARCHAR NOT NULL,
	state submission_state NOT NULL,
	timestamp_submission TIMESTAMP,
	requires_revision BOOLEAN NOT NULL DEFAULT FALSE,
	timestamp_deadline_revision TIMESTAMP,
	
	author_id SERIAL REFERENCES user(id) NOT NULL,
	editor_id SERIAL REFERENCES user(id) NOT NULL,
	forum_id SERIAL REFERENCES scientificForum(id) NOT NULL
);
