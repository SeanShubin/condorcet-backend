insert into ballot (user_id, election_id, confirmation, when_cast)
values ((select id from user where name = ?),
        (select id from election where name = ?),
        ?,
        ?)
