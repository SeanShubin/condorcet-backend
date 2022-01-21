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
values ('Alice', 'alice@email.com', 'acb9ef15-ee53-4921-a8e3-0e345bb17ddd', 'C3AB0D8CD392C7AB6230C154449B898FB14A943C637229DB9B08A6A949C4822C', 'OWNER');
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
select permission
from role_permission
where role = 'OWNER';
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
values ('Bob', 'bob@email.com', '3396f2cb-79bd-4d33-b01c-e9b2656c3ffd', '81C48D4A77584F8099E1BBD23F892CBD0F7F96D122BC32F179E7D2DD8CF7004D', 'OBSERVER');
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
select permission
from role_permission
where role = 'OBSERVER';
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
values ('Carol', 'carol@email.com', '33b16b3e-1955-4fbc-9657-3d6f529786a1', '1451CCFCB4D6806DE07F859983265353C83F555ADD86E1DF8F3B43F2A19FAAF0', 'OBSERVER');
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
select permission
from role_permission
where role = 'OBSERVER';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Dave';
select name,
       email,
       salt,
       hash,
       role
from user
where email = 'dave@email.com';
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
values ('Dave', 'dave@email.com', '8a844e78-61de-4b0d-9ffc-a9dfa2fe25ea', 'B8136925AF6F728CC31023CD0482A62F1EE2C3A876C70A4C020CA494B687AC0B', 'OBSERVER');
update int_variable
set value = 4
where name = 'last-synced';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Dave';
select permission
from role_permission
where role = 'OBSERVER';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Eve';
select name,
       email,
       salt,
       hash,
       role
from user
where email = 'eve@email.com';
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
values ('Eve', 'eve@email.com', '39ff9e68-45e9-439d-9c15-8781473dc04f', 'AF1753C62BD200523B3ECE11AC3FB79B064318841AFE0E6678FE51DCE5037BAD', 'OBSERVER');
update int_variable
set value = 5
where name = 'last-synced';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Eve';
select permission
from role_permission
where role = 'OBSERVER';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Alice';
select permission
from role_permission
where role = 'OWNER';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Alice';
select permission
from role_permission
where role = 'OWNER';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'MANAGE_USERS';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Alice';
select permission
from role_permission
where role = 'OWNER';
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
set value = 6
where name = 'last-synced';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'MANAGE_USERS';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Alice';
select permission
from role_permission
where role = 'OWNER';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Dave';
select value
from int_variable
where name = 'last-synced';
update user
set role='USER'
where name = 'Dave';
update int_variable
set value = 7
where name = 'last-synced';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'MANAGE_USERS';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Alice';
select permission
from role_permission
where role = 'OWNER';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Eve';
select value
from int_variable
where name = 'last-synced';
update user
set role='USER'
where name = 'Eve';
update int_variable
set value = 8
where name = 'last-synced';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'MANAGE_USERS';
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
set value = 9
where name = 'last-synced';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'MANAGE_USERS';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Alice';
select permission
from role_permission
where role = 'OWNER';
select name, email, salt, hash, role
from user
order by name;
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'USE_APPLICATION';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id
where election.name = 'Delete Me';
select value
from int_variable
where name = 'last-synced';
insert into election (owner_id, name)
values ((select id from user where name = 'Alice'), 'Delete Me');
update int_variable
set value = 10
where name = 'last-synced';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'USE_APPLICATION';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id
where election.name = 'Favorite Ice Cream Flavor';
select value
from int_variable
where name = 'last-synced';
insert into election (owner_id, name)
values ((select id from user where name = 'Alice'), 'Favorite Ice Cream Flavor');
update int_variable
set value = 11
where name = 'last-synced';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'USE_APPLICATION';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id
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
where name = 'Favorite Ice Cream';
update
    election
set no_voting_before = null
where name = 'Favorite Ice Cream';
update
    election
set no_voting_after = null
where name = 'Favorite Ice Cream';
update int_variable
set value = 12
where name = 'last-synced';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'USE_APPLICATION';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id
where election.name = 'Favorite Ice Cream';
select candidate.name
from candidate
         inner join election
                    on candidate.election_id = election.id
where election.name = 'Favorite Ice Cream';
select value
from int_variable
where name = 'last-synced';
insert into candidate (election_id, name)
values ((select id from election where name = 'Favorite Ice Cream'),
        'Chocolate');
insert into candidate (election_id, name)
values ((select id from election where name = 'Favorite Ice Cream'),
        'Vanilla');
insert into candidate (election_id, name)
values ((select id from election where name = 'Favorite Ice Cream'),
        'Strawberry');
update int_variable
set value = 13
where name = 'last-synced';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'USE_APPLICATION';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id
where election.name = 'Favorite Ice Cream';
select name, email, salt, hash, role
from user
order by name;
select user.name
from user
     inner join voter on user.id = voter.user_id
     inner join election on election.id = voter.election_id
where election.name = 'Favorite Ice Cream'
order by user.name;
select value
from int_variable
where name = 'last-synced';
insert into voter (election_id, user_id)
values ((select id from election where name = 'Favorite Ice Cream'),
        (select id from user where name = 'Alice'));
insert into voter (election_id, user_id)
values ((select id from election where name = 'Favorite Ice Cream'),
        (select id from user where name = 'Bob'));
insert into voter (election_id, user_id)
values ((select id from election where name = 'Favorite Ice Cream'),
        (select id from user where name = 'Dave'));
update int_variable
set value = 14
where name = 'last-synced';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'VIEW_APPLICATION';
select user.name
from user
         inner join role_permission on user.role = role_permission.role
where permission = 'USE_APPLICATION'
order by user.name;
select user.name
from user
     inner join voter on user.id = voter.user_id
     inner join election on election.id = voter.election_id
where election.name = 'Favorite Ice Cream'
order by user.name;
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'VIEW_APPLICATION';
select user.name
from user
     inner join voter on user.id = voter.user_id
     inner join election on election.id = voter.election_id
where election.name = 'Favorite Ice Cream'
order by user.name;
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'USE_APPLICATION';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id
where election.name = 'Favorite Ice Cream';
select value
from int_variable
where name = 'last-synced';
update
    election
set allow_edit = 1
where name = 'Favorite Ice Cream';
update
    election
set allow_vote = 1
where name = 'Favorite Ice Cream';
update int_variable
set value = 15
where name = 'last-synced';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'USE_APPLICATION';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id
where election.name = 'Favorite Ice Cream';
select user.name as user_name, election.name as election_name, confirmation, when_cast
from ballot
inner join user on ballot.user_id = user.id
inner join election on ballot.election_id = election.id
where user.name = 'Alice' and election.name = 'Favorite Ice Cream';
select value
from int_variable
where name = 'last-synced';
insert into ballot (user_id, election_id, confirmation, when_cast)
values ((select id from user where name = 'Alice'),
        (select id from election where name = 'Favorite Ice Cream'),
        '2266d672-8185-4bab-9c96-e488b98ccd70',
        '2022-01-21 06:18:26.35079');
delete from ranking
where ballot_id = (select id from ballot where confirmation = '2266d672-8185-4bab-9c96-e488b98ccd70');
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '2266d672-8185-4bab-9c96-e488b98ccd70'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Vanilla'),
        1);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '2266d672-8185-4bab-9c96-e488b98ccd70'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate'),
        2);
update int_variable
set value = 16
where name = 'last-synced';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'USE_APPLICATION';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id
where election.name = 'Favorite Ice Cream';
select candidate.name
from candidate
         inner join election
                    on candidate.election_id = election.id
where election.name = 'Favorite Ice Cream';
select value
from int_variable
where name = 'last-synced';
delete
from candidate
where election_id = (select id from election where name = 'Favorite Ice Cream')
  and candidate.name = 'Strawberry';
update int_variable
set value = 17
where name = 'last-synced';
select value
from int_variable
where name = 'last-synced';
insert into candidate (election_id, name)
values ((select id from election where name = 'Favorite Ice Cream'),
        'Butter Pecan');
insert into candidate (election_id, name)
values ((select id from election where name = 'Favorite Ice Cream'),
        'Neapolitan');
