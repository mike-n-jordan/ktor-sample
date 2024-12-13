CREATE TABLE User
(
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    username      VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    type          TEXT CHECK ( type IN ('ADMIN', 'USER') ) DEFAULT 'USER',
    created_at    TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_username ON User (username);

CREATE TABLE UserSession
(
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    token      VARCHAR(255) NOT NULL,
    user_id    INT          NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User (id)
);
