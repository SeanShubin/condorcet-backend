select user.name as owner,
       election.name,
       election.start,
       election.end,
       election.secret,
       election.done_configuring,
       election.template,
       election.started,
       election.finished,
       election.can_change_candidates_after_done_configuring,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots
from election
         inner join user
                    on election.owner_id = user.id
where election.name = ?
