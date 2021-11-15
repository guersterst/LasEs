CREATE TABLE verification (
	id INTEGER NOT NULL,
	validation_random VARCHAR,
	verified BOOLEAN,
	timestamp_validation_started TIMESTAMP,
	unvalidated_email_address VARCHAR,
	
  	PRIMARY KEY (id),
	FOREIGN KEY (id) REFERENCES "user"(id)
);
