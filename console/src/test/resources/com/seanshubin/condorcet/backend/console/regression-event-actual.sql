insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:24.586708', 'Alice', 'AddUser', '{"name":"Alice","email":"alice@email.com","salt":"acb9ef15-ee53-4921-a8e3-0e345bb17ddd","hash":"C3AB0D8CD392C7AB6230C154449B898FB14A943C637229DB9B08A6A949C4822C","role":"OWNER"}');
select id, `when`, authority, type, text
from event
where id > 0
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:24.789737', 'Bob', 'AddUser', '{"name":"Bob","email":"bob@email.com","salt":"3396f2cb-79bd-4d33-b01c-e9b2656c3ffd","hash":"81C48D4A77584F8099E1BBD23F892CBD0F7F96D122BC32F179E7D2DD8CF7004D","role":"OBSERVER"}');
select id, `when`, authority, type, text
from event
where id > 1
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:24.923599', 'Carol', 'AddUser', '{"name":"Carol","email":"carol@email.com","salt":"33b16b3e-1955-4fbc-9657-3d6f529786a1","hash":"1451CCFCB4D6806DE07F859983265353C83F555ADD86E1DF8F3B43F2A19FAAF0","role":"OBSERVER"}');
select id, `when`, authority, type, text
from event
where id > 2
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:25.034614', 'Dave', 'AddUser', '{"name":"Dave","email":"dave@email.com","salt":"8a844e78-61de-4b0d-9ffc-a9dfa2fe25ea","hash":"B8136925AF6F728CC31023CD0482A62F1EE2C3A876C70A4C020CA494B687AC0B","role":"OBSERVER"}');
select id, `when`, authority, type, text
from event
where id > 3
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:25.168424', 'Eve', 'AddUser', '{"name":"Eve","email":"eve@email.com","salt":"39ff9e68-45e9-439d-9c15-8781473dc04f","hash":"AF1753C62BD200523B3ECE11AC3FB79B064318841AFE0E6678FE51DCE5037BAD","role":"OBSERVER"}');
select id, `when`, authority, type, text
from event
where id > 4
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:25.470894', 'Alice', 'SetRole', '{"name":"Bob","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 5
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:25.534425', 'Alice', 'SetRole', '{"name":"Dave","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 6
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:25.585081', 'Alice', 'SetRole', '{"name":"Eve","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 7
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:25.647237', 'Alice', 'RemoveUser', '{"name":"Carol"}');
select id, `when`, authority, type, text
from event
where id > 8
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:25.782896', 'Alice', 'AddElection', '{"owner":"Alice","name":"Delete Me"}');
select id, `when`, authority, type, text
from event
where id > 9
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:25.834248', 'Alice', 'AddElection', '{"owner":"Alice","name":"Favorite Ice Cream Flavor"}');
select id, `when`, authority, type, text
from event
where id > 10
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:25.955359', 'Alice', 'UpdateElection', '{"name":"Favorite Ice Cream Flavor","updates":{"newElectionName":"Favorite Ice Cream","secretBallot":true,"clearNoVotingBefore":true,"noVotingBefore":null,"clearNoVotingAfter":true,"noVotingAfter":null,"allowVote":null,"allowEdit":null}}');
select id, `when`, authority, type, text
from event
where id > 11
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:26.036475', 'Alice', 'AddCandidates', '{"electionName":"Favorite Ice Cream","candidateNames":["Chocolate","Vanilla","Strawberry"]}');
select id, `when`, authority, type, text
from event
where id > 12
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:26.11518', 'Alice', 'AddVoters', '{"electionName":"Favorite Ice Cream","voterNames":["Alice","Bob","Dave"]}');
select id, `when`, authority, type, text
from event
where id > 13
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:26.273102', 'Alice', 'UpdateElection', '{"name":"Favorite Ice Cream","updates":{"newElectionName":null,"secretBallot":null,"clearNoVotingBefore":null,"noVotingBefore":null,"clearNoVotingAfter":null,"noVotingAfter":null,"allowVote":true,"allowEdit":true}}');
select id, `when`, authority, type, text
from event
where id > 14
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:26.362508', 'Alice', 'CastBallot', '{"voterName":"Alice","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Vanilla","rank":1},{"candidateName":"Chocolate","rank":2}],"confirmation":"2266d672-8185-4bab-9c96-e488b98ccd70","now":"2022-01-21T06:18:26.350790Z"}');
select id, `when`, authority, type, text
from event
where id > 15
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:26.428303', 'Alice', 'RemoveCandidates', '{"electionName":"Favorite Ice Cream","candidateNames":["Strawberry"]}');
select id, `when`, authority, type, text
from event
where id > 16
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:26.437569', 'Alice', 'AddCandidates', '{"electionName":"Favorite Ice Cream","candidateNames":["Butter Pecan","Neapolitan","Mint","Chocolate Chip"]}');
select id, `when`, authority, type, text
from event
where id > 17
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:26.488168', 'Alice', 'SetRankings', '{"confirmation":"2266d672-8185-4bab-9c96-e488b98ccd70","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Chocolate Chip","rank":1},{"candidateName":"Neapolitan","rank":2},{"candidateName":"Chocolate","rank":3},{"candidateName":"Vanilla","rank":4},{"candidateName":"Butter Pecan","rank":5},{"candidateName":"Mint","rank":6}]}');
select id, `when`, authority, type, text
from event
where id > 18
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:26.515328', 'Alice', 'UpdateWhenCast', '{"confirmation":"2266d672-8185-4bab-9c96-e488b98ccd70","now":"2022-01-21T06:18:26.481986Z"}');
select id, `when`, authority, type, text
from event
where id > 19
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:26.691208', 'Alice', 'DeleteElection', '{"name":"Delete Me"}');
select id, `when`, authority, type, text
from event
where id > 20
order by id;
select count(id)
from event;
select * from event;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:27.195852', 'Bob', 'CastBallot', '{"voterName":"Bob","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Chocolate","rank":1},{"candidateName":"Chocolate Chip","rank":2},{"candidateName":"Vanilla","rank":3},{"candidateName":"Mint","rank":4},{"candidateName":"Butter Pecan","rank":5},{"candidateName":"Neapolitan","rank":6}],"confirmation":"8f208935-c86b-408d-a7e5-9547ba866f97","now":"2022-01-21T06:18:27.194641Z"}');
select id, `when`, authority, type, text
from event
where id > 21
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:27.305997', 'Dave', 'CastBallot', '{"voterName":"Dave","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Mint","rank":1},{"candidateName":"Chocolate Chip","rank":2},{"candidateName":"Neapolitan","rank":3},{"candidateName":"Chocolate","rank":4},{"candidateName":"Vanilla","rank":5},{"candidateName":"Butter Pecan","rank":6}],"confirmation":"26e73b89-e82a-4194-ae52-31ec033ee04f","now":"2022-01-21T06:18:27.304843Z"}');
select id, `when`, authority, type, text
from event
where id > 22
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:27.42846', 'Alice', 'UpdateElection', '{"name":"Favorite Ice Cream","updates":{"newElectionName":null,"secretBallot":null,"clearNoVotingBefore":null,"noVotingBefore":null,"clearNoVotingAfter":null,"noVotingAfter":null,"allowVote":false,"allowEdit":false}}');
select id, `when`, authority, type, text
from event
where id > 23
order by id;
