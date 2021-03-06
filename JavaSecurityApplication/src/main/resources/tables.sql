CREATE TABLE CREDENTIALS(
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    username VARCHAR(30) UNIQUE,
    password VARCHAR(30) NOT NULL
);

CREATE TABLE COMMENTS(
    comment VARCHAR(200) NOT NULL
);

CREATE TABLE BANK_ACCOUNT(
    id BIGINT PRIMARY KEY,
    amount BIGINT DEFAULT 0,
    username VARCHAR(30) NOT NULL,
    CONSTRAINT FK_BANK_ACCOUNT_ID FOREIGN KEY (id) REFERENCES CREDENTIALS(id) ON DELETE CASCADE,
    CONSTRAINT BANK_ACCOUNT_NEGATIVE_AMOUNT CHECK (amount >= 0)
);

CREATE TABLE ROLES(
    id BIGINT ,
    username VARCHAR(30) NOT NULL,
    authority VARCHAR(30) ,
    CONSTRAINT PK_ROLES PRIMARY KEY (id,authority),
    CONSTRAINT FK_ROLES_ID FOREIGN KEY (id) REFERENCES CREDENTIALS(Id) ON DELETE CASCADE
);