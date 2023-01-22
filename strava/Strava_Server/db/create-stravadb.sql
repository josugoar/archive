/* DELETE 'stravaDB' database*/
DROP SCHEMA IF EXISTS stravaDB;
/* DELETE USER 'strava_user' AT LOCAL SERVER*/
DROP USER IF EXISTS 'strava_user'@'localhost:3306';

/* CREATE ''stravaDB' DATABASE */
CREATE SCHEMA IF NOT EXISTS stravaDB;
/* CREATE THE USER 'strava_user' AT LOCAL SERVER WITH PASSWORD 'Password1?' */
CREATE USER IF NOT EXISTS 'strava_user'@'localhost:3306' IDENTIFIED BY 'Password1?';
/* GRANT FULL ACCESS TO THE DATABASE FOR THE USER 'strava_user' AT LOCAL SERVER*/
GRANT ALL ON stravaDB.* TO 'strava_user'@'localhost:3306';
