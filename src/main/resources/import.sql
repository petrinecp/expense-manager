insert into Users (name, forname, username, passwdHash, authRole) values ('John', 'Doe', 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918','ADMIN')
insert into Users (name, forname, username, passwdHash, authRole, authToken) values ('test', 'test', 'test', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08','ADMIN', 'test')
insert into Users (name, forname, username, passwdHash, authRole, authToken) values ('user', 'user', 'user', '04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb','PRIVILEGED_USER', 'user')
insert into Users (name, forname, username, passwdHash, authRole, authToken) values ('user', 'user', 'user2', '04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb','BASIC_USER', 'user')
insert into Categories (title) values ('Car')
insert into Categories (title) values ('Travel')
insert into Categories (title) values ('Food & Drink')
insert into Categories (title) values ('Family & Personal')
insert into Categories (title) values ('Bills')
insert into Categories (title) values ('Entertainment')
insert into Categories (title) values ('Shopping')
insert into Banks (title, identifier) values ('Československá obchodní banka', 'CSOB')
insert into Rules (userId, categoryId, ruleString) values(3, 3, 'albert');
insert into Rules (userId, categoryId, ruleString) values(3, 6, 'sportisimo');
insert into Rules (userId, categoryId, ruleString) values(3, 5, 'vyplata');
