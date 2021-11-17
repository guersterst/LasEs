CREATE TABLE review (
	reviewer_id INTEGER REFERENCES reviewer(id) ON DELETE CASCADE,
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
