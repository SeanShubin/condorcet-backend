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
values ('Alice', 'alice@email.com', 'a09a049b-4228-4c21-b86d-35bc7a1ed3bb', '08DDCCEBFB8973647C97B56F04F5F24BDDBB3F164B4DF22B1DB6EC3DFB9B5398', 'OWNER');
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
values ('Bob', 'bob@email.com', '2ee67e26-d4d3-426a-9bcf-2b8b816ff2ce', 'DA6BCFE83821E48B41440D56EC9C34A9277520F69C26980E1ECD5ADE7B89813B', 'UNASSIGNED');
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
values ('Carol', 'carol@email.com', '8c35dfe2-e4b9-4d66-8488-bb09a1f7451f', '8398C9E8F1030A6BEE2F524A91A3FDF3C8AF4468BBC5CA90712D2AEE70B65240', 'UNASSIGNED');
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
values ('Dave', 'dave@email.com', '11baa435-610b-4776-84c7-7201dbdf3dc7', '2935594E22361D0DC1AB0118A6D589ECDF5119390FD408FBCAD60AFB22E2996E', 'UNASSIGNED');
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
set value = 5
where name = 'last-synced';
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
set value = 6
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
set value = 7
where name = 'last-synced';
select user.name           as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.allow_changes_after_vote,
       election.is_open,
       count(candidate.id) as candidate_count
from election
         inner join user on election.owner_id = user.id
         left join candidate on election.id = candidate.election_id
where election.name = 'Delete Me'
group by election.id;
select value
from int_variable
where name = 'last-synced';
insert into election (owner_id, name)
values ((select id from user where name = 'Alice'), 'Delete Me');
update int_variable
set value = 8
where name = 'last-synced';
select user.name           as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.allow_changes_after_vote,
       election.is_open,
       count(candidate.id) as candidate_count
from election
         inner join user on election.owner_id = user.id
         left join candidate on election.id = candidate.election_id
where election.name = 'Favorite Ice Cream Flavor'
group by election.id;
select value
from int_variable
where name = 'last-synced';
insert into election (owner_id, name)
values ((select id from user where name = 'Alice'), 'Favorite Ice Cream Flavor');
update int_variable
set value = 9
where name = 'last-synced';
select user.name           as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.allow_changes_after_vote,
       election.is_open,
       count(candidate.id) as candidate_count
from election
         inner join user on election.owner_id = user.id
         left join candidate on election.id = candidate.election_id
where election.name = 'Favorite Ice Cream Flavor'
group by election.id;
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
set no_voting_before = '2021-02-03 04:55:30'
where name = 'Favorite Ice Cream Flavor';
update
    election
set no_voting_after = '2022-02-03 04:55:30'
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
set allow_changes_after_vote = 0
where name = 'Favorite Ice Cream Flavor';
update
    election
set is_open = 1
where name = 'Favorite Ice Cream Flavor';
update int_variable
set value = 10
where name = 'last-synced';
select user.name           as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.allow_changes_after_vote,
       election.is_open,
       count(candidate.id) as candidate_count
from election
         inner join user on election.owner_id = user.id
         left join candidate on election.id = candidate.election_id
where election.name = 'Favorite Ice Cream'
group by election.id;
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
set value = 11
where name = 'last-synced';
select user.name           as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.allow_changes_after_vote,
       election.is_open,
       count(candidate.id) as candidate_count
from election
         inner join user on election.owner_id = user.id
         left join candidate on election.id = candidate.election_id
where election.name = 'Favorite Ice Cream'
group by election.id;
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Alice';
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
        '3b12bd5b-0e7d-4519-86ff-0629ecdd5afb',
        '2021-12-30 03:47:50.396089');
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Alice'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Vanilla'),
        1);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Alice'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate'),
        2);
update int_variable
set value = 12
where name = 'last-synced';
select user.name           as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.allow_changes_after_vote,
       election.is_open,
       count(candidate.id) as candidate_count
from election
         inner join user on election.owner_id = user.id
         left join candidate on election.id = candidate.election_id
where election.name = 'Favorite Ice Cream'
group by election.id;
select value
from int_variable
where name = 'last-synced';
delete
from candidate
where election_id = (select id from election where name = 'Favorite Ice Cream')
  and candidate.name = 'Strawberry';
update int_variable
set value = 13
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
set value = 14
where name = 'last-synced';
select user.name           as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.allow_changes_after_vote,
       election.is_open,
       count(candidate.id) as candidate_count
from election
         inner join user on election.owner_id = user.id
         left join candidate on election.id = candidate.election_id
