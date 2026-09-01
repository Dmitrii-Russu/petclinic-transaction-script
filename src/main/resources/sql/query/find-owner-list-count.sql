SELECT COUNT(*) FROM owners o
        WHERE (:lastNamePrefix IS NULL
        OR LOWER(o.last_name) LIKE LOWER(:lastNamePrefix) || '%' ESCAPE '\')
