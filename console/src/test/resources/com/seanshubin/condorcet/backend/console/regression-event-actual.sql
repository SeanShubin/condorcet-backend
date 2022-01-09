insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:48.927303', 'Alice', 'AddUser', '{"name":"Alice","email":"alice@email.com","salt":"a09a049b-4228-4c21-b86d-35bc7a1ed3bb","hash":"08DDCCEBFB8973647C97B56F04F5F24BDDBB3F164B4DF22B1DB6EC3DFB9B5398","role":"OWNER"}');
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:49.081708', 'Bob', 'AddUser', '{"name":"Bob","email":"bob@email.com","salt":"2ee67e26-d4d3-426a-9bcf-2b8b816ff2ce","hash":"DA6BCFE83821E48B41440D56EC9C34A9277520F69C26980E1ECD5ADE7B89813B","role":"UNASSIGNED"}');
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:49.171624', 'Carol', 'AddUser', '{"name":"Carol","email":"carol@email.com","salt":"8c35dfe2-e4b9-4d66-8488-bb09a1f7451f","hash":"8398C9E8F1030A6BEE2F524A91A3FDF3C8AF4468BBC5CA90712D2AEE70B65240","role":"UNASSIGNED"}');
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:49.476193', 'Dave', 'AddUser', '{"name":"Dave","email":"dave@email.com","salt":"11baa435-610b-4776-84c7-7201dbdf3dc7","hash":"2935594E22361D0DC1AB0118A6D589ECDF5119390FD408FBCAD60AFB22E2996E","role":"UNASSIGNED"}');
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:49.566729', 'Alice', 'SetRole', '{"name":"Bob","role":"USER"}');
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:49.762353', 'Alice', 'SetRole', '{"name":"Dave","role":"USER"}');
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:49.841237', 'Alice', 'RemoveUser', '{"name":"Carol"}');
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:49.977763', 'Alice', 'AddElection', '{"owner":"Alice","name":"Delete Me"}');
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:50.134133', 'Alice', 'AddElection', '{"owner":"Alice","name":"Favorite Ice Cream Flavor"}');
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:50.293079', 'Alice', 'UpdateElection', '{"name":"Favorite Ice Cream Flavor","updates":{"newName":"Favorite Ice Cream","secretBallot":true,"clearNoVotingBefore":false,"noVotingBefore":"2021-02-03T04:55:30Z","clearNoVotingAfter":false,"noVotingAfter":"2022-02-03T04:55:30Z","restrictWhoCanVote":true,"ownerCanDeleteBallots":true,"auditorCanDeleteBallots":true,"isTemplate":true,"allowChangesAfterVote":false,"isOpen":true}}');
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:50.312971', 'Alice', 'AddCandidates', '{"electionName":"Favorite Ice Cream","candidateNames":["Chocolate","Vanilla","Strawberry"]}');
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:50.383046', 'Alice', 'CastBallot', '{"voterName":"Alice","electionName":"Favorite Ice Cream","rankings":[{"name":"Vanilla","rank":1},{"name":"Chocolate","rank":2}]}');
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:50.493329', 'Alice', 'RemoveCandidates', '{"electionName":"Favorite Ice Cream","candidateNames":["Strawberry"]}');
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:50.519411', 'Alice', 'AddCandidates', '{"electionName":"Favorite Ice Cream","candidateNames":["Butter Pecan","Neapolitan","Mint","Chocolate Chip"]}');
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:50.52543', 'Alice', 'RescindBallot', '{"voterName":"Alice","electionName":"Favorite Ice Cream"}');
insert into event (`when`, authority, type, text)
values ('2021-12-30 03:47:50.768159', 'Alice', 'CastBallot', '{"voterName":"Alice","electionName":"Favorite Ice Cream","rankings":[{"name":"Chocolate Chip","rank":1},{"name":"Neapolitan","rank":2},{"name":"Chocolate","rank":3},{"name":"Vanilla","rank":4},{"name":"Butter Pecan","rank":5},{"name":"Mint","rank":6}]}');
insert into event (`when`, authority, type, text)
values ('2021-12-30 18:52:08.75218', 'Alice', 'DeleteElection', '{"name":"Delete Me"}');
select count(id)
from event;
select * from event;
insert into event (`when`, authority, type, text)
values ('2021-12-30 18:52:08.756192', 'Bob', 'CastBallot', '{"voterName":"Bob","electionName":"Favorite Ice Cream","rankings":[{"name":"Chocolate","rank":1},{"name":"Chocolate Chip","rank":2},{"name":"Vanilla","rank":3},{"name":"Mint","rank":4},{"name":"Butter Pecan","rank":5},{"name":"Neapolitan","rank":6}]}');
insert into event (`when`, authority, type, text)
values ('2021-12-30 19:21:10.807202', 'Dave', 'CastBallot', '{"voterName":"Dave","electionName":"Favorite Ice Cream","rankings":[{"name":"Mint","rank":1},{"name":"Chocolate Chip","rank":2},{"name":"Neapolitan","rank":3},{"name":"Chocolate","rank":4},{"name":"Vanilla","rank":5},{"name":"Butter Pecan","rank":6}]}');
