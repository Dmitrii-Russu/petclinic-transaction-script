SELECT o.id,
       o.first_name,
       o.last_name,
       o.street,
       o.city,
       o.telephone,
       pets.names AS pet_names
FROM owners o
LEFT JOIN LATERAL (
    SELECT array_agg(p.name ORDER BY p.name) AS names
    FROM pets p
    WHERE p.owner_id = o.id
) pets ON true
WHERE o.id IN (
    SELECT o2.id
    FROM owners o2
    WHERE (:lastNamePrefix IS NULL
           OR LOWER(o2.last_name) LIKE LOWER(:lastNamePrefix) || '%' ESCAPE '\')
    ORDER BY o2.id
    LIMIT :size
    OFFSET :offset
)
ORDER BY o.id