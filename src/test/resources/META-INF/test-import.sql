insert into Users (name, forname, username, passwdHash, authRole) values ('Peter', 'Nash', 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918','ADMIN')
insert into Users (name, forname, username, passwdHash, authRole, authToken) values ('test', 'test', 'test', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08','ADMIN', 'test')
insert into Banks (title, identifier) values ('ČSOB', 'CSOB')
insert into Categories (title) values ('Car')
insert into Categories (title) values ('Travel')
--insert into Rules (id, userId, categoryId, ruleString) values(1, 1, 1, 'omv');