CREATE TABLE scientific_forum(
	id SERIAL PRIMARY KEY NOT NULL,
	name VARCHAR UNIQUE NOT NULL,
	description VARCHAR,
	url VARCHAR,
	review_manual VARCHAR,
	timestamp_deadline TIMESTAMP
);
