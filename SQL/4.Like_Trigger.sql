DELIMITER $$
DROP PROCEDURE IF EXISTS sp_add_fav_count_column $$
CREATE PROCEDURE sp_add_fav_count_column()
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'Animals'
      AND COLUMN_NAME = 'fav_count'
  ) THEN
    ALTER TABLE Animals ADD COLUMN fav_count INT NOT NULL DEFAULT 0;
  END IF;
END $$
DELIMITER ;
CALL sp_add_fav_count_column();
DROP PROCEDURE IF EXISTS sp_add_fav_count_column;

-- 2) 기존 데이터로 초기 세팅
UPDATE Animals a
LEFT JOIN (
  SELECT animal_id, COUNT(*) AS cnt
  FROM fav_animal
  GROUP BY animal_id
) f ON f.animal_id = a.animal_id
SET a.fav_count = COALESCE(f.cnt, 0);

DELIMITER $$

-- 즐겨찾기 추가 시 fav_count +1
DROP TRIGGER IF EXISTS trg_fav_animal_after_insert $$
CREATE TRIGGER trg_fav_animal_after_insert
AFTER INSERT ON fav_animal
FOR EACH ROW
BEGIN
    UPDATE Animals
       SET fav_count = fav_count + 1
     WHERE animal_id = NEW.animal_id;
END $$

-- 즐겨찾기 삭제 시 fav_count -1 
DROP TRIGGER IF EXISTS trg_fav_animal_after_delete $$
CREATE TRIGGER trg_fav_animal_after_delete
AFTER DELETE ON fav_animal
FOR EACH ROW
BEGIN
    UPDATE Animals
       SET fav_count = CASE
                           WHEN fav_count > 0 THEN fav_count - 1
                           ELSE 0
                       END
     WHERE animal_id = OLD.animal_id;
END $$

DELIMITER ;
