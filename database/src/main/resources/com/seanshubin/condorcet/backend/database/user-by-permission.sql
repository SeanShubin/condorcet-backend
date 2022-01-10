select user.name
from user
         inner join role_permission on user.role = role_permission.role
where permission = ?
order by user.name;
