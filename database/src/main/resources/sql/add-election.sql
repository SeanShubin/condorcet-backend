insert into election (owner_id, name)
values ((select id from user where name = ?), ?)
