-- 删除ID为24的菜单及其绑定关系
DELETE FROM role_resource_bind WHERE resource_id = 24;
DELETE FROM resources WHERE id = 24;

-- 添加一个新菜单，格式与"健康建议"菜单完全一致
INSERT INTO resources 
(id, create_datetime, update_datetime, icon, name, parent_id, permission, sort, type, url)
VALUES 
(24, '2019-04-16 16:31:35', '2019-04-16 16:48:15', 'iconfont icon-jiankang', '智能健康分析', 0, '1', 3, 0, '/smartAnalysis');

-- 添加角色绑定关系，与健康建议菜单绑定方式一致
INSERT INTO role_resource_bind 
(create_datetime, update_datetime, resource_id, role_id)
VALUES 
('2019-04-16 16:35:00', '2019-04-16 16:49:00', 24, 1),
('2019-04-16 16:35:00', '2019-04-16 16:49:00', 24, 2),
('2019-04-16 16:35:00', '2019-04-16 16:49:00', 24, 3); 