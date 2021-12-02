CREATE TABLE system(
	id INTEGER PRIMARY KEY,
	company_name VARCHAR(100),
	welcome_heading VARCHAR(200),
	welcome_description VARCHAR(500),
	css_theme VARCHAR(20),
	imprint VARCHAR,
	logo_image BYTEA
);
