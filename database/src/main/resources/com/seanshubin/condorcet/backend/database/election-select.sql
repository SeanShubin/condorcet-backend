select user.name              owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.allow_changes_after_vote,
       election.is_open,
       count(candidate.id) as candidate_count
from election
         inner join user on election.owner_id = user.id
         left join candidate on election.id = candidate.election_id
group by election.id
