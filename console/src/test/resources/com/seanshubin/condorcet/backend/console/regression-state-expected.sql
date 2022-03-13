select count(id)
from user;
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
values ('Alice', 'alice@email.com', '86E2763B5F192DCA4CEC3CE22023E273', '86765C5FC67880E5A65E15B5259242AC8AB8CAA3FE9F62AF00447626228ED4FF', 'OWNER');
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
values ('Bob', 'bob@email.com', '95127FEBEA8BD883B729D2D84CF4BC51', 'A820B9ACBBAE29C9D8C95AA85450ACF3BB911A4D80737E2F1BE9D51BCE2ECB1A', 'VOTER');
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
where role = 'VOTER';
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
values ('Carol', 'carol@email.com', '168A7E74F7E93F3AD3FFF89BCAEB8965', 'B6EA0C8348FC80D297B22779874F224DCD6E01D6140BA1547D677F9168C2E4AC', 'VOTER');
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
where role = 'VOTER';
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
values ('Dave', 'dave@email.com', '28E7E88968AEA760BDC7E9C27EB29083', '797E878E90C3936917ED74B3CE92915F7373ECBEA023107F437321F7AD1D5FFA', 'VOTER');
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
where role = 'VOTER';
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
values ('Eve', 'eve@email.com', '5CD781EC81444AAC3B43D60DAD621B15', 'E79D34E6D38A14C470ABFD5B840C5E8217DDC7C66D9F49A66C6EC7A78D2C9D7E', 'VOTER');
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
where role = 'VOTER';
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
select
       count(candidate.id)
from candidate
    inner join election
        on candidate.election_id = election.id
where
      election.name = 'Favorite Ice Cream';
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
  and permission = 'VOTE';
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
        '8FDE24EC97C067F3315B31120D97ADE0',
        '2022-03-13 01:21:45.37869');
delete from ranking
where ballot_id = (select id from ballot where confirmation = '8FDE24EC97C067F3315B31120D97ADE0');
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '8FDE24EC97C067F3315B31120D97ADE0'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Vanilla'),
        1);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '8FDE24EC97C067F3315B31120D97ADE0'),
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
  and permission = 'VOTE';
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
where ballot_id = (select id from ballot where confirmation = '8FDE24EC97C067F3315B31120D97ADE0');
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '8FDE24EC97C067F3315B31120D97ADE0'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate Chip'),
        1);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '8FDE24EC97C067F3315B31120D97ADE0'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Neapolitan'),
        2);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '8FDE24EC97C067F3315B31120D97ADE0'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate'),
        3);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '8FDE24EC97C067F3315B31120D97ADE0'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Vanilla'),
        4);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '8FDE24EC97C067F3315B31120D97ADE0'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Butter Pecan'),
        5);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '8FDE24EC97C067F3315B31120D97ADE0'),
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
set when_cast = '2022-03-13 01:21:45.503531'
where confirmation = '8FDE24EC97C067F3315B31120D97ADE0';
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
  and permission = 'VOTE';
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
        '89D8015719AB344F11FE6E8D9B4F419F',
        '2022-03-13 01:21:46.254619');
delete from ranking
where ballot_id = (select id from ballot where confirmation = '89D8015719AB344F11FE6E8D9B4F419F');
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '89D8015719AB344F11FE6E8D9B4F419F'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate'),
        1);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '89D8015719AB344F11FE6E8D9B4F419F'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate Chip'),
        2);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '89D8015719AB344F11FE6E8D9B4F419F'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Vanilla'),
        3);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '89D8015719AB344F11FE6E8D9B4F419F'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Mint'),
        4);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '89D8015719AB344F11FE6E8D9B4F419F'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Butter Pecan'),
        5);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = '89D8015719AB344F11FE6E8D9B4F419F'),
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
  and permission = 'VOTE';
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
        'ABCD6DC81CA9327D2ACAEEF758B61414',
        '2022-03-13 01:21:46.383634');
delete from ranking
where ballot_id = (select id from ballot where confirmation = 'ABCD6DC81CA9327D2ACAEEF758B61414');
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = 'ABCD6DC81CA9327D2ACAEEF758B61414'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Mint'),
        1);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = 'ABCD6DC81CA9327D2ACAEEF758B61414'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate Chip'),
        2);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = 'ABCD6DC81CA9327D2ACAEEF758B61414'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Neapolitan'),
        3);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = 'ABCD6DC81CA9327D2ACAEEF758B61414'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate'),
        4);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = 'ABCD6DC81CA9327D2ACAEEF758B61414'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Vanilla'),
        5);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((select ballot.id from ballot where ballot.confirmation = 'ABCD6DC81CA9327D2ACAEEF758B61414'),
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
select value
from int_variable
where name = 'last-synced';
update
    user
set salt = '7F0BD8C0EEF466D7D18420565E333532', hash = 'B8F9FABF861F0A40C7F487665D2D5D1B9333A51C3EF5A8B749261884753ED2E0'
where name = 'Alice';
update int_variable
set value = 25
where name = 'last-synced';
