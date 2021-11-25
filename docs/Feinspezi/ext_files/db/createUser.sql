CREATE TABLE "user" (
	id SERIAL PRIMARY KEY NOT NULL,
	email_address VARCHAR(70) UNIQUE NOT NULL,
	is_administrator BOOLEAN NOT NULL,
	firstname VARCHAR(50) NOT NULL,
	lastname VARCHAR(50) NOT NULL,
	title VARCHAR(20),
	employer VARCHAR(100),
	birthdate DATE,
	avatar_thumbnail BYTEA,
	is_registered BOOLEAN NOT NULL,
	-- 128 Bit, Base64 encoded
	password_hash VARCHAR(24),
	-- 16 Byte Salt, Base64 encoded
	password_salt VARCHAR(24),
	
	CONSTRAINT valid_name CHECK (length(firstname) >= 1 AND length(lastname) >= 1),
	CONSTRAINT valid_birthdate CHECK (birthdate < CURRENT_DATE),
	CONSTRAINT email_address_pattern CHECK (email_address LIKE '_%@_%._%')
);

CREATE UNIQUE INDEX email_unique_case_insensitive ON "user" (LOWER(email_address));
