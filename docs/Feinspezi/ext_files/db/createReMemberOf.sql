CREATE TABLE member_of(
	editor_id INTEGER NOT NULL,
	scientific_forum_id INTEGER NOT NULL,
	
	PRIMARY KEY (editor_id, scientific_forum_id),
	FOREIGN KEY (editor_id) REFERENCES editor(id) ON DELETE CASCADE,
	FOREIGN KEY (scientific_forum_id) REFERENCES scientific_forum(id) ON DELETE CASCADE
);
