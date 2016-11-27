# training-cassandra
Repository for training cassandra

## Chapter 3

```
CREATE KEYSPACE my_keyspace WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
DESCRIBE KEYSPACE my_keyspace;
USE my_keyspace;
CREATE TABLE user (first_name text, last_name text, PRIMARY KEY (first_name));
DESCRIBE TABLE user;
INSERT INTo uesr (first_name, last_name) VALUES ('Bill', 'Nguyen');
SELECT COUNT(*) FROM user;
SELECT * FROM user WHERE first_name='Bill';
DELETE last_name FROM user WHERE first_name='Bill';
SELECT * FROM user WHERE first_name='Bill';
DELETE FROM user WHERE first_name='Bill';
SELECT * FROM user WHERE first_name='Bill';
TRUNCATE user;
DROP TABLE user;
```

## Chapter4

```
ALTER TABLE user ADD title text;
DESCRIBE TABLE user;
INSERT INTO user (first_name, last_name, title) VALUES ('Bill', 'Nguyen', 'Mr.');
INSERT INTO user (first_name, last_name) VALUES ('Mary', 'Rodriguez');
SELECT * FROM user;
SELECT first_name, last_name, writetime(last_name) FROM user;
UPDATE user USING TIMESTAMP 1500000000000000 SET last_name = 'Boateng' WHERE first_name = 'Mary';  // Note that we maybe can't use an earlier date as timestamp.
SELECT first_name, last_name, writetime(last_name) FROM user;
SELECT first_name, last_name, TTL(last_name) FROM user WHERE first_name = 'Mary';
UPDATE user USING TTL 3600 SET last_name = 'McDonald' WHERE first_name = 'Mary';
SELECT first_name, last_name, TTL(last_name) FROM user WHERE first_name = 'Mary';
ALTER TABLE user ADD id uuid;
UPDATE user SET id = uuid() WHERE first_name = 'Mary';
SELECT first_name, id FROM user WHERE first_name = 'Mary';
ALTER TABLE user ADD emails set<text>;
UPDATE user SET emails = { 'mary@example.com' } WHERE first_name = 'Mary';
SELECT emails FROM user WHERE first_name = 'Mary';
UPDATE user SET emails = emails + { 'mary.mcdonald.AZ@gmail.com' } WHERE first_name = 'Mary';
SELECT emails FROM user WHERE first_name = 'Mary';
ALTER TABLE user ADD phone_numbers list<text>;
UPDATE user SET phone_numbers = ['1-800-999-9999'] WHERE first_name = 'Mary';
SELECT phone_numbers FROM user WHERE first_name = 'Mary';
UPDATE user SET phone_numbers = phone_numbers + ['480-111-1111'] WHERE first_name = 'Mary';
SELECT phone_numbers FROM user WHERE first_name = 'Mary';
UPDATE user SET phone_numbers[1] = '999-9999-9999' WHERE first_name = 'Mary';
SELECT phone_numbers FROM user WHERE first_name = 'Mary';
UPDATE user SET phone_numbers = phone_numbers - ['999-9999-9999'] WHERE first_name = 'Mary';
SELECT phone_numbers FROM user WHERE first_name = 'Mary';
DELETE phone_numbers[0] FROM user WHERE first_name = 'Mary';
SELECT phone_numbers FROM user WHERE first_name = 'Mary';
ALTER TABLE user ADD login_sessions map<timeuuid, int>;
UPDATE user SET login_sessions = { now(): 13, now(): 18 } WHERE first_name = 'Mary';
SELECT login_sessions FROM user WHERE first_name = 'Mary';
CREATE TYPE address (street text, city text, state text, zip_code int);
ALTER TABLE user ADD addresses map<text, frozen<address>>;
UPDATE user SET addresses = addresses + { 'home': { street: '7712 E. Broadway', city: 'Tucson', state: 'AZ', zip_code: 85715 } } WHERE first_name = 'Mary';
CREATE INDEX ON user ( last_name );
SELECT * FROM user WHERE last_name = 'Nguyen';
CREATE INDEX ON user ( addresses );
CREATE INDEX ON user ( emails );
CREATE INDEX ON user ( phone_numbers );
DROP INDEX user_last_name_idx;
```