insert into candidate (election_id, name)
values ((select id from election where name = 'Favorite Ice Cream'),
        'Mint');
insert into candidate (election_id, name)
values ((select id from election where name = 'Favorite Ice Cream'),
        'Chocolate Chip');
update int_variable
set value = 18
where name = 'last-synced';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'USE_APPLICATION';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id
where election.name = 'Favorite Ice Cream';
select user.name as user_name, election.name as election_name, confirmation, when_cast
from ballot
inner join user on ballot.user_id = user.id
inner join election on ballot.election_id = election.id
where user.name = 'Alice' and election.name = 'Favorite Ice Cream';
select value
from int_variable
where name = 'last-synced';
delete from ranking
where ballot_id = (select id from ballot where confirmation = '2266d672-8185-4bab-9c96-e488b98ccd70');
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '2266d672-8185-4bab-9c96-e488b98ccd70'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate Chip'),
        1);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '2266d672-8185-4bab-9c96-e488b98ccd70'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Neapolitan'),
        2);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '2266d672-8185-4bab-9c96-e488b98ccd70'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate'),
        3);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '2266d672-8185-4bab-9c96-e488b98ccd70'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Vanilla'),
        4);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '2266d672-8185-4bab-9c96-e488b98ccd70'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Butter Pecan'),
        5);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '2266d672-8185-4bab-9c96-e488b98ccd70'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Mint'),
        6);
update int_variable
set value = 19
where name = 'last-synced';
select value
from int_variable
where name = 'last-synced';
update ballot
set when_cast = '2022-01-21 06:18:26.481986'
where confirmation = '2266d672-8185-4bab-9c96-e488b98ccd70';
update int_variable
set value = 20
where name = 'last-synced';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'VIEW_APPLICATION';
select candidate.name
from candidate
         inner join election
                    on candidate.election_id = election.id
where election.name = 'Favorite Ice Cream';
select candidate.name as candidate, ranking.rank as `rank`
from ballot
         inner join election on ballot.election_id = election.id
         inner join ranking on ranking.ballot_id = ballot.id
         inner join candidate on ranking.candidate_id = candidate.id
         inner join user on ballot.user_id = user.id
where user.name = 'Alice'
  and election.name = 'Favorite Ice Cream';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'VIEW_APPLICATION';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id
where election.name = 'Favorite Ice Cream';
select
       count(candidate.id)
from candidate
    inner join election
        on candidate.election_id = election.id
where
      election.name = 'Favorite Ice Cream';
select
       count(voter.id)
from voter
    inner join election
        on voter.election_id = election.id
where
        election.name = 'Favorite Ice Cream';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'USE_APPLICATION';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id
where election.name = 'Delete Me';
select value
from int_variable
where name = 'last-synced';
delete
from election
where name = 'Delete Me';
update int_variable
set value = 21
where name = 'last-synced';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'VIEW_APPLICATION';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id
order by election.name;
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'VIEW_APPLICATION';
select candidate.name
from candidate
         inner join election
                    on candidate.election_id = election.id
where election.name = 'Favorite Ice Cream';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'VIEW_SECRETS';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'VIEW_APPLICATION';
select count(id)
from user;
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'VIEW_APPLICATION';
select count(id)
from election;
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'VIEW_SECRETS';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'VIEW_SECRETS';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'VIEW_SECRETS';
select * from user;
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'VIEW_SECRETS';
select election.id,
       election.owner_id,
       user.name owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id;
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'VIEW_SECRETS';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Bob';
select permission
from role_permission
where role = 'USER';
select role, permission
from role_permission
where role = 'USER'
  and permission = 'USE_APPLICATION';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id
where election.name = 'Favorite Ice Cream';
select user.name as user_name, election.name as election_name, confirmation, when_cast
from ballot
inner join user on ballot.user_id = user.id
inner join election on ballot.election_id = election.id
where user.name = 'Bob' and election.name = 'Favorite Ice Cream';
select value
from int_variable
where name = 'last-synced';
insert into ballot (user_id, election_id, confirmation, when_cast)
values ((select id from user where name = 'Bob'),
        (select id from election where name = 'Favorite Ice Cream'),
        '8f208935-c86b-408d-a7e5-9547ba866f97',
        '2022-01-21 06:18:27.194641');
delete from ranking
where ballot_id = (select id from ballot where confirmation = '8f208935-c86b-408d-a7e5-9547ba866f97');
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '8f208935-c86b-408d-a7e5-9547ba866f97'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate'),
        1);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '8f208935-c86b-408d-a7e5-9547ba866f97'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate Chip'),
        2);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '8f208935-c86b-408d-a7e5-9547ba866f97'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Vanilla'),
        3);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '8f208935-c86b-408d-a7e5-9547ba866f97'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Mint'),
        4);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '8f208935-c86b-408d-a7e5-9547ba866f97'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Butter Pecan'),
        5);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '8f208935-c86b-408d-a7e5-9547ba866f97'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Neapolitan'),
        6);
update int_variable
set value = 22
where name = 'last-synced';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Dave';
select permission
from role_permission
where role = 'USER';
select role, permission
from role_permission
where role = 'USER'
  and permission = 'USE_APPLICATION';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id
where election.name = 'Favorite Ice Cream';
select user.name as user_name, election.name as election_name, confirmation, when_cast
from ballot
inner join user on ballot.user_id = user.id
inner join election on ballot.election_id = election.id
where user.name = 'Dave' and election.name = 'Favorite Ice Cream';
select value
from int_variable
where name = 'last-synced';
insert into ballot (user_id, election_id, confirmation, when_cast)
values ((select id from user where name = 'Dave'),
        (select id from election where name = 'Favorite Ice Cream'),
        '26e73b89-e82a-4194-ae52-31ec033ee04f',
        '2022-01-21 06:18:27.304843');
delete from ranking
where ballot_id = (select id from ballot where confirmation = '26e73b89-e82a-4194-ae52-31ec033ee04f');
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '26e73b89-e82a-4194-ae52-31ec033ee04f'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Mint'),
        1);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '26e73b89-e82a-4194-ae52-31ec033ee04f'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate Chip'),
        2);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '26e73b89-e82a-4194-ae52-31ec033ee04f'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Neapolitan'),
        3);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '26e73b89-e82a-4194-ae52-31ec033ee04f'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate'),
        4);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '26e73b89-e82a-4194-ae52-31ec033ee04f'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Vanilla'),
        5);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '26e73b89-e82a-4194-ae52-31ec033ee04f'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Butter Pecan'),
        6);
update int_variable
set value = 23
where name = 'last-synced';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Alice';
select permission
from role_permission
where role = 'OWNER';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'USE_APPLICATION';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id
where election.name = 'Favorite Ice Cream';
select value
from int_variable
where name = 'last-synced';
update
    election
set allow_edit = 0
where name = 'Favorite Ice Cream';
update
    election
set allow_vote = 0
where name = 'Favorite Ice Cream';
update int_variable
set value = 24
where name = 'last-synced';
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'VIEW_APPLICATION';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id
where election.name = 'Favorite Ice Cream';
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id
where election.name = 'Favorite Ice Cream';
select candidate.name
from candidate
         inner join election
                    on candidate.election_id = election.id
where election.name = 'Favorite Ice Cream';
select
    ballot.id,
    user.name user,
    election.name election,
    ballot.confirmation,
    ballot.when_cast,
    ranking.rank,
    candidate.name candidate
from ballot
         inner join ranking on ballot.id = ranking.ballot_id
         inner join user on ballot.user_id = user.id
         inner join election on ballot.election_id = election.id
         inner join candidate on ranking.candidate_id = candidate.id
where election.name = 'Favorite Ice Cream'
order by ballot.confirmation, ranking.rank, candidate.name;
select role, permission
from role_permission
where role = 'OWNER'
  and permission = 'VIEW_APPLICATION';
select user.name as user_name, election.name as election_name, confirmation, when_cast
from ballot
inner join user on ballot.user_id = user.id
inner join election on ballot.election_id = election.id
where user.name = 'Alice' and election.name = 'Favorite Ice Cream';
