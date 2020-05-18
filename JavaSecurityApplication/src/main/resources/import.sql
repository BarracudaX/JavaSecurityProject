INSERT INTO CREDENTIALS(username,password) VALUES('BobMarly123','{noop}secret123');
INSERT INTO CREDENTIALS(username,password) VALUES('USER','{noop}USER');
INSERT INTO CREDENTIALS(username,password) VALUES('ATTACKER','{noop}ATTACKER');
INSERT INTO CREDENTIALS(username,password) VALUES('Pitter2012','{noop}Pitter2013');
INSERT INTO CREDENTIALS(username,password) VALUES('John20012','{noop}AXSA12SA21A!$12A');
INSERT INTO CREDENTIALS(username,password) VALUES('username','{noop}password');
INSERT INTO CREDENTIALS(username,password) VALUES('admin','{noop}0012a002!@2');

INSERT INTO ROLES(id,username,authority) VALUES((SELECT id FROM CREDENTIALS WHERE username = 'USER'),'USER','ROLE_USER');
INSERT INTO ROLES(id,username,authority) VALUES((SELECT id FROM CREDENTIALS WHERE username = 'ATTACKER'),'ATTACKER','ROLE_USER');

INSERT INTO BANK_ACCOUNT(id,username,amount) VALUES((SELECT id FROM CREDENTIALS WHERE username = 'USER'),'USER',1000);
INSERT INTO BANK_ACCOUNT(id,username,amount) VALUES((SELECT id FROM CREDENTIALS WHERE username = 'ATTACKER'),'ATTACKER',1000);
