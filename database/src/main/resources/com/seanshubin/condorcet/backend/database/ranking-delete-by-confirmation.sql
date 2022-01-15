delete from ranking
where ballot_id = (select id from ballot where confirmation = ?)
