CREATE TABLE topics(
	science_field_name VARCHAR,
	scientific_forum_id SERIAL,
	
	PRIMARY KEY (science_field_name, scientific_forum_id),
	FOREIGN KEY science_field_name REFERENCES science_field(name) ON DELETE CASCADE,
	FOREIGN KEY scientific_forum_id REFERENCES scientific_forum(id) ON DELETE CASCADE
);
