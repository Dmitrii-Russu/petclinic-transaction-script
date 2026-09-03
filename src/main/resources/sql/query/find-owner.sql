SELECT
    o.id, o.first_name, o.last_name, o.street, o.city, o.telephone,
    COALESCE(pets.data, '[]'::jsonb) AS pets_json
FROM owners o
LEFT JOIN LATERAL (
    SELECT jsonb_agg(
        jsonb_build_object(
            'id',        p.id,
            'name',      p.name,
            'birthDate', p.birth_date,
            'type',      p.type,
            'visits',    COALESCE(visits.data, '[]'::jsonb)
        ) ORDER BY p.id
    ) AS data
    FROM pets p
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
    WHERE p.owner_id = o.id
) pets ON true
WHERE o.id = :id