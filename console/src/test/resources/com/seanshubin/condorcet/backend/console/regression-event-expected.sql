insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:48.927303', 'Alice', 'AddUser', '{"name":"Alice","email":"alice@email.com","salt":"a09a049b-4228-4c21-b86d-35bc7a1ed3bb","hash":"08DDCCEBFB8973647C97B56F04F5F24BDDBB3F164B4DF22B1DB6EC3DFB9B5398","role":"OWNER"}');
select id, `when`, authority, type, text
from event
where id > 0
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:49.081708', 'Bob', 'AddUser', '{"name":"Bob","email":"bob@email.com","salt":"2ee67e26-d4d3-426a-9bcf-2b8b816ff2ce","hash":"DA6BCFE83821E48B41440D56EC9C34A9277520F69C26980E1ECD5ADE7B89813B","role":"UNASSIGNED"}');
select id, `when`, authority, type, text
from event
where id > 1
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:49.171624', 'Carol', 'AddUser', '{"name":"Carol","email":"carol@email.com","salt":"8c35dfe2-e4b9-4d66-8488-bb09a1f7451f","hash":"8398C9E8F1030A6BEE2F524A91A3FDF3C8AF4468BBC5CA90712D2AEE70B65240","role":"UNASSIGNED"}');
select id, `when`, authority, type, text
from event
where id > 2
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:49.476193', 'Dave', 'AddUser', '{"name":"Dave","email":"dave@email.com","salt":"11baa435-610b-4776-84c7-7201dbdf3dc7","hash":"2935594E22361D0DC1AB0118A6D589ECDF5119390FD408FBCAD60AFB22E2996E","role":"UNASSIGNED"}');
select id, `when`, authority, type, text
from event
where id > 3
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:49.566729', 'Eve', 'AddUser', '{"name":"Eve","email":"eve@email.com","salt":"3b12bd5b-0e7d-4519-86ff-0629ecdd5afb","hash":"00313095B4583C6B2498847968E56379891E7476C052DE7B104979CE004F141B","role":"UNASSIGNED"}');
select id, `when`, authority, type, text
from event
where id > 4
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:49.762353', 'Alice', 'SetRole', '{"name":"Bob","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 5
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:49.841237', 'Alice', 'SetRole', '{"name":"Dave","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 6
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:49.977763', 'Alice', 'SetRole', '{"name":"Eve","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 7
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:50.134133', 'Alice', 'RemoveUser', '{"name":"Carol"}');
select id, `when`, authority, type, text
from event
where id > 8
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:50.293079', 'Alice', 'AddElection', '{"owner":"Alice","name":"Delete Me"}');
select id, `when`, authority, type, text
from event
where id > 9
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:50.312971', 'Alice', 'AddElection', '{"owner":"Alice","name":"Favorite Ice Cream Flavor"}');
select id, `when`, authority, type, text
from event
where id > 10
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:50.383046', 'Alice', 'UpdateElection', '{"name":"Favorite Ice Cream Flavor","updates":{"newName":"Favorite Ice Cream","secretBallot":true,"clearNoVotingBefore":false,"noVotingBefore":"2021-02-03T04:55:30Z","clearNoVotingAfter":false,"noVotingAfter":"2022-02-03T04:55:30Z","allowVote":null,"allowEdit":null}}');
select id, `when`, authority, type, text
from event
where id > 11
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:50.396089', 'Alice', 'AddCandidates', '{"electionName":"Favorite Ice Cream","candidateNames":["Chocolate","Vanilla","Strawberry"]}');
select id, `when`, authority, type, text
from event
where id > 12
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:50.493329', 'Alice', 'AddVoters', '{"electionName":"Favorite Ice Cream","voterNames":["Alice","Bob","Dave"]}');
select id, `when`, authority, type, text
from event
where id > 13
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:50.519411', 'Alice', 'UpdateElection', '{"name":"Favorite Ice Cream","updates":{"newName":null,"secretBallot":null,"clearNoVotingBefore":null,"noVotingBefore":null,"clearNoVotingAfter":null,"noVotingAfter":null,"allowVote":true,"allowEdit":true}}');
select id, `when`, authority, type, text
from event
where id > 14
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:50.52543', 'Alice', 'CastBallot', '{"voterName":"Alice","electionName":"Favorite Ice Cream","rankings":[{"name":"Vanilla","rank":1},{"name":"Chocolate","rank":2}]}');
select id, `when`, authority, type, text
from event
where id > 15
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 18:52:08.744169', 'Alice', 'RemoveCandidates', '{"electionName":"Favorite Ice Cream","candidateNames":["Strawberry"]}');
select id, `when`, authority, type, text
from event
where id > 16
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 18:52:08.75218', 'Alice', 'AddCandidates', '{"electionName":"Favorite Ice Cream","candidateNames":["Butter Pecan","Neapolitan","Mint","Chocolate Chip"]}');
select id, `when`, authority, type, text
from event
where id > 17
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 18:52:08.756192', 'Alice', 'SetRankings', '{"voterName":"Favorite Ice Cream","electionName":"afee81f2-21cc-4fab-b630-770a08721686","rankings":[{"name":"Chocolate Chip","rank":1},{"name":"Neapolitan","rank":2},{"name":"Chocolate","rank":3},{"name":"Vanilla","rank":4},{"name":"Butter Pecan","rank":5},{"name":"Mint","rank":6}]}');
select id, `when`, authority, type, text
from event
where id > 18
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 19:21:10.7203', 'Alice', 'UpdateElection', '{"name":"Favorite Ice Cream","updates":{"newName":null,"secretBallot":null,"clearNoVotingBefore":null,"noVotingBefore":null,"clearNoVotingAfter":null,"noVotingAfter":null,"allowVote":false,"allowEdit":false}}');
select id, `when`, authority, type, text
from event
where id > 19
order by id;
insert into event (`when`, authority, type, text)
values ('2021-12-30 19:21:10.807202', 'Alice', 'DeleteElection', '{"name":"Delete Me"}');
select id, `when`, authority, type, text
from event
where id > 20
order by id;
select count(id)
from event;
select * from event;
insert into event (`when`, authority, type, text)
values ('2021-12-30 19:21:10.812729', 'Bob', 'CastBallot', '{"voterName":"Bob","electionName":"Favorite Ice Cream","rankings":[{"name":"Chocolate","rank":1},{"name":"Chocolate Chip","rank":2},{"name":"Vanilla","rank":3},{"name":"Mint","rank":4},{"name":"Butter Pecan","rank":5},{"name":"Neapolitan","rank":6}]}');
select id, `when`, authority, type, text
from event
where id > 21
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-10 18:26:27.791247', 'Dave', 'CastBallot', '{"voterName":"Dave","electionName":"Favorite Ice Cream","rankings":[{"name":"Mint","rank":1},{"name":"Chocolate Chip","rank":2},{"name":"Neapolitan","rank":3},{"name":"Chocolate","rank":4},{"name":"Vanilla","rank":5},{"name":"Butter Pecan","rank":6}]}');
select id, `when`, authority, type, text
from event
where id > 22
order by id;
