# 0. DB삭제
-- 외래 키 제약 조건 비활성화
SET FOREIGN_KEY_CHECKS = 0;

-- 테이블 삭제
DROP TABLE IF EXISTS Comment;
DROP TABLE IF EXISTS Answer;
DROP TABLE IF EXISTS Visit;
DROP TABLE IF EXISTS ChatMessage;
DROP TABLE IF EXISTS fav_animal;
DROP TABLE IF EXISTS Review;
DROP TABLE IF EXISTS Adoption;
DROP TABLE IF EXISTS Animals;
DROP TABLE IF EXISTS Inquiry;
DROP TABLE IF EXISTS ChattingRoom;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Shelter;
DROP TABLE IF EXISTS Species;
DROP TABLE IF EXISTS Image;
DROP TABLE IF EXISTS email_verify;

-- 데이터베이스 삭제 (요청하신 코드)
DROP DATABASE IF EXISTS CareMatch;

-- 외래 키 제약 조건 다시 활성화
SET FOREIGN_KEY_CHECKS = 1;

# 1. 스키마 생성
CREATE DATABASE IF NOT EXISTS CareMatch
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
USE CareMatch;

#2. 테이블 생성
CREATE TABLE Users (
    user_id VARCHAR(50) PRIMARY KEY,
    role VARCHAR(25),
    password VARCHAR(255),
    phone VARCHAR(15),
    email VARCHAR(100),
    user_createDate CHAR(50)
);

CREATE TABLE Shelter (
    shelter_id VARCHAR(50) PRIMARY KEY,
    shelter_name VARCHAR(100),
    telephone VARCHAR(20),
    address VARCHAR(200)
);

CREATE TABLE Species (
    kind_id VARCHAR(50) PRIMARY KEY,
    kind_name VARCHAR(50),
    type_name VARCHAR(50),
    category VARCHAR(50)
);

CREATE TABLE Image (
    image_id VARCHAR(50) PRIMARY KEY,
    image_url VARCHAR(200),
    image_data longblob
);

CREATE TABLE Animals (
    animal_id VARCHAR(50) PRIMARY KEY,
    age INT,
    weight FLOAT,
    sex CHAR(1),
    neutered CHAR(1),
    happenDate DATE,
    kind_id VARCHAR(50),
    shelter_id VARCHAR(50),
    status VARCHAR(50),
    image_id VARCHAR(50),
    animal_name VARCHAR(50)
);

CREATE TABLE Inquiry (
    inquiry_id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(100),
    content TEXT,
    created_date DATE,
    status VARCHAR(50),
    user_id VARCHAR(50)
);

CREATE TABLE Answer (
    answer_id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(100),
    content TEXT,
    created_date DATETIME,
    inquiry_id VARCHAR(50),
    user_id VARCHAR(50)
);

CREATE TABLE Review (
    review_id VARCHAR(50) PRIMARY KEY,
    created_date DATE,
    title VARCHAR(100),
    content TEXT,
    image_id VARCHAR(50),
    `like` INT,
    user_id VARCHAR(50),
    animal_id VARCHAR(50)
);

CREATE TABLE Comment (
    comment_id VARCHAR(50) PRIMARY KEY,
    created_date DATE,
    content TEXT,
    `like` INT,
    review_id VARCHAR(50),
    user_id VARCHAR(50)
);

CREATE TABLE Adoption (
    application_id VARCHAR(50) PRIMARY KEY,
    status VARCHAR(50),
    visit_date DATE,
    submitted_date DATE,
    user_id VARCHAR(50),
    animal_id VARCHAR(50),
    animal_reason TEXT
);

CREATE TABLE Visit (
    visit_id VARCHAR(50) PRIMARY KEY,
    purpose VARCHAR(100),
    status VARCHAR(50),
    visit_date DATE,
    submitted_date DATE,
    user_id VARCHAR(50),
    animal_id VARCHAR(50),
    application_id VARCHAR(50)
);

CREATE TABLE ChattingRoom (
    room_id INT PRIMARY KEY,
    user_id VARCHAR(50),
    created_date DATE
);

CREATE TABLE ChatMessage (
    message_id INT PRIMARY KEY,
    room_id INT,
    user_id VARCHAR(50),
    message TEXT,
    sent_date DATE,
    status BOOLEAN,
    image_id VARCHAR(50)
);

CREATE TABLE fav_animal (
    fav_id   INT PRIMARY KEY,
    animal_id VARCHAR(50),
    user_id   VARCHAR(50)
);

CREATE TABLE email_verify (
    fav_id     INT PRIMARY KEY,
    animal_id  VARCHAR(255),
    user_id    VARCHAR(50),
    verified   BOOLEAN DEFAULT FALSE,
    created_at DATE
);