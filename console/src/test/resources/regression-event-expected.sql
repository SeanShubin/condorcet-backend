insert into event (`when`, authority, type, text)
values ('2021-02-15 07:56:19.715621', 'Alice', 'AddUser', '{"name":"Alice","email":"alice@email.com","salt":"8d027cff-d538-4078-8de9-b15c0c46d383","hash":"972A59B9F4F03166E3023CFE496F179F1AEACBAC0D8BEA2A5E28676D384A2BC4","role":"OWNER"}');
insert into event (`when`, authority, type, text)
values ('2021-02-15 07:56:19.863825', 'Bob', 'AddUser', '{"name":"Bob","email":"bob@email.com","salt":"9cfdf833-a1cb-487a-bf8e-a2efd8b209a4","hash":"7961966BF84F86F943E0B56DB8CF66075CB7B160811FF2995107F7715D4F9D6C","role":"UNASSIGNED"}');
insert into event (`when`, authority, type, text)
values ('2021-02-15 07:56:19.907919', 'Carol', 'AddUser', '{"name":"Carol","email":"carol@email.com","salt":"9c24ff2a-9f85-45cc-8f83-50974f93bc80","hash":"1F7CAB856AEE2DF4AE89369F2CF3C6B51856655B16E945BBB1E39D81CABB37E9","role":"UNASSIGNED"}');
insert into event (`when`, authority, type, text)
values ('2021-02-15 08:47:44.946811', 'Alice', 'SetRole', '{"name":"Bob","role":"USER"}');
insert into event (`when`, authority, type, text)
values ('2021-02-15 08:49:06.420277', 'Alice', 'RemoveUser', '{"name":"Carol"}');
insert into event (`when`, authority, type, text)
values ('2021-02-20 06:26:30.148351', 'Alice', 'AddElection', '{"owner":"Alice","name":"Delete Me"}');
insert into event (`when`, authority, type, text)
values ('2021-02-24 00:11:35.648172', 'Alice', 'AddElection', '{"owner":"Alice","name":"Favorite Ice Cream Flavor"}');
insert into event (`when`, authority, type, text)
values ('2021-02-24 19:26:02.441961', 'Alice', 'UpdateElection', '{"name":"Favorite Ice Cream Flavor","updates":{"newName":"Favorite Ice Cream","secretBallot":true,"clearScheduledStart":false,"scheduledStart":"2021-02-03T04:55:30Z","clearScheduledEnd":false,"scheduledEnd":"2022-02-03T04:55:30Z","restrictWhoCanVote":true,"ownerCanDeleteBallots":true,"auditorCanDeleteBallots":true,"isTemplate":true,"noChangesAfterVote":false,"isOpen":true}}');
insert into event (`when`, authority, type, text)
values ('2021-02-24 19:26:02.579771', 'Alice', 'SetCandidates', '{"electionName":"Favorite Ice Cream","candidateNames":["Chocolate","Vanilla","Strawberry"]}');
insert into event (`when`, authority, type, text)
values ('2021-02-24 19:26:03.44616', 'Alice', 'DeleteElection', '{"name":"Delete Me"}');
select count(id)
from event;
select * from event;
