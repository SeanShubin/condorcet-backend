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
insert into int_variable (name, value)
values ('last-synced', 0);
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
select name, email, salt, hash, role from user where name = 'Bob';
select name, email, salt, hash, role from user where name = 'Carol';
select name, email, salt, hash, role from user where email = 'carol@email.com';
select count(id) from user;
select value from int_variable where name = 'last-synced';
insert into user (name, email, salt, hash, role) values ('Carol', 'carol@email.com', '9c24ff2a-9f85-45cc-8f83-50974f93bc80', '1F7CAB856AEE2DF4AE89369F2CF3C6B51856655B16E945BBB1E39D81CABB37E9', 'UNASSIGNED');
update int_variable set value = 3 where name = 'last-synced';
select name, email, salt, hash, role from user where name = 'Carol';
select name, email, salt, hash, role from user where name = 'Alice';
select name, email, salt, hash, role from user where name = 'Alice';
select name, email, salt, hash, role from user where name = 'Bob';
select value from int_variable where name = 'last-synced';
update user set role='USER' where name = 'Bob';
update int_variable set value = 4 where name = 'last-synced';
select name, email, salt, hash, role from user where name = 'Carol';
select value from int_variable where name = 'last-synced';
delete from user where name = 'Carol';
update int_variable set value = 5 where name = 'last-synced';
select * from user;
