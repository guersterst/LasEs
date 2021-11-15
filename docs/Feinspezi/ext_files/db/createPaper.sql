CREATE TABLE paper (
	version INTEGER NOT NULL,
	submission_id SERIAL NOT NULL,
	timestamp_upload TIMESTAMP NOT NULL DEFAULT NOW(),
	is_visible BOOLEAN NOT NULL,
	pdf_file BYTEA NOT NULL,
	
	PRIMARY KEY (submission_id, version),
	FOREIGN KEY (submission_id) REFERENCES submission(id)
);
