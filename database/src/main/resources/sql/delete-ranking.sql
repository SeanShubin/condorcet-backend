insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
            where user.name = ?),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = ?
              and candidate.name = ?),
        ?)
