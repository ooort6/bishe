-- 更新智能健康分析菜单的URL，使其与前端路由匹配
UPDATE resources SET url = '/smartAnalysis' WHERE name = '智能健康分析';

-- 如果菜单不存在，则添加
INSERT INTO resources (create_datetime, update_datetime, icon, name, parent_id, permission, sort, type, url) 
SELECT NOW(), NOW(), 'iconfont icon-jiankang', '智能健康分析', 0, '1', 3, 0, '/smartAnalysis' 
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM resources WHERE name = '智能健康分析');

-- 获取智能健康分析菜单的ID
SET @resourceId = (SELECT id FROM resources WHERE name = '智能健康分析');

-- 确保菜单与所有角色绑定
INSERT IGNORE INTO role_resource_bind (create_datetime, update_datetime, resource_id, role_id)
VALUES 
(NOW(), NOW(), @resourceId, 1),
(NOW(), NOW(), @resourceId, 2),
(NOW(), NOW(), @resourceId, 3); 