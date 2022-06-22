select user.name as user_name, election.name as election_name, confirmation, when_cast
from ballot
         inner join user on ballot.user_id = user.id
         inner join election on ballot.election_id = election.id
where user.name = ?
  and election.name = ?
