delete
from ranking
where ranking.ballot_id = (
    select id
    from ballot
    where ballot.user_id = (
        select id
        from user
        where name = ?)
      and ballot.election_id = (
        select id
        from election
        where name = ?))