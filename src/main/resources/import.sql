insert into Users (name, forname, username, passwd, authRole) values ('John', 'Doe', 'admin', 'admin','ADMIN')
insert into Users (name, forname, username, passwd, authRole, authToken) values ('test', 'test', 'test', 'test','ADMIN', 'test')


insert into Categories (title) values ('Car')
insert into Categories (title) values ('Travel')
insert into Categories (title) values ('Food & Drink')
insert into Categories (title) values ('Family & Personal')
insert into Categories (title) values ('Bills')
insert into Categories (title) values ('Entertainment')
insert into Categories (title) values ('Shopping')
insert into Banks (title, identifier) values ('Československá obchodní banka', 'CSOB')

--insert into Rules (id, userId, categoryId, ruleString) values(1, 1, 1, 'omv');
--insert into Rules (id, userId, categoryId, ruleString) values(2, 1, 6, 'cinema');