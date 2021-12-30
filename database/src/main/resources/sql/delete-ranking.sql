delete
from ranking
where ranking.ballot_id = (
    select ballot.id
    from ballot
             inner join user on ballot.user_id = user.id
    where user.name = ?)
  and ranking.candidate_id = (
    select candidate.id
    from candidate
             inner join election on candidate.election_id = election.id
    where election.name = ?
      and candidate.name = ?)
