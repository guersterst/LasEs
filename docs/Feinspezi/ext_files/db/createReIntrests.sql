CREATE TABLE intrests(
	user_id SERIAL,
	science_field_name VARCHAR,
	
	PRIMARY KEY (user_id, science_field_name),
	FOREIGN KEY user_id REFERENCES user(id) ON DELETE CASCADE,
	FOREIGN KEY science_field_name REFERENCES science_field(name) ON DELETE CASCADE
);
