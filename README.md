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

## Chapter 5
```
CREATE KEYSPACE hotel WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': 3 };

CREATE TYPE hotel.address (
        street text,
        city text,
        state_or_province text,
        postal_code text,
        country text);

CREATE TABLE hotel.hotels_by_poi (
        poi_name text,
        hotel_id text,
        name text,
        phone text,
        address frozen<address>,
        PRIMARY KEY ((poi_name), hotel_id))
    WITH comment = 'Q1. Find hotels near given poi' AND
    CLUSTERING ORDER BY (hotel_id ASC);

CREATE TABLE hotel.hotels (
        id text PRIMARY KEY,
        name text,
        phone text,
        address frozen<address>,
        pois set<text>)
    WITH comment = 'Q2. Find information about a hotel';

CREATE TABLE hotel.pois_by_hotel (
        poi_name text,
        hotel_id text,
        description text,
        PRIMARY KEY ((hotel_id), poi_name))
    WITH comment = 'Q3. Find pois near a hotel';

CREATE TABLE hotel.available_rooms_by_hotel_date (
        hotel_id text,
        date date,
        room_number smallint,
        is_available boolean,
        PRIMARY KEY ((hotel_id), date, room_number))
    WITH comment = 'Q4. Find available rooms by hotel / date';

CREATE TABLE hotel.amenities_by_room (
        hotel_id text,
        room_number smallint,
        amenity_name text,
        description text,
        PRIMARY KEY ((hotel_id, room_number), amenity_name))
    WITH comment = 'Q5. Find amenities for a room';

CREATE KEYSPACE reservation WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': 3 };

CREATE TYPE reservation.address (
        street text,
        city text,
        state_or_province text,
        postal_code text,
        country text);

CREATE TABLE reservation.reservations_by_hotel_date (
        hotel_id text,
        start_date date,
        end_date date,
        room_number smallint,
        confirm_number text,
        guest_id uuid,
        PRIMARY KEY ((hotel_id, start_date), room_number))
    WITH comment = 'Q7. Find reservations by hotel and date';

CREATE MATERIALIZED VIEW reservation.reservations_by_confirmation AS
    SELECT *
        FROM reservation.reservations_by_hotel_date
        WHERE confirm_number IS NOT NULL AND
            hotel_id IS NOT NULL AND
            start_date IS NOT NULL AND
                room_number IS NOT NULL
    PRIMARY KEY (confirm_number, hotel_id, start_date, room_number);

CREATE TABLE reservation.reservations_by_guest (
        guest_last_name text,
        hotel_id text,
        start_date date,
        end_date date,
        room_number smallint,
        confirm_number text,
        guest_id uuid,
        PRIMARY KEY ((guest_last_name), hotel_id))
    WITH comment = 'Q8. Find reservations by guest name';

CREATE TABLE reservation.guests (
        guest_id uuid PRIMARY KEY,
        first_name text,
        last_name text,
        title text,
        emails set<text>,
        phone_numbers list<text>,
        addresses map<text, frozen<address>>,
        confirm_number text)
    WITH comment = 'Q9. Find guest by ID';
```
