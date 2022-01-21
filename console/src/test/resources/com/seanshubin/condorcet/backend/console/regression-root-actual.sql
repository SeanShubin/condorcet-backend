drop database if exists condorcet_regression_test_event_can_be_purged;
drop database if exists condorcet_regression_test_state_can_be_purged;
select count(*)
from information_schema.schemata
where schema_name = 'condorcet_regression_test_event_can_be_purged';
create database condorcet_regression_test_event_can_be_purged;
use condorcet_regression_test_event_can_be_purged;
create table event (
    id int not null auto_increment,
    `when` datetime(6) not null,
    authority varchar(255) not null,
    type varchar(255) not null,
    text text not null,
    primary key(id)
);
select count(*)
from information_schema.schemata
where schema_name = 'condorcet_regression_test_state_can_be_purged';
create database condorcet_regression_test_state_can_be_purged;
use condorcet_regression_test_state_can_be_purged;
create table int_variable (
    id int not null auto_increment,
    name varchar(255) not null unique,
    value int not null,
    primary key(id)
);
create table role_permission (
    id int not null auto_increment,
    role ENUM('NO_ACCESS','OBSERVER','USER','ADMIN','AUDITOR','OWNER') not null,
    permission ENUM('TRANSFER_OWNER','VIEW_SECRETS','MANAGE_USERS','USE_APPLICATION','VIEW_APPLICATION') not null,
    primary key(id)
);
create table user (
    id int not null auto_increment,
    name varchar(255) not null unique,
    email varchar(255) not null unique,
    role ENUM('NO_ACCESS','OBSERVER','USER','ADMIN','AUDITOR','OWNER') not null,
    salt varchar(255) not null,
    hash varchar(255) not null,
    primary key(id)
);
create table election (
    id int not null auto_increment,
    owner_id int not null,
    foreign key fk_owner(owner_id) references user(id),
    name varchar(255) not null unique,
    secret_ballot boolean not null default false,
    no_voting_before datetime(6),
    no_voting_after datetime(6),
    allow_vote boolean not null default false,
    allow_edit boolean not null default true,
    primary key(id)
);
create table candidate (
    id int not null auto_increment,
    election_id int not null,
    foreign key fk_election(election_id) references election(id),
    name varchar(255) not null,
    primary key(id)
);
alter table candidate add unique unique_candidate(election_id, name);
create table voter (
    id int not null auto_increment,
    election_id int not null,
    foreign key fk_election(election_id) references election(id),
    user_id int not null,
    foreign key fk_user(user_id) references user(id),
    primary key(id)
);
alter table voter add unique unique_voter(election_id, user_id);
create table ballot (
    id int not null auto_increment,
    user_id int not null,
    foreign key fk_user(user_id) references user(id),
    election_id int not null,
    foreign key fk_election(election_id) references election(id),
    confirmation varchar(255) not null,
    when_cast datetime(6) not null,
    primary key(id)
);
alter table ballot add unique unique_ballot(user_id, election_id);
create table ranking (
    id int not null auto_increment,
    ballot_id int not null,
    foreign key fk_ballot(ballot_id) references ballot(id),
    candidate_id int not null,
    foreign key fk_candidate(candidate_id) references candidate(id),
    `rank` int,
    primary key(id)
);
alter table ranking add unique unique_ranking(ballot_id, candidate_id);
create table tally (
    id int not null auto_increment,
    election_id int not null,
    foreign key fk_election(election_id) references election(id),
    report text not null,
    primary key(id)
);
alter table tally add unique unique_tally(election_id);
insert into role_permission (role, permission)
values ('OWNER', 'TRANSFER_OWNER');
insert into role_permission (role, permission)
values ('OWNER', 'VIEW_SECRETS');
insert into role_permission (role, permission)
values ('OWNER', 'MANAGE_USERS');
insert into role_permission (role, permission)
values ('OWNER', 'USE_APPLICATION');
insert into role_permission (role, permission)
values ('OWNER', 'VIEW_APPLICATION');
insert into role_permission (role, permission)
values ('AUDITOR', 'VIEW_SECRETS');
insert into role_permission (role, permission)
values ('AUDITOR', 'MANAGE_USERS');
insert into role_permission (role, permission)
values ('AUDITOR', 'USE_APPLICATION');
insert into role_permission (role, permission)
values ('AUDITOR', 'VIEW_APPLICATION');
insert into role_permission (role, permission)
values ('ADMIN', 'MANAGE_USERS');
insert into role_permission (role, permission)
values ('ADMIN', 'USE_APPLICATION');
insert into role_permission (role, permission)
values ('ADMIN', 'VIEW_APPLICATION');
insert into role_permission (role, permission)
values ('USER', 'USE_APPLICATION');
insert into role_permission (role, permission)
values ('USER', 'VIEW_APPLICATION');
insert into role_permission (role, permission)
values ('OBSERVER', 'VIEW_APPLICATION');
use condorcet_regression_test_event_can_be_purged;
select * from event;
use condorcet_regression_test_state_can_be_purged;
select * from int_variable;
select * from role_permission;
select * from user;
select * from election;
select * from candidate;
select * from voter;
select * from ballot;
select * from ranking;
select * from tally;
use condorcet_regression_test_event_can_be_purged;
select event.id,
       event.`when`,
       event.authority,
       event.type,
       event.text
from event
order by event.id;
use condorcet_regression_test_state_can_be_purged;
select *
from int_variable;
select role, permission
from role_permission;
select *
from user
order by user.id;
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
select candidate.id,
       election.name election,
       candidate.name,
       candidate.election_id
from candidate
         inner join election on candidate.election_id = election.id
order by candidate.id;
select voter.id,
       user.name     voter,
       election.name election,
       voter.user_id,
       voter.election_id
from voter
         inner join election on voter.election_id = election.id
         inner join user on voter.user_id = user.id
order by voter.id;
select ballot.id,
       user.name     user,
       election.name election,
       ballot.confirmation,
       ballot.when_cast,
       ballot.user_id,
       ballot.election_id
from ballot
         inner join user on ballot.user_id = user.id
         inner join election on ballot.election_id = election.id
order by ballot.id;
select ranking.id,
       election.name  election,
       user.name      voter,
       candidate.name candidate,
       ranking.rank,
       ballot.confirmation,
       ballot.when_cast,
       ballot.election_id,
       ranking.ballot_id,
       ranking.candidate_id,
       ballot.user_id
from ranking
         inner join ballot on ranking.ballot_id = ballot.id
         inner join election on ballot.election_id = election.id
         inner join candidate on ranking.candidate_id = candidate.id
         inner join user on ballot.user_id = user.id
order by election.name,
         user.name,
         ranking.rank;
select tally.id,
       election.name election,
       tally.report,
       tally.election_id
from tally
         inner join election on tally.election_id = election.id
order by tally.id;
