CREATE TABLE "user" (
	id SERIAL PRIMARY KEY NOT NULL,
	email_address VARCHAR UNIQUE NOT NULL,
	administrator BOOLEAN NOT NULL,
	firstname VARCHAR NOT NULL,
	lastname VARCHAR NOT NULL,
	title VARCHAR,
	employer VARCHAR,
	birthdate DATE,
	avatar_thumbnail BYTEA,
	is_registered BOOLEAN NOT NULL,
	password_hash VARCHAR,
	password_salt VARCHAR,
	
	CONSTRAINT valid_name CHECK (length(firstname) >= 1 AND length(lastname) >= 1),
	CONSTRAINT valid_birthdate CHECK (birthdate < CURRENT_DATE),
	CONSTRAINT email_address_pattern CHECK (email_address LIKE '_%@_%._%')
);

CREATE UNIQUE INDEX email_unique_case_insensitive ON "user" (LOWER(email_address));
