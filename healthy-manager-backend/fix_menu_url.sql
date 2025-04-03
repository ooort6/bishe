-- 检查是否已存在智能健康分析菜单
SELECT id FROM resources WHERE name = '智能健康分析';

-- 如果不存在，则添加智能健康分析菜单资源
INSERT INTO resources (create_datetime, update_datetime, icon, name, parent_id, permission, sort, type, url) 
SELECT NOW(), NOW(), 'iconfont icon-jiankang', '智能健康分析', 0, '1', 3, 0, '/smartAnalysis' 
WHERE NOT EXISTS (SELECT 1 FROM resources WHERE name = '智能健康分析');

-- 获取智能健康分析菜单的ID
SET @resourceId = (SELECT id FROM resources WHERE name = '智能健康分析');

-- 检查角色资源绑定是否存在，不存在则添加
INSERT INTO role_resource_bind (create_datetime, update_datetime, resource_id, role_id)
SELECT NOW(), NOW(), @resourceId, 1
WHERE NOT EXISTS (SELECT 1 FROM role_resource_bind WHERE resource_id = @resourceId AND role_id = 1);

INSERT INTO role_resource_bind (create_datetime, update_datetime, resource_id, role_id)
SELECT NOW(), NOW(), @resourceId, 2
WHERE NOT EXISTS (SELECT 1 FROM role_resource_bind WHERE resource_id = @resourceId AND role_id = 2);

INSERT INTO role_resource_bind (create_datetime, update_datetime, resource_id, role_id)
SELECT NOW(), NOW(), @resourceId, 3
WHERE NOT EXISTS (SELECT 1 FROM role_resource_bind WHERE resource_id = @resourceId AND role_id = 3); 