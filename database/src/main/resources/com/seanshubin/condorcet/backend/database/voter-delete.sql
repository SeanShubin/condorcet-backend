delete
from voter
where election_id = (select id from election where name = ?)
  and user_id = (select id from user where name = ?)
