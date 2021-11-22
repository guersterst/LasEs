CREATE TYPE submission_state AS ENUM (
	'SUBMITTED',
	'REVISION_REQUIRED',
	'ACCEPTED',
	'REJECTED'
);

CREATE TABLE submission (
	id SERIAL PRIMARY KEY NOT NULL,
	title VARCHAR NOT NULL,
	state submission_state NOT NULL,
	timestamp_submission TIMESTAMP,
	requires_revision BOOLEAN NOT NULL DEFAULT FALSE,
	timestamp_deadline_revision TIMESTAMP,
	
	author_id INTEGER REFERENCES "user"(id) ON DELETE CASCADE,
	editor_id INTEGER REFERENCES "user"(id) ON DELETE SET NULL,
	forum_id INTEGER REFERENCES scientific_forum(id) ON DELETE CASCADE
);
