insert into company(id, name) values
('e25d8dc0-d57e-4ac6-a559-b3fce861c5be', 'Evil Corp.'),
('65213830-9b8d-40cb-ac2b-dfedf38f1d73', 'Acme Corp.');

insert into role(id, title) values
('e71a24b4-f639-4e46-8ecd-b4152d031076', 'Software Engineer'),
('d06945fe-4df5-49fa-85cd-11fcaeb3575d', 'Data Engineer');

insert into salary(role_id, company_id, amount, currency) values
('e71a24b4-f639-4e46-8ecd-b4152d031076', 'e25d8dc0-d57e-4ac6-a559-b3fce861c5be', 79000, 'EUR'),
('e71a24b4-f639-4e46-8ecd-b4152d031076', 'e25d8dc0-d57e-4ac6-a559-b3fce861c5be', 78000, 'EUR'),
('d06945fe-4df5-49fa-85cd-11fcaeb3575d', 'e25d8dc0-d57e-4ac6-a559-b3fce861c5be', 77000, 'EUR'),
('d06945fe-4df5-49fa-85cd-11fcaeb3575d', 'e25d8dc0-d57e-4ac6-a559-b3fce861c5be', 73000, 'EUR'),
('e71a24b4-f639-4e46-8ecd-b4152d031076', '65213830-9b8d-40cb-ac2b-dfedf38f1d73', 76000, 'USD'),
('e71a24b4-f639-4e46-8ecd-b4152d031076', '65213830-9b8d-40cb-ac2b-dfedf38f1d73', 80000, 'USD'),
('d06945fe-4df5-49fa-85cd-11fcaeb3575d', '65213830-9b8d-40cb-ac2b-dfedf38f1d73', 79000, 'USD'),
('d06945fe-4df5-49fa-85cd-11fcaeb3575d', '65213830-9b8d-40cb-ac2b-dfedf38f1d73', 74000, 'USD');

-- password: 'password'
insert into user_account(id, username, password) values
('2e188343-cfee-4755-928b-69527c1748a9', 'johndoe', '$pbkdf2$vxqo4vFP8J+LKW12igxZA9ipzGGmBGA/+gFZt7JrwME=$DSVlpC/9tKrKba8v9hTLuYXW0hHD47iHJa7Vk5joRvBXg38TScVLWaxmucIpSf10m8VLgNLwPS+KpQI7eBWq0Q');
insert into user_role(user_id, role) values
('2e188343-cfee-4755-928b-69527c1748a9', 'admin');
insert into role_permission(role, permission) values
('admin', 'write-company');
