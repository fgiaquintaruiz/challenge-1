CREATE DATABASE naranjax;

USE naranjax;

CREATE TABLE account (
                         ID INT NOT NULL AUTO_INCREMENT,
                         name VARCHAR(100) NOT NULL,
                         dni INT NOT NULL,
                         active_card bool NOT NULL,
                         available_ammount BIGINT NOT NULL DEFAULT 0,
                         PRIMARY KEY (ID));

CREATE TABLE transaction (
                             transaction_id INT NOT NULL AUTO_INCREMENT,
                             account_id INT NOT NULL,
                             type VARCHAR(100) NOT NULL,
                             commerce VARCHAR(100) NOT NULL,
                             amount BIGINT NOT NULL,
                             time DATE NOT NULL,
                             PRIMARY KEY (transaction_id));