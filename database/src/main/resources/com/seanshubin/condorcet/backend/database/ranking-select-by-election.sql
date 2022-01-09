select
    ballot.id,
    user.name user,
    election.name election,
    ballot.confirmation,
    ballot.when_cast,
    ranking.rank,
    candidate.name candidate
from ballot
         inner join ranking on ballot.id = ranking.ballot_id
         inner join user on ballot.user_id = user.id
         inner join election on ballot.election_id = election.id
         inner join candidate on ranking.candidate_id = candidate.id
where election.name = ?
order by ballot.confirmation, ranking.rank, candidate.name