where election.name = 'Favorite Ice Cream'
group by election.id;
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Alice';
select user.name as user_name, election.name as election_name, confirmation, when_cast
from ballot
inner join user on ballot.user_id = user.id
inner join election on ballot.election_id = election.id
where user.name = 'Alice' and election.name = 'Favorite Ice Cream';
select value
from int_variable
where name = 'last-synced';
delete
from ranking
where ranking.ballot_id = (
    select id
    from ballot
    where ballot.user_id = (
        select id
        from user
        where name = 'Alice')
      and ballot.election_id = (
        select id
        from election
        where name = 'Favorite Ice Cream'));
delete
from ballot
where ballot.user_id = (select id from user where name = 'Alice')
  and ballot.election_id = (select id from election where name = 'Favorite Ice Cream');
update int_variable
set value = 15
where name = 'last-synced';
select value
from int_variable
where name = 'last-synced';
insert into ballot (user_id, election_id, confirmation, when_cast)
values ((select id from user where name = 'Alice'),
        (select id from election where name = 'Favorite Ice Cream'),
        'afee81f2-21cc-4fab-b630-770a08721686',
        '2021-12-30 18:52:08.744169');
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Alice'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate Chip'),
        1);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Alice'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Neapolitan'),
        2);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Alice'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate'),
        3);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Alice'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Vanilla'),
        4);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Alice'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Butter Pecan'),
        5);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Alice'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Mint'),
        6);
update int_variable
set value = 16
where name = 'last-synced';
select user.name           as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.allow_changes_after_vote,
       election.is_open,
       count(candidate.id) as candidate_count
from election
         inner join user on election.owner_id = user.id
         left join candidate on election.id = candidate.election_id
where election.name = 'Favorite Ice Cream'
group by election.id;
select user.name           as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.allow_changes_after_vote,
       election.is_open,
       count(candidate.id) as candidate_count
from election
         inner join user on election.owner_id = user.id
         left join candidate on election.id = candidate.election_id
where election.name = 'Delete Me'
group by election.id;
select value
from int_variable
where name = 'last-synced';
delete
from election
where name = 'Delete Me';
update int_variable
set value = 17
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
       election.no_voting_before,
       election.no_voting_after,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.allow_changes_after_vote,
       election.is_open
from election
         inner join user on election.owner_id = user.id;
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Bob';
select user.name           as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.allow_changes_after_vote,
       election.is_open,
       count(candidate.id) as candidate_count
from election
         inner join user on election.owner_id = user.id
         left join candidate on election.id = candidate.election_id
where election.name = 'Favorite Ice Cream'
group by election.id;
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Bob';
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
        'e1c1dfe0-0423-43f2-bfec-430782b04545',
        '2021-12-30 19:21:10.7203');
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Bob'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate'),
        1);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Bob'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate Chip'),
        2);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Bob'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Vanilla'),
        3);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Bob'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Mint'),
        4);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Bob'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Butter Pecan'),
        5);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Bob'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Neapolitan'),
        6);
update int_variable
set value = 18
where name = 'last-synced';
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Dave';
select user.name           as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.restrict_who_can_vote,
       election.owner_can_delete_ballots,
       election.auditor_can_delete_ballots,
       election.is_template,
       election.allow_changes_after_vote,
       election.is_open,
       count(candidate.id) as candidate_count
from election
         inner join user on election.owner_id = user.id
         left join candidate on election.id = candidate.election_id
where election.name = 'Favorite Ice Cream'
group by election.id;
select name,
       email,
       salt,
       hash,
       role
from user
where name = 'Dave';
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
        '8c9fd5ac-7f5b-44bf-ab33-4542452dbceb',
        '2021-12-30 19:21:10.812729');
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Dave'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Mint'),
        1);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Dave'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate Chip'),
        2);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Dave'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Neapolitan'),
        3);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Dave'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Chocolate'),
        4);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Dave'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Vanilla'),
        5);
insert into ranking (ballot_id, candidate_id, `rank`)
values ((
            select ballot.id
            from ballot
                     inner join user on ballot.user_id = user.id
                     inner join election on ballot.election_id = election.id
            where user.name = 'Dave'
              and election.name = 'Favorite Ice Cream'),
        (
            select candidate.id
            from candidate
                     inner join election on candidate.election_id = election.id
            where election.name = 'Favorite Ice Cream'
              and candidate.name = 'Butter Pecan'),
        6);
update int_variable
set value = 19
where name = 'last-synced';
