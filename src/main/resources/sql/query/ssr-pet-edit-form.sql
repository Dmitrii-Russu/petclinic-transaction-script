SELECT
    p.id          AS id,
    p.name        AS name,
    p.birth_date  AS birth_date,
    p.type        AS type,
    o.id          AS owner_id,
    o.first_name  AS first_name,
    o.last_name   AS last_name
FROM pets p
JOIN owners o ON o.id = p.owner_id
WHERE p.id = :id