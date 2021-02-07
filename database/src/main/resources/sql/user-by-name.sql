select name,
       email,
       salt,
       hash,
       role
from user
where name = ?
