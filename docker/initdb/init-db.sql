CREATE USER lhv WITH ENCRYPTED PASSWORD 'changeme';
GRANT ALL ON SCHEMA public TO lhv;

CREATE DATABASE lhv_test;
GRANT ALL PRIVILEGES ON DATABASE lhv_test TO lhv;
ALTER DATABASE lhv_test OWNER TO lhv;