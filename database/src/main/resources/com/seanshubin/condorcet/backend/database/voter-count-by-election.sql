select count(voter.id)
from voter
         inner join election
                    on voter.election_id = election.id
where election.name = ?
