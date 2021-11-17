CREATE TABLE interests (
	user_id INTEGER NOT NULL,
	science_field_name VARCHAR NOT NULL,
	
	PRIMARY KEY (user_id, science_field_name),
	FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE,
	FOREIGN KEY (science_field_name) REFERENCES science_field(name) ON DELETE CASCADE
);
