insert into Member (id, name, email, phone_number) values (1, 'John Smith', 'john.smith@mailinator.com', '2125551212') 
--insert into Users (id, name, forname) values (1, 'John', 'Doe')
insert into Categories (id, title) values (1, 'Car')
insert into Categories (id, title) values (2, 'Travel')
insert into Categories (id, title) values (3, 'Food & Drink')
insert into Categories (id, title) values (4, 'Family & Personal')
insert into Categories (id, title) values (5, 'Bills')
insert into Categories (id, title) values (6, 'Entertainment')
insert into Categories (id, title) values (7, 'Shopping')
insert into Banks (id, title, identifier) values (1, 'Československá obchodní banka', 'CSOB')

insert into Rules (id, userId, categoryId, ruleString) values(1, 1, 1, 'omv');
insert into Rules (id, userId, categoryId, ruleString) values(2, 1, 6, 'cinema');