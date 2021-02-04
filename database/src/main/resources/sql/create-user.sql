insert into user (name,
                  email,
                  salt,
                  hash,
                  role_id)
values (?, ?, ?, ?, (select id from role where name = ?))
