select id, `when`, authority, type, text
from event
where id > ?
order by id
