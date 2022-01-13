select candidate.name as candidate, ranking.rank as `rank`
from ballot
         inner join election on ballot.election_id = election.id
         inner join ranking on ranking.ballot_id = ballot.id
         inner join candidate on ranking.candidate_id = candidate.id
         inner join user on ballot.user_id = user.id
where user.name = ?
  and election.name = ?
