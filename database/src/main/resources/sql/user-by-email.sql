select name,
       email,
       salt,
       hash,
       role
from user
where email = ?
