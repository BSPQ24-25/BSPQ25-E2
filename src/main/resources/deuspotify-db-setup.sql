-- Script para crear la base de datos y el usuario para poder hacer cosas con los tests
CREATE DATABASE IF NOT EXISTS deuspotify;

-- Crear usuario para conexiones remotas
CREATE USER IF NOT EXISTS 'actions'@'%' IDENTIFIED BY 'actions';

-- Conceder privilegios al usuario
GRANT ALL PRIVILEGES ON deuspotify.* TO 'actions'@'%' WITH GRANT OPTION;

-- Aplicar cambios
FLUSH PRIVILEGES;