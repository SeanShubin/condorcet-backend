delete
from candidate
where election_id = (select id from election where name = ?)
  and candidate.name = ?
