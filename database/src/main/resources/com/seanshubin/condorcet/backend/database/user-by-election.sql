select user.name
from user
     inner join voter on user.id = voter.user_id
     inner join election on election.id = voter.election_id
where election.name = ?
order by user.name
