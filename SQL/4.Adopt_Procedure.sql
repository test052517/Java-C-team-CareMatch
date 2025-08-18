DROP PROCEDURE IF EXISTS stats_daily_adoptions;
DROP PROCEDURE IF EXISTS stats_monthly_adoptions;

DELIMITER $$

/* 일별 입양 건수 */
CREATE PROCEDURE stats_daily_adoptions (
    IN p_start DATE,
    IN p_end   DATE,
    IN p_shelter_id INT,
    IN p_kind_id    INT
)
BEGIN
    SELECT
        DATE(a.visit_date) AS d,
        COUNT(*) AS adoption_count
    FROM adoption a
    JOIN animals an ON an.animal_id = a.animal_id
    WHERE a.visit_date >= p_start
      AND a.visit_date < DATE_ADD(p_end, INTERVAL 1 DAY)
      AND (p_shelter_id IS NULL OR an.shelter_id = p_shelter_id)
      AND (p_kind_id    IS NULL OR an.kind_id    = p_kind_id)
    GROUP BY d
    ORDER BY d;
END $$

/* 월별 입양 건수 */
CREATE PROCEDURE stats_monthly_adoptions (
    IN p_start DATE,
    IN p_end   DATE,
    IN p_shelter_id INT,
    IN p_kind_id    INT
)
BEGIN
    SELECT
        DATE_FORMAT(a.visit_date, '%Y-%m') AS ym,
        COUNT(*) AS adoption_count
    FROM adoption a
    JOIN animals an ON an.animal_id = a.animal_id
    WHERE a.visit_date >= p_start
      AND a.visit_date < DATE_ADD(p_end, INTERVAL 1 DAY)
      AND (p_shelter_id IS NULL OR an.shelter_id = p_shelter_id)
      AND (p_kind_id    IS NULL OR an.kind_id    = p_kind_id)
    GROUP BY ym
    ORDER BY ym;
END $$

DELIMITER ;
