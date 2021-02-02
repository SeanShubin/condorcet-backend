select id, `when`, type, text
from event
where id > ?
order by id
