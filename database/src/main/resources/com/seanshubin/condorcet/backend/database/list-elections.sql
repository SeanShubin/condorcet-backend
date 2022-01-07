select user.name              owner,
       election.name,
       election.secret_ballot,
       election.scheduled_start,
       election.scheduled_end,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.allow_changes_after_vote,
       election.is_open,
       count(candidate.id) as candidate_count
from election
         inner join user on election.owner_id = user.id
         left join candidate on candidate.election_id = election.id
group by election.id
