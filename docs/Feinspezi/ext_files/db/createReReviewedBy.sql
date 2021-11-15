CREATE TABLE reviewed_by(
	reviewer_id SERIAL,
	submission_id SERIAL,
	has_accepted BOOLEAN,
	timestamp_deadline TIMESTAMP,
	
	PRIMARY KEY (reviewer_id, submission_id),
	FOREIGN KEY reviewer_id REFERENCES reviewer(id) ON DELETE CASCADE,
	FOREIGN KEY submission_id REFERENCES submission(id) ON DELETE CASCADE
);
