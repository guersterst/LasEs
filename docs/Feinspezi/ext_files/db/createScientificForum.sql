CREATE TABLE scientific_forum(
	id SERIAL PRIMARY KEY NOT NULL,
	name VARCHAR(200) UNIQUE NOT NULL,
	description VARCHAR(500),
	url VARCHAR(150),
	review_manual VARCHAR(500),
	timestamp_deadline TIMESTAMP
);
