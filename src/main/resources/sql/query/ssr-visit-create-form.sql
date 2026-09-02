SELECT
    p.id          AS pet_id,
    p.name        AS pet_name,
    p.birth_date  AS birth_date,
    p.type        AS type,
    o.id          AS owner_id,
    o.first_name  AS owner_first_name,
    o.last_name   AS owner_last_name,
    COALESCE(visits.data, '[]'::jsonb) AS visits_json
FROM pets p
JOIN owners o ON o.id = p.owner_id
LEFT JOIN LATERAL (
    SELECT jsonb_agg(
        jsonb_build_object(
            'id',          v.id,
            'visitDate',   v.visit_date,
            'description', v.description
        ) ORDER BY v.id
    ) AS data
    FROM visits v
    WHERE v.pet_id = p.id
) visits ON true
WHERE p.id = :petId