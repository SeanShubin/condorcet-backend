select user.name
from user
     inner join voter on user.id = voter.user_id
     inner join election on user.id = voter.user_id
where election.name = ?
order by user.name
