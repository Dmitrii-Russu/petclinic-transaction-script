UPDATE pets
SET name       = :name,
    birth_date = :birthDate,
    type       = :type
WHERE id       = :id
  AND owner_id = :ownerId;