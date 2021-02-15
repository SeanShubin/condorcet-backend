drop database if exists condorcet_regression_test_state;
select count(*)
from information_schema.schemata
where schema_name = 'condorcet_regression_test_state';
create database condorcet_regression_test_state;
use condorcet_regression_test_state;
create table int_variable
(
    id    int          not null auto_increment,
    name  varchar(255) not null unique,
    value int          not null,
    primary key (id)
);
create table role_permission
(
    id         int                                                                                        not null auto_increment,
    role       ENUM ('OWNER','AUDITOR','ADMIN','USER','OBSERVER','UNASSIGNED')                            not null,
    permission ENUM ('TRANSFER_OWNER','VIEW_SECRETS','MANAGE_USERS','USE_APPLICATION','VIEW_APPLICATION') not null,
    primary key (id)
);
create table user
(
    id    int                                                             not null auto_increment,
    name  varchar(255)                                                    not null unique,
    email varchar(255)                                                    not null unique,
    salt  varchar(255)                                                    not null,
    hash  varchar(255)                                                    not null,
    role  ENUM ('OWNER','AUDITOR','ADMIN','USER','OBSERVER','UNASSIGNED') not null,
    primary key (id)
);
create table election
(
    id       int                                not null auto_increment,
    owner_id int                                not null,
    foreign key fk_owner (owner_id) references user (id),
    name     varchar(255)                       not null unique,
    end      datetime(6),
    secret   boolean                            not null,
    status   ENUM ('EDITING','LIVE','COMPLETE') not null,
    primary key (id)
);
create table candidate
(
    id          int          not null auto_increment,
    election_id int          not null,
    foreign key fk_election (election_id) references election (id),
    name        varchar(255) not null,
    primary key (id)
);
alter table candidate
    add unique unique_candidate (election_id, name);
create table voter
(
    id          int not null auto_increment,
    election_id int not null,
    foreign key fk_election (election_id) references election (id),
    user_id     int not null,
    foreign key fk_user (user_id) references user (id),
    primary key (id)
);
alter table voter
    add unique unique_voter (election_id, user_id);
create table ballot
(
    id           int          not null auto_increment,
    user_id      int          not null,
    foreign key fk_user (user_id) references user (id),
    election_id  int          not null,
    foreign key fk_election (election_id) references election (id),
    confirmation varchar(255) not null,
    when_cast    datetime(6)  not null,
    primary key (id)
);
alter table ballot
    add unique unique_ballot (user_id, election_id);
create table ranking
(
    id           int not null auto_increment,
    ballot_id    int not null,
    foreign key fk_ballot (ballot_id) references ballot (id),
    candidate_id int not null,
    foreign key fk_candidate (candidate_id) references candidate (id),
    `rank`       int,
    primary key (id)
);
alter table ranking
    add unique unique_ranking (ballot_id, candidate_id);
create table tally
(
    id          int  not null auto_increment,
    election_id int  not null,
    foreign key fk_election (election_id) references election (id),
    report      text not null,
    primary key (id)
);
alter table tally
    add unique unique_tally (election_id);
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
select value
from int_variable
where name = 'last-synced';
insert into int_variable (name, value)
values ('last-synced', 0);
select value
from int_variable
where name = 'last-synced';
select name, email, salt, hash, role
from user
where name = 'Alice';
select name, email, salt, hash, role
from user
where email = 'alice@email.com';
select count(id)
from user;
select value
from int_variable
where name = 'last-synced';
insert into user (name, email, salt, hash, role)
values ('Alice', 'alice@email.com', '8d027cff-d538-4078-8de9-b15c0c46d383',
        '972A59B9F4F03166E3023CFE496F179F1AEACBAC0D8BEA2A5E28676D384A2BC4', 'OWNER');
update int_variable
set value = 1
where name = 'last-synced';
select name, email, salt, hash, role
from user
where name = 'Alice';
select name, email, salt, hash, role
from user
where name = 'Bob';
select name, email, salt, hash, role
from user
where email = 'bob@email.com';
select count(id)
from user;
select value
from int_variable
where name = 'last-synced';
insert into user (name, email, salt, hash, role)
values ('Bob', 'bob@email.com', '9cfdf833-a1cb-487a-bf8e-a2efd8b209a4',
        '7961966BF84F86F943E0B56DB8CF66075CB7B160811FF2995107F7715D4F9D6C', 'UNASSIGNED');
update int_variable
set value = 2
where name = 'last-synced';
select name, email, salt, hash, role
from user
where name = 'Bob';
select name, email, salt, hash, role
from user
where name = 'Carol';
select name, email, salt, hash, role
from user
where email = 'carol@email.com';
select count(id)
from user;
select value
from int_variable
where name = 'last-synced';
insert into user (name, email, salt, hash, role)
values ('Carol', 'carol@email.com', '9c24ff2a-9f85-45cc-8f83-50974f93bc80',
        '1F7CAB856AEE2DF4AE89369F2CF3C6B51856655B16E945BBB1E39D81CABB37E9', 'UNASSIGNED');
update int_variable
set value = 3
where name = 'last-synced';
select name, email, salt, hash, role
from user
where name = 'Carol';
select name, email, salt, hash, role
from user
where name = 'Alice';
select name, email, salt, hash, role
from user
where name = 'Alice';
select name, email, salt, hash, role
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
select name, email, salt, hash, role
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
select *
from int_variable;
select *
from role_permission;
select *
from user;
select *
from election;
select *
from candidate;
select *
from voter;
select *
from ballot;
select *
from ranking;
select *
from tally;