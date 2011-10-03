-- Role: mysecrets

-- DROP ROLE mysecrets;

CREATE ROLE mysecrets LOGIN
  ENCRYPTED PASSWORD 'md5e2a113378d7e84b8f0e0a3c856ab04e5'
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;
-- Database: mysecrets

-- DROP DATABASE mysecrets;

CREATE DATABASE mysecrets
  WITH OWNER = mysecrets
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'en_GB.UTF-8'
       LC_CTYPE = 'en_GB.UTF-8'
       CONNECTION LIMIT = -1;
