ALTER TABLE tb_user ALTER COLUMN id RESTART WITH 1;

insert into tb_user (name, email, password) values ('John Doe', 'john@email.com', '123456');
insert into tb_user (name, email, password) values ('Kaue', 'kaue@email.com', '123456');