DELIMITER $$
/* 동물 좋아요 TOP_N*/
ALTER TABLE fav_animal 
ADD COLUMN created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

CREATE PROCEDURE status_fav_animal_topN (
    IN p_start DATE,
    IN p_end   DATE,
    IN p_limit INT
)
BEGIN
    SELECT
        fa.animal_id,
        COUNT(*) AS fav_count
    FROM fav_animal fa
    WHERE fa.created_at >= p_start
      AND fa.created_at < DATE_ADD(p_end, INTERVAL 1 DAY)
    GROUP BY fa.animal_id
    ORDER BY fav_count DESC, fa.animal_id
    LIMIT p_limit;
END $$

DELIMITER ;
