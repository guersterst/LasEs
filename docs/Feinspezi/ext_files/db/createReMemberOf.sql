CREATE TABLE member_of(
	editor_id SERIAL,
	scientific_forum_id SERIAL,
	
	PRIMARY KEY (editor_id, scientific_forum_id),
	FOREIGN KEY user_id REFERENCES editor(id) ON DELETE CASCADE,
	FOREIGN KEY scientific_forum_id REFERENCES scientific_forum(id) ON DELETE CASCADE
);
