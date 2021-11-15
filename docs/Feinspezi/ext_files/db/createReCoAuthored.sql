CREATE TABLE co_authored(
	user_id SERIAL,
	submission_id SERIAL,
	
	PRIMARY KEY (user_id, submission_id),
	FOREIGN KEY user_id REFERENCES user(id) ON DELETE CASCADE,
	FOREIGN KEY submission_id REFERENCES submission(id) ON DELETE CASCADE
);
