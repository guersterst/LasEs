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
	FOREIGN KEY (reviewer_id) REFERENCES reviewer(id) ON DELETE CASCADE,
	FOREIGN KEY (submission_id) REFERENCES submission(id) ON DELETE CASCADE
);
