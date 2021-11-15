CREATE TABLE co_authored(
	user_id INTEGER NOT NULL,
	submission_id INTEGER NOT NULL,
	
	PRIMARY KEY (user_id, submission_id),
	FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE,
	FOREIGN KEY (submission_id) REFERENCES submission(id) ON DELETE CASCADE
);
