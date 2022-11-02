insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:14.303455', 'Alice', 'AddUser', '{"name":"Alice","email":"alice@email.com","salt":"mu69MMmRyEyWfgfIJKMnaMDm9LejGHrl/3NxaIBXr7o=","hash":"6Wz4XZhc1TVHac9r2U57NGQftRrIzx4dc0OxIkU371g=","role":"OWNER"}');
select id, `when`, authority, type, text
from event
where id > 0
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:14.733557', 'Bob', 'AddUser', '{"name":"Bob","email":"bob@email.com","salt":"spjK5ffE5/CNFFL5uLxTSIEEvrllIqVS8XmzgjzbLVU=","hash":"Au9pUzRKN04g0G/64yUs+2DFzsfq2m4gSPqh4Sj9P2k=","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 1
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:14.854937', 'Carol', 'AddUser', '{"name":"Carol","email":"carol@email.com","salt":"YWh8eaWRk+eHNGFJwFQWubm/xaqIO26ZcjWoiOs+CMc=","hash":"agOUHYCjA3JXQ3euSE2KZUf1ot8keb+IKsD2Z2gmbzw=","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 2
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:14.962174', 'Dave', 'AddUser', '{"name":"Dave","email":"dave@email.com","salt":"x3s3jABExhlX+MeJl5YtIUKlheujRsDvlp4qB4ntn9Y=","hash":"OJ24dKzC0MDfTmlvDVvfdFlPNJPrRkQteHXVwGWq7LE=","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 3
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:15.069882', 'Eve', 'AddUser', '{"name":"Eve","email":"eve@email.com","salt":"D+fWYeIiF0vntWjpGLPbKqOKgSaoJat5PswAuvLBo3o=","hash":"MjLabSeHJFlRlA+0lfpQG1NraABKZXFB4j/VIlTLklU=","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 4
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:15.399983', 'Alice', 'SetRole', '{"name":"Bob","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 5
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:15.459053', 'Alice', 'SetRole', '{"name":"Dave","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 6
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:15.515492', 'Alice', 'SetRole', '{"name":"Eve","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 7
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:15.577377', 'Alice', 'RemoveUser', '{"name":"Carol"}');
select id, `when`, authority, type, text
from event
where id > 8
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:15.697616', 'Alice', 'AddElection', '{"owner":"Alice","name":"Delete Me"}');
select id, `when`, authority, type, text
from event
where id > 9
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:15.751015', 'Alice', 'AddElection', '{"owner":"Alice","name":"Favorite Ice Cream Flavor"}');
select id, `when`, authority, type, text
from event
where id > 10
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:15.819851', 'Alice', 'UpdateElection', '{"name":"Favorite Ice Cream Flavor","updates":{"newElectionName":"Favorite Ice Cream","secretBallot":true,"clearNoVotingBefore":true,"noVotingBefore":null,"clearNoVotingAfter":true,"noVotingAfter":null,"allowVote":null,"allowEdit":null}}');
select id, `when`, authority, type, text
from event
where id > 11
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:15.93123', 'Alice', 'AddCandidates', '{"electionName":"Favorite Ice Cream","candidateNames":["Chocolate","Vanilla","Strawberry"]}');
select id, `when`, authority, type, text
from event
where id > 12
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:16.006926', 'Alice', 'AddVoters', '{"electionName":"Favorite Ice Cream","voterNames":["Alice","Bob","Dave"]}');
select id, `when`, authority, type, text
from event
where id > 13
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:16.253951', 'Alice', 'UpdateElection', '{"name":"Favorite Ice Cream","updates":{"newElectionName":null,"secretBallot":null,"clearNoVotingBefore":null,"noVotingBefore":null,"clearNoVotingAfter":null,"noVotingAfter":null,"allowVote":true,"allowEdit":true}}');
select id, `when`, authority, type, text
from event
where id > 14
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:16.348205', 'Alice', 'CastBallot', '{"voterName":"Alice","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Vanilla","rank":1},{"candidateName":"Chocolate","rank":2}],"confirmation":"2Khjmwi9q8y08gnYQ7ioUoaSmdanqDogMp66NwvQRpc=","now":"2022-11-02T00:11:16.340168Z"}');
select id, `when`, authority, type, text
from event
where id > 15
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:16.419399', 'Alice', 'RemoveCandidates', '{"electionName":"Favorite Ice Cream","candidateNames":["Strawberry"]}');
select id, `when`, authority, type, text
from event
where id > 16
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:16.435063', 'Alice', 'AddCandidates', '{"electionName":"Favorite Ice Cream","candidateNames":["Butter Pecan","Neapolitan","Mint","Chocolate Chip"]}');
select id, `when`, authority, type, text
from event
where id > 17
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:16.500031', 'Alice', 'SetRankings', '{"confirmation":"2Khjmwi9q8y08gnYQ7ioUoaSmdanqDogMp66NwvQRpc=","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Chocolate Chip","rank":1},{"candidateName":"Neapolitan","rank":2},{"candidateName":"Chocolate","rank":3},{"candidateName":"Vanilla","rank":4},{"candidateName":"Butter Pecan","rank":5},{"candidateName":"Mint","rank":6}]}');
select id, `when`, authority, type, text
from event
where id > 18
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:16.524856', 'Alice', 'UpdateWhenCast', '{"confirmation":"2Khjmwi9q8y08gnYQ7ioUoaSmdanqDogMp66NwvQRpc=","now":"2022-11-02T00:11:16.499172Z"}');
select id, `when`, authority, type, text
from event
where id > 19
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:16.723191', 'Alice', 'DeleteElection', '{"name":"Delete Me"}');
select id, `when`, authority, type, text
from event
where id > 20
order by id;
select count(id)
from event;
select * from event;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:17.344327', 'Bob', 'CastBallot', '{"voterName":"Bob","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Chocolate","rank":1},{"candidateName":"Chocolate Chip","rank":2},{"candidateName":"Vanilla","rank":3},{"candidateName":"Mint","rank":4},{"candidateName":"Butter Pecan","rank":5},{"candidateName":"Neapolitan","rank":6}],"confirmation":"jUubd/FehXhCOlhNsLegZdddQS04clejQc6DQZYv44Y=","now":"2022-11-02T00:11:17.343050Z"}');
select id, `when`, authority, type, text
from event
where id > 21
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:17.50783', 'Dave', 'CastBallot', '{"voterName":"Dave","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Mint","rank":1},{"candidateName":"Chocolate Chip","rank":2},{"candidateName":"Neapolitan","rank":3},{"candidateName":"Chocolate","rank":4},{"candidateName":"Vanilla","rank":5},{"candidateName":"Butter Pecan","rank":6}],"confirmation":"39zeqJl2itJ9cX+exU4rYE8JN9RS50fP2khSIqud0oc=","now":"2022-11-02T00:11:17.506646Z"}');
select id, `when`, authority, type, text
from event
where id > 22
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:17.696692', 'Alice', 'UpdateElection', '{"name":"Favorite Ice Cream","updates":{"newElectionName":null,"secretBallot":null,"clearNoVotingBefore":null,"noVotingBefore":null,"clearNoVotingAfter":null,"noVotingAfter":null,"allowVote":false,"allowEdit":false}}');
select id, `when`, authority, type, text
from event
where id > 23
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:17.922758', 'Alice', 'SetPassword', '{"userName":"Alice","salt":"GZvFrxTJryoDbOeymIAKG6EAsz199NFmd4S8jN8+EmI=","hash":"gWzGUtpikw5JDydU1qRn6z4rLo/Zt+XkvRbgAU/+ah8="}');
select id, `when`, authority, type, text
from event
where id > 24
order by id;
