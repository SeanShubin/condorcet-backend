insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:24.586708', 'Alice', 'AddUser', '{"name":"Alice","email":"alice@email.com","salt":"3EDA01A7D056605C3C778E0179EC2AE3","hash":"4F9E9E2A359F92CAAFA507E7880638E15BF4F68D1A9250688CDA8B87221B68F8","role":"OWNER"}');
select id, `when`, authority, type, text
from event
where id > 0
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:24.789737', 'Bob', 'AddUser', '{"name":"Bob","email":"bob@email.com","salt":"79E90A5258764397DA853AFCED2F88E7","hash":"80C423B16D0C9AF27A95362F0878674736259463102B89CF9B2387E0F10EBB7C","role":"VOTER"}');
select id, `when`, authority, type, text
from event
where id > 1
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:24.923599', 'Carol', 'AddUser', '{"name":"Carol","email":"carol@email.com","salt":"E79BFDA7276912CC49F1DC28BA2E8CBD","hash":"5D3B307F0C357D4687FC3A658473C204DB825F831AB12B7DED00AF05E11EA914","role":"VOTER"}');
select id, `when`, authority, type, text
from event
where id > 2
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:25.034614', 'Dave', 'AddUser', '{"name":"Dave","email":"dave@email.com","salt":"299B27AEEF6F189B4C56090474AC5239","hash":"4BBF4E6580F022C50E1BFD10F9CF0DD3D310D1712BA4BE8F9296BEF3792C7CB5","role":"VOTER"}');
select id, `when`, authority, type, text
from event
where id > 3
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:25.168424', 'Eve', 'AddUser', '{"name":"Eve","email":"eve@email.com","salt":"11DD7B4117C780908F18A402A48C8079","hash":"98E94206D70222DCB5B7360C64E80898AF6BDC3ACEB046B8945A02B7515BB211","role":"VOTER"}');
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
values ('2022-01-21 06:18:26.362508', 'Alice', 'CastBallot', '{"voterName":"Alice","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Vanilla","rank":1},{"candidateName":"Chocolate","rank":2}],"confirmation":"D7ED737E6DF479C9EA2E6B28B416A733","now":"2022-01-21T06:18:26.350790Z"}');
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
values ('2022-01-21 06:18:26.488168', 'Alice', 'SetRankings', '{"confirmation":"D7ED737E6DF479C9EA2E6B28B416A733","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Chocolate Chip","rank":1},{"candidateName":"Neapolitan","rank":2},{"candidateName":"Chocolate","rank":3},{"candidateName":"Vanilla","rank":4},{"candidateName":"Butter Pecan","rank":5},{"candidateName":"Mint","rank":6}]}');
select id, `when`, authority, type, text
from event
where id > 18
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:26.515328', 'Alice', 'UpdateWhenCast', '{"confirmation":"D7ED737E6DF479C9EA2E6B28B416A733","now":"2022-01-21T06:18:26.481986Z"}');
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
values ('2022-01-21 06:18:27.195852', 'Bob', 'CastBallot', '{"voterName":"Bob","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Chocolate","rank":1},{"candidateName":"Chocolate Chip","rank":2},{"candidateName":"Vanilla","rank":3},{"candidateName":"Mint","rank":4},{"candidateName":"Butter Pecan","rank":5},{"candidateName":"Neapolitan","rank":6}],"confirmation":"B922EC70D29A75C87659972712FB67EC","now":"2022-01-21T06:18:27.194641Z"}');
select id, `when`, authority, type, text
from event
where id > 21
order by id;
insert into event (`when`, authority, type, text)
values ('2022-01-21 06:18:27.305997', 'Dave', 'CastBallot', '{"voterName":"Dave","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Mint","rank":1},{"candidateName":"Chocolate Chip","rank":2},{"candidateName":"Neapolitan","rank":3},{"candidateName":"Chocolate","rank":4},{"candidateName":"Vanilla","rank":5},{"candidateName":"Butter Pecan","rank":6}],"confirmation":"760D2BFB08D64A9F416FADDCAE168028","now":"2022-01-21T06:18:27.304843Z"}');
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
insert into event (`when`, authority, type, text)
values ('2022-03-10 17:44:01.658088', 'Alice', 'SetPassword', '{"userName":"Alice","salt":"13F7D3D7C224D1B50B32224CAE54C2D7","hash":"D1B89516283393E9597D2697F67EBD06027B10F2646C4785BD1E88A1F19DE1C3"}');
select id, `when`, authority, type, text
from event
where id > 24
order by id;
