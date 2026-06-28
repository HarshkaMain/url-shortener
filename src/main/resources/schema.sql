CREATE TABLE IF NOT EXISTS url_mappings (
    short_code VARCHAR(10) NOT NULL,
    long_url TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (short_code)
);