-- 修复智能健康分析菜单的名称
UPDATE resources SET name = '智能健康分析' WHERE id = 24;

-- 确保URL与前端路由保持一致
UPDATE resources SET url = '/smartAnalysis' WHERE id = 24;

-- 确保所有角色都能访问这个菜单
INSERT IGNORE INTO role_resource_bind (create_datetime, update_datetime, resource_id, role_id)
VALUES 
(NOW(), NOW(), 24, 1),
(NOW(), NOW(), 24, 2),
(NOW(), NOW(), 24, 3); 