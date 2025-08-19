USE mydb;
DELIMITER $$
DROP PROCEDURE IF EXISTS drop_except_zip $$
CREATE PROCEDURE drop_except_zip(IN in_db VARCHAR(64))
BEGIN
  DECLARE done INT DEFAULT 0;
  DECLARE tname VARCHAR(64);
  DECLARE cur CURSOR FOR
    SELECT table_name FROM information_schema.tables
    WHERE table_schema=in_db AND table_name <> 'tblzipcode';
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1;
  SET FOREIGN_KEY_CHECKS=0;
  OPEN cur;
  read_loop: LOOP
    FETCH cur INTO tname;
    IF done=1 THEN LEAVE read_loop; END IF;
    SET @sql = CONCAT('DROP TABLE IF EXISTS `', in_db, '`.`', tname, '`');
    PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
  END LOOP;
  CLOSE cur;
  SET FOREIGN_KEY_CHECKS=1;
END $$
DELIMITER ;
CALL drop_except_zip('mydb');
DROP PROCEDURE drop_except_zip;

CREATE TABLE IF NOT EXISTS users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  login_id VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  name VARCHAR(100) NOT NULL,
  role ENUM('ADMIN','USER') NOT NULL DEFAULT 'USER',
  status ENUM('ONLINE','OFFLINE') DEFAULT 'OFFLINE',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS chatting_room (
  room_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  type ENUM('DM','GROUP') NOT NULL,
  title VARCHAR(120),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS chatting_room_member (
  room_id BIGINT NOT NULL,
  user_id INT NOT NULL,
  last_read_message_id BIGINT DEFAULT NULL,
  joined_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (room_id, user_id),
  CONSTRAINT fk_crm_room FOREIGN KEY (room_id) REFERENCES chatting_room(room_id) ON DELETE CASCADE,
  CONSTRAINT fk_crm_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS chatting_message (
  message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  room_id BIGINT NOT NULL,
  sender_id INT NOT NULL,
  content TEXT,
  content_type ENUM('TEXT','IMAGE') NOT NULL DEFAULT 'TEXT',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_room_created (room_id, created_at),
  CONSTRAINT fk_cm_room FOREIGN KEY (room_id) REFERENCES chatting_room(room_id) ON DELETE CASCADE,
  CONSTRAINT fk_cm_user FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS chat_image (
  image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  message_id BIGINT NOT NULL,
  file_path VARCHAR(255) NOT NULL,
  original_name VARCHAR(255),
  size_bytes BIGINT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_ci_message FOREIGN KEY (message_id) REFERENCES chatting_message(message_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO users (login_id, password, name, role) VALUES
('aaa',   '1234', '관리자', 'ADMIN'),
('user1', '1234', 'user1',  'USER'),
('user2', '1234', 'user2',  'USER')
ON DUPLICATE KEY UPDATE name=VALUES(name), role=VALUES(role);

INSERT INTO chatting_room (type, title) VALUES ('DM', NULL);
SET @room1 := LAST_INSERT_ID();
INSERT IGNORE INTO chatting_room_member (room_id, user_id)
SELECT @room1, user_id FROM users WHERE login_id IN ('aaa','user1');
INSERT INTO chatting_message (room_id, sender_id, content, content_type)
SELECT @room1, (SELECT user_id FROM users WHERE login_id='aaa'), '안녕하세요! 테스트 DM입니다.', 'TEXT';
