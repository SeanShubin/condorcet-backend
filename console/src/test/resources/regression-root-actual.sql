drop database if exists condorcet_regression_test_event_can_be_purged;
drop database if exists condorcet_regression_test_state_can_be_purged;
select count(*)
from information_schema.schemata
where schema_name = 'condorcet_regression_test_event_can_be_purged';
create database condorcet_regression_test_event_can_be_purged;
use condorcet_regression_test_event_can_be_purged;
create table event
(
    id        int          not null auto_increment,
    `when`    datetime(6)  not null,
    authority varchar(255) not null,
    type      varchar(255) not null,
    text      text         not null,
    primary key (id)
);
select count(*)
from information_schema.schemata
where schema_name = 'condorcet_regression_test_state_can_be_purged';
create database condorcet_regression_test_state_can_be_purged;
use condorcet_regression_test_state_can_be_purged;
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
    role  ENUM ('OWNER','AUDITOR','ADMIN','USER','OBSERVER','UNASSIGNED') not null,
    salt  varchar(255)                                                    not null,
    hash  varchar(255)                                                    not null,
    primary key (id)
);
create table election
(
    id                         int          not null auto_increment,
    owner_id                   int          not null,
    foreign key fk_owner (owner_id) references user (id),
    name                       varchar(255) not null unique,
    secret_ballot              boolean      not null default false,
    scheduled_start            datetime(6),
    scheduled_end              datetime(6),
    restrict_who_can_vote      boolean      not null default false,
    owner_can_delete_ballots   boolean      not null default false,
    auditor_can_delete_ballots boolean      not null default false,
    is_template                boolean      not null default false,
    no_changes_after_vote      boolean      not null default true,
    is_open                    boolean      not null default false,
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
use condorcet_regression_test_event_can_be_purged;
select *
from event;
use condorcet_regression_test_state_can_be_purged;
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
select * from ranking;
select * from tally;
