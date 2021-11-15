CREATE TABLE paper (
	reviewer_id INTEGER REFERENCES reviewer(id) NOT NULL ON DELETE CASCADE,
	version INTEGER NOT NULL,
	submission_id INTEGER NOT NULL,
	
	timestamp_upload TIMESTAMP NOT NULL DEFAULT NOW(),
	is_visible BOOLEAN NOT NULL,
	is_recommended BOOLEAN NOT NULL,
	comment VARCHAR,
	pdf_file BYTEA,
	
	PRIMARY KEY (submission_id, version, reviewer_id),
	FOREIGN KEY (submission_id) REFERENCES submission(id) ON DELETE CASCADE,
	FOREIGN KEY (version) REFERENCES paper(version) ON DELETE CASCADE
);
