select user.name           as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.allow_edit,
       election.allow_vote,
       (select count(candidate.id) from candidate inner join election on candidate.election_id = election.id) as candidate_count,
       (select count(voter.id) from voter inner join election on voter.election_id = election.id) as voter_count
from election
         inner join user on election.owner_id = user.id
where election.name = ?
