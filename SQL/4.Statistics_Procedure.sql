DELIMITER $$
/*품종별 통계*/
CREATE PROCEDURE stats_species_breakdown (
  IN p_start DATE,
  IN p_end   DATE,
  IN p_shelter_id BIGINT
)
BEGIN
  SELECT
    s.kind_id,
    s.kind_name,
    COUNT(*) AS adoption_count
  FROM adoption a
  JOIN animals an
    ON (a.animal_id REGEXP '^[0-9]+$'
        AND an.animal_id = CAST(a.animal_id AS UNSIGNED))
  JOIN species s ON s.kind_id = an.kind_id
  WHERE a.visit_date >= p_start
    AND a.visit_date < (p_end + INTERVAL 1 DAY)
    AND (p_shelter_id IS NULL OR an.shelter_id = p_shelter_id)
  GROUP BY s.kind_id, s.kind_name
  ORDER BY adoption_count DESC, s.kind_name;
END $$

/*보호소별 통계*/
CREATE PROCEDURE stats_shelter_breakdown(
  IN p_start DATE,
  IN p_end   DATE,
  IN p_kind_id VARCHAR(50)
)
BEGIN
  SELECT
    an.shelter_id,
    COUNT(*) AS adoption_count
  FROM adoption a
  JOIN animals an
    ON (a.animal_id REGEXP '^[0-9]+$'
        AND an.animal_id = CAST(a.animal_id AS UNSIGNED))
  WHERE a.visit_date >= p_start
    AND a.visit_date < DATE_ADD(p_end, INTERVAL 1 DAY)
    AND (p_kind_id IS NULL OR an.kind_id = p_kind_id)
  GROUP BY an.shelter_id
  ORDER BY adoption_count DESC, an.shelter_id;
END $$

CREATE PROCEDURE state

DELIMITER ;
