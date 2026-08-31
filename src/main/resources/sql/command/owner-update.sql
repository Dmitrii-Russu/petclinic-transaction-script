UPDATE owners
SET first_name = :firstName,
    last_name  = :lastName,
    street     = :street,
    city       = :city,
    telephone  = :telephone
WHERE id = :id;