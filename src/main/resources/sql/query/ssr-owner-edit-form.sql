SELECT
    id,
    first_name,
    last_name,
    street,
    city,
    telephone
FROM owners
WHERE id = :ownerId