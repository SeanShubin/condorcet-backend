select election.id,
       election.owner_id,
       user.name owner,
       election.name,
       election.secret_ballot,
       election.scheduled_start,
       election.scheduled_end,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.no_more_changes,
       election.is_open
from election
         inner join user on election.owner_id = user.id
