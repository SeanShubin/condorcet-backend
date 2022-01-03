select event.id,
       event.`when`,
       event.authority,
       event.type,
       event.text
from event
order by event.id
