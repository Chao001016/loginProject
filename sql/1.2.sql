alter table usercenter.question
    add question_img longtext null comment '题目图片' after type;

delete from tag;
INSERT INTO usercenter.tag (id, content, creator, create_time, update_time, is_delete) VALUES (1, 'Java', null, '2024-07-28 00:38:40', '2024-07-28 00:38:40', 1);
INSERT INTO usercenter.tag (id, content, creator, create_time, update_time, is_delete) VALUES (2, 'Typescript', null, '2024-07-28 00:54:02', '2024-07-28 00:54:02', 1);
INSERT INTO usercenter.tag (id, content, creator, create_time, update_time, is_delete) VALUES (3, 'Javascript', null, '2024-07-28 00:54:02', '2024-07-28 00:54:02', 1);
INSERT INTO usercenter.tag (id, content, creator, create_time, update_time, is_delete) VALUES (4, 'Vite', null, '2024-07-28 00:54:02', '2024-07-28 00:54:02', 1);
INSERT INTO usercenter.tag (id, content, creator, create_time, update_time, is_delete) VALUES (5, 'Mybatis', null, '2024-07-28 00:54:02', '2024-07-28 00:54:02', 1);
INSERT INTO usercenter.tag (id, content, creator, create_time, update_time, is_delete) VALUES (6, 'Vuex', null, '2024-07-28 00:54:02', '2024-07-28 00:54:02', 1);
INSERT INTO usercenter.tag (id, content, creator, create_time, update_time, is_delete) VALUES (7, 'Pinia', null, '2024-07-28 00:54:02', '2024-07-28 00:54:02', 1);
INSERT INTO usercenter.tag (id, content, creator, create_time, update_time, is_delete) VALUES (8, 'Vue-router', null, '2024-07-28 00:54:02', '2024-07-28 00:54:02', 1);
INSERT INTO usercenter.tag (id, content, creator, create_time, update_time, is_delete) VALUES (9, 'Vue', null, '2024-07-28 00:54:02', '2024-07-28 00:54:02', 1);
INSERT INTO usercenter.tag (id, content, creator, create_time, update_time, is_delete) VALUES (10, 'Spring', null, '2024-07-28 00:54:02', '2024-07-28 00:54:02', 1);
INSERT INTO usercenter.tag (id, content, creator, create_time, update_time, is_delete) VALUES (21, '基础', null, '2024-08-11 20:41:36', '2024-08-11 20:41:36', 0);
INSERT INTO usercenter.tag (id, content, creator, create_time, update_time, is_delete) VALUES (22, '理论', null, '2024-08-11 20:41:36', '2024-08-11 20:41:36', 0);
INSERT INTO usercenter.tag (id, content, creator, create_time, update_time, is_delete) VALUES (23, '熟记', null, '2024-08-11 20:41:36', '2024-08-11 20:41:36', 0);
INSERT INTO usercenter.tag (id, content, creator, create_time, update_time, is_delete) VALUES (24, '创新', null, '2024-08-11 20:41:36', '2024-08-11 20:41:36', 0);
INSERT INTO usercenter.tag (id, content, creator, create_time, update_time, is_delete) VALUES (25, '设计模式', null, '2024-08-11 20:41:36', '2024-08-11 20:41:36', 0);
