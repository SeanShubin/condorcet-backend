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
       count(candidate.id) as candidate_count
from election
         inner join user on election.owner_id = user.id
         left join candidate on election.id = candidate.election_id
where election.name = ?
group by election.id
