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
