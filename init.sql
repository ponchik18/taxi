-- init.sql
SELECT 'CREATE DATABASE passenger' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'passenger')
SELECT 'CREATE DATABASE driver' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'driver')
SELECT 'CREATE DATABASE payment' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'payment')
SELECT 'CREATE DATABASE rating' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'rating')
SELECT 'CREATE DATABASE promo_code' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'promo_code')
SELECT 'CREATE DATABASE rides' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'rides')