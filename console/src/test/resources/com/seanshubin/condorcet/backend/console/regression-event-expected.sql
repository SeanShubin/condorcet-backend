insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:43.136621', 'Alice', 'AddUser', '{"name":"Alice","email":"alice@email.com","salt":"86E2763B5F192DCA4CEC3CE22023E273","hash":"86765C5FC67880E5A65E15B5259242AC8AB8CAA3FE9F62AF00447626228ED4FF","role":"OWNER"}');
select id, `when`, authority, type, text
from event
where id > 0
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:43.876718', 'Bob', 'AddUser', '{"name":"Bob","email":"bob@email.com","salt":"95127FEBEA8BD883B729D2D84CF4BC51","hash":"A820B9ACBBAE29C9D8C95AA85450ACF3BB911A4D80737E2F1BE9D51BCE2ECB1A","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 1
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:43.985961', 'Carol', 'AddUser', '{"name":"Carol","email":"carol@email.com","salt":"168A7E74F7E93F3AD3FFF89BCAEB8965","hash":"B6EA0C8348FC80D297B22779874F224DCD6E01D6140BA1547D677F9168C2E4AC","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 2
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:44.122167', 'Dave', 'AddUser', '{"name":"Dave","email":"dave@email.com","salt":"28E7E88968AEA760BDC7E9C27EB29083","hash":"797E878E90C3936917ED74B3CE92915F7373ECBEA023107F437321F7AD1D5FFA","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 3
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:44.221046', 'Eve', 'AddUser', '{"name":"Eve","email":"eve@email.com","salt":"5CD781EC81444AAC3B43D60DAD621B15","hash":"E79D34E6D38A14C470ABFD5B840C5E8217DDC7C66D9F49A66C6EC7A78D2C9D7E","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 4
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:44.575872', 'Alice', 'SetRole', '{"name":"Bob","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 5
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:44.633436', 'Alice', 'SetRole', '{"name":"Dave","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 6
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:44.678235', 'Alice', 'SetRole', '{"name":"Eve","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 7
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:44.742803', 'Alice', 'RemoveUser', '{"name":"Carol"}');
select id, `when`, authority, type, text
from event
where id > 8
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:44.86151', 'Alice', 'AddElection', '{"owner":"Alice","name":"Delete Me"}');
select id, `when`, authority, type, text
from event
where id > 9
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:44.91723', 'Alice', 'AddElection', '{"owner":"Alice","name":"Favorite Ice Cream Flavor"}');
select id, `when`, authority, type, text
from event
where id > 10
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:44.994712', 'Alice', 'UpdateElection', '{"name":"Favorite Ice Cream Flavor","updates":{"newElectionName":"Favorite Ice Cream","secretBallot":true,"clearNoVotingBefore":true,"noVotingBefore":null,"clearNoVotingAfter":true,"noVotingAfter":null,"allowVote":null,"allowEdit":null}}');
select id, `when`, authority, type, text
from event
where id > 11
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:45.067256', 'Alice', 'AddCandidates', '{"electionName":"Favorite Ice Cream","candidateNames":["Chocolate","Vanilla","Strawberry"]}');
select id, `when`, authority, type, text
from event
where id > 12
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:45.138625', 'Alice', 'AddVoters', '{"electionName":"Favorite Ice Cream","voterNames":["Alice","Bob","Dave"]}');
select id, `when`, authority, type, text
from event
where id > 13
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:45.295126', 'Alice', 'UpdateElection', '{"name":"Favorite Ice Cream","updates":{"newElectionName":null,"secretBallot":null,"clearNoVotingBefore":null,"noVotingBefore":null,"clearNoVotingAfter":null,"noVotingAfter":null,"allowVote":true,"allowEdit":true}}');
select id, `when`, authority, type, text
from event
where id > 14
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:45.380475', 'Alice', 'CastBallot', '{"voterName":"Alice","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Vanilla","rank":1},{"candidateName":"Chocolate","rank":2}],"confirmation":"8FDE24EC97C067F3315B31120D97ADE0","now":"2022-03-13T01:21:45.378690Z"}');
select id, `when`, authority, type, text
from event
where id > 15
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:45.44093', 'Alice', 'RemoveCandidates', '{"electionName":"Favorite Ice Cream","candidateNames":["Strawberry"]}');
select id, `when`, authority, type, text
from event
where id > 16
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:45.452531', 'Alice', 'AddCandidates', '{"electionName":"Favorite Ice Cream","candidateNames":["Butter Pecan","Neapolitan","Mint","Chocolate Chip"]}');
select id, `when`, authority, type, text
from event
where id > 17
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:45.504489', 'Alice', 'SetRankings', '{"confirmation":"8FDE24EC97C067F3315B31120D97ADE0","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Chocolate Chip","rank":1},{"candidateName":"Neapolitan","rank":2},{"candidateName":"Chocolate","rank":3},{"candidateName":"Vanilla","rank":4},{"candidateName":"Butter Pecan","rank":5},{"candidateName":"Mint","rank":6}]}');
select id, `when`, authority, type, text
from event
where id > 18
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:45.53352', 'Alice', 'UpdateWhenCast', '{"confirmation":"8FDE24EC97C067F3315B31120D97ADE0","now":"2022-03-13T01:21:45.503531Z"}');
select id, `when`, authority, type, text
from event
where id > 19
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:45.7038', 'Alice', 'DeleteElection', '{"name":"Delete Me"}');
select id, `when`, authority, type, text
from event
where id > 20
order by id;
select count(id)
from event;
select * from event;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:46.255331', 'Bob', 'CastBallot', '{"voterName":"Bob","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Chocolate","rank":1},{"candidateName":"Chocolate Chip","rank":2},{"candidateName":"Vanilla","rank":3},{"candidateName":"Mint","rank":4},{"candidateName":"Butter Pecan","rank":5},{"candidateName":"Neapolitan","rank":6}],"confirmation":"89D8015719AB344F11FE6E8D9B4F419F","now":"2022-03-13T01:21:46.254619Z"}');
select id, `when`, authority, type, text
from event
where id > 21
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:46.384461', 'Dave', 'CastBallot', '{"voterName":"Dave","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Mint","rank":1},{"candidateName":"Chocolate Chip","rank":2},{"candidateName":"Neapolitan","rank":3},{"candidateName":"Chocolate","rank":4},{"candidateName":"Vanilla","rank":5},{"candidateName":"Butter Pecan","rank":6}],"confirmation":"ABCD6DC81CA9327D2ACAEEF758B61414","now":"2022-03-13T01:21:46.383634Z"}');
select id, `when`, authority, type, text
from event
where id > 22
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:46.531978', 'Alice', 'UpdateElection', '{"name":"Favorite Ice Cream","updates":{"newElectionName":null,"secretBallot":null,"clearNoVotingBefore":null,"noVotingBefore":null,"clearNoVotingAfter":null,"noVotingAfter":null,"allowVote":false,"allowEdit":false}}');
select id, `when`, authority, type, text
from event
where id > 23
order by id;
insert into event (`when`, authority, type, text)
values ('2022-03-13 01:21:46.733358', 'Alice', 'SetPassword', '{"userName":"Alice","salt":"7F0BD8C0EEF466D7D18420565E333532","hash":"B8F9FABF861F0A40C7F487665D2D5D1B9333A51C3EF5A8B749261884753ED2E0"}');
select id, `when`, authority, type, text
from event
where id > 24
order by id;
