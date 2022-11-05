insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:14.303455', 'Alice', 'AddUser', '{"name":"Alice","email":"alice@email.com","salt":"LFIvycKxNVS0g8s6bA0Pq49dk9Kej6dnmwZOFFKG1Ic","hash":"as3YwX+MoNWB8Dlbk7vccm1z/bK3hl1pN/kaR2W6sPI","role":"OWNER"}');
select id, `when`, authority, type, text
from event
where id > 0
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:14.733557', 'Bob', 'AddUser', '{"name":"Bob","email":"bob@email.com","salt":"Q64BJiEeD3vxr0D4qOJPQ04+sbnHVKySyaUPahP5dCI","hash":"iWBf2M2HjvVbmzIShzlAC6yTwqieGaU8sh0r2BUY6Rg","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 1
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:14.854937', 'Carol', 'AddUser', '{"name":"Carol","email":"carol@email.com","salt":"KSlIxtAnbqk26NmxTk2eucYtae4WFUxLq60Bmknf9sQ","hash":"cPzoVu3rYjA9GeV1bxBKU6HMw8BCRnN0LkyGOfq2znc","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 2
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:14.962174', 'Dave', 'AddUser', '{"name":"Dave","email":"dave@email.com","salt":"csnTO/Ogqits+VtszeoNi2jgUOf8AHlcWbI7HWKxIhQ","hash":"/DzUvu1yOwztsHmPBLOjsM/LDLyffwrYBvwoUSNjyxM","role":"USER"}');
select id, `when`, authority, type, text
from event
where id > 3
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:15.069882', 'Eve', 'AddUser', '{"name":"Eve","email":"eve@email.com","salt":"584syS7RSw4/q9PlDbIAHH4rCs/6K9GZak2ALOsnJac","hash":"egzfsBOy49KnWgj/BuZySA4JRRTUF1lzW5XEuYvD+Og","role":"USER"}');
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
values ('2022-11-02 00:11:16.348205', 'Alice', 'CastBallot', '{"voterName":"Alice","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Vanilla","rank":1},{"candidateName":"Chocolate","rank":2}],"confirmation":"2PziIg7qU7INxq2Hup1trR5cnhC00caBVorUIkf4BDc","now":"2022-11-02T00:11:16.340168Z"}');
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
values ('2022-11-02 00:11:16.500031', 'Alice', 'SetRankings', '{"confirmation":"2PziIg7qU7INxq2Hup1trR5cnhC00caBVorUIkf4BDc","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Chocolate Chip","rank":1},{"candidateName":"Neapolitan","rank":2},{"candidateName":"Chocolate","rank":3},{"candidateName":"Vanilla","rank":4},{"candidateName":"Butter Pecan","rank":5},{"candidateName":"Mint","rank":6}]}');
select id, `when`, authority, type, text
from event
where id > 18
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:16.524856', 'Alice', 'UpdateWhenCast', '{"confirmation":"2PziIg7qU7INxq2Hup1trR5cnhC00caBVorUIkf4BDc","now":"2022-11-02T00:11:16.499172Z"}');
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
values ('2022-11-02 00:11:17.344327', 'Bob', 'CastBallot', '{"voterName":"Bob","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Chocolate","rank":1},{"candidateName":"Chocolate Chip","rank":2},{"candidateName":"Vanilla","rank":3},{"candidateName":"Mint","rank":4},{"candidateName":"Butter Pecan","rank":5},{"candidateName":"Neapolitan","rank":6}],"confirmation":"Q2crPhJGgW4CS0x+EkVH9Mc2aoNZGTBQyMx8JvWFl6I","now":"2022-11-02T00:11:17.343050Z"}');
select id, `when`, authority, type, text
from event
where id > 21
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 00:11:17.50783', 'Dave', 'CastBallot', '{"voterName":"Dave","electionName":"Favorite Ice Cream","rankings":[{"candidateName":"Mint","rank":1},{"candidateName":"Chocolate Chip","rank":2},{"candidateName":"Neapolitan","rank":3},{"candidateName":"Chocolate","rank":4},{"candidateName":"Vanilla","rank":5},{"candidateName":"Butter Pecan","rank":6}],"confirmation":"sdUhDdpjvoDuYLrzoYDSOVcZYoDOwSSDbE0IlFVrlpw","now":"2022-11-02T00:11:17.506646Z"}');
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
values ('2022-11-02 00:11:17.922758', 'Alice', 'SetPassword', '{"userName":"Alice","salt":"FYY+dmn+bZTJdUQOFvBdYc/83PQs5M6B4VAgaKb/2J8","hash":"+gIvAJlp0hI81UDIAv6It0mECt9F7+gh8it+AfkuJY0"}');
select id, `when`, authority, type, text
from event
where id > 24
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 18:11:19.187392', 'Alice', 'SetEmail', '{"userName":"Alice","email":"alice@email2"}');
select id, `when`, authority, type, text
from event
where id > 25
order by id;
insert into event (`when`, authority, type, text)
values ('2022-11-02 18:11:19.198439', 'Alice', 'SetUserName', '{"oldUserName":"Alice","newUserName":"Alice Smith"}');
select id, `when`, authority, type, text
from event
where id > 26
order by id;
