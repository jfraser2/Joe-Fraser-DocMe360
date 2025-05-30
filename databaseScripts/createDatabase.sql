-- create or load database from windows command line
sqlite3 <database_name.db>

PRAGMA foreign_keys = ON;

-- view the value
PRAGMA foreign_keys;

-- show databases default is main

.databases

-- create Templates Table
 CREATE TABLE IF NOT EXISTS main.Templates (id INTEGER PRIMARY KEY AUTOINCREMENT, body VARCHAR(200) NOT NULL);
 
 -- create Notifications Table
 CREATE TABLE IF NOT EXISTS main.Notifications (id INTEGER PRIMARY KEY AUTOINCREMENT, phone_number VARCHAR(20) NOT NULL,
  personalization VARCHAR(25), template_id INTEGER REFERENCES Templates(id) NOT NULL);

-- show tables
.tables

-- show foreign keys
PRAGMA foreign_key_list('Notifications');

-- persist to disk
.save va.db

-- exit sqlite command shell
.quit



