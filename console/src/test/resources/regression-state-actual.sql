select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Alice';
select name,
       email,
       salt,
       hash,
       role
from user
where email = 'alice@email.com';
select count(id)
from user;
select value
from int_variable
where name = 'last-synced';
insert into int_variable (name, value)
values ('last-synced', 0);
select value
from int_variable
where name = 'last-synced';
insert into user (name,
                  email,
                  salt,
                  hash,
                  role)
values ('Alice', 'alice@email.com', '8d027cff-d538-4078-8de9-b15c0c46d383', '972A59B9F4F03166E3023CFE496F179F1AEACBAC0D8BEA2A5E28676D384A2BC4', 'OWNER');
update int_variable
set value = 1
where name = 'last-synced';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Alice';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Bob';
select name,
       email,
       salt,
       hash,
       role
from user
where email = 'bob@email.com';
select count(id)
from user;
select value
from int_variable
where name = 'last-synced';
insert into user (name,
                  email,
                  salt,
                  hash,
                  role)
values ('Bob', 'bob@email.com', '9cfdf833-a1cb-487a-bf8e-a2efd8b209a4', '7961966BF84F86F943E0B56DB8CF66075CB7B160811FF2995107F7715D4F9D6C', 'UNASSIGNED');
update int_variable
set value = 2
where name = 'last-synced';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Bob';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Carol';
select name,
       email,
       salt,
       hash,
       role
from user
where email = 'carol@email.com';
select count(id)
from user;
select value
from int_variable
where name = 'last-synced';
insert into user (name,
                  email,
                  salt,
                  hash,
                  role)
values ('Carol', 'carol@email.com', '9c24ff2a-9f85-45cc-8f83-50974f93bc80', '1F7CAB856AEE2DF4AE89369F2CF3C6B51856655B16E945BBB1E39D81CABB37E9', 'UNASSIGNED');
update int_variable
set value = 3
where name = 'last-synced';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Carol';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Alice';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Alice';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Bob';
select value
from int_variable
where name = 'last-synced';
update user
set role='USER'
where name = 'Bob';
update int_variable
set value = 4
where name = 'last-synced';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Carol';
select value
from int_variable
where name = 'last-synced';
delete
from user
where name = 'Carol';
update int_variable
set value = 5
where name = 'last-synced';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.scheduled_start,
       election.scheduled_end,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.no_changes_after_vote,
       election.is_open
from election
         inner join user
                    on election.owner_id = user.id
where election.name = 'Delete Me';
select value
from int_variable
where name = 'last-synced';
insert into election (owner_id, name)
values ((select id from user where name = 'Alice'), 'Delete Me');
update int_variable
set value = 6
where name = 'last-synced';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.scheduled_start,
       election.scheduled_end,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.no_changes_after_vote,
       election.is_open
from election
         inner join user
                    on election.owner_id = user.id
where election.name = 'Favorite Ice Cream Flavor';
select value
from int_variable
where name = 'last-synced';
insert into election (owner_id, name)
values ((select id from user where name = 'Alice'), 'Favorite Ice Cream Flavor');
update int_variable
set value = 7
where name = 'last-synced';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.scheduled_start,
       election.scheduled_end,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.no_changes_after_vote,
       election.is_open
from election
         inner join user
                    on election.owner_id = user.id
where election.name = 'Favorite Ice Cream Flavor';
select value
from int_variable
where name = 'last-synced';
update
    election
set name = 'Favorite Ice Cream'
where name = 'Favorite Ice Cream Flavor';
update
    election
set secret_ballot = 1
where name = 'Favorite Ice Cream Flavor';
update
    election
set scheduled_start = '2021-02-03 04:55:30'
where name = 'Favorite Ice Cream Flavor';
update
    election
set scheduled_end = '2022-02-03 04:55:30'
where name = 'Favorite Ice Cream Flavor';
update
    election
set restrict_who_can_vote = 1
where name = 'Favorite Ice Cream Flavor';
update
    election
set owner_can_delete_ballots = 1
where name = 'Favorite Ice Cream Flavor';
update
    election
set auditor_can_delete_ballots = 1
where name = 'Favorite Ice Cream Flavor';
update
    election
set is_template = 1
where name = 'Favorite Ice Cream Flavor';
update
    election
set no_changes_after_vote = 0
where name = 'Favorite Ice Cream Flavor';
update
    election
set is_open = 1
where name = 'Favorite Ice Cream Flavor';
update int_variable
set value = 8
where name = 'last-synced';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.scheduled_start,
       election.scheduled_end,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.no_changes_after_vote,
       election.is_open
from election
         inner join user
                    on election.owner_id = user.id
where election.name = 'Favorite Ice Cream';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.scheduled_start,
       election.scheduled_end,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.no_changes_after_vote,
       election.is_open
from election
         inner join user
                    on election.owner_id = user.id
where election.name = 'Delete Me';
select value
from int_variable
where name = 'last-synced';
delete
from election
where name = 'Delete Me';
update int_variable
set value = 9
where name = 'last-synced';
select count(id)
from user;
select count(id)
from election;
select * from user;
select election.id,
       election.owner_id,
       user.name owner,
       election.name,
       election.secret_ballot,
       election.scheduled_start,
       election.scheduled_end,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.no_changes_after_vote,
       election.is_open
from election
         inner join user on election.owner_id = user.id;
