-- 删除资源ID为24的所有角色资源绑定（避免重复）
DELETE FROM role_resource_bind WHERE resource_id = 24;

-- 删除原始菜单
DELETE FROM resources WHERE id = 24;

-- 按照系统中已有菜单的完全相同格式添加智能健康分析菜单
INSERT INTO resources (id, create_datetime, update_datetime, icon, name, parent_id, permission, sort, type, url) 
VALUES (
  24, 
  NOW(), 
  NOW(), 
  'iconfont icon-jiankang', 
  '智能健康分析', 
  0, 
  '1', 
  3, 
  0, 
  '/smartAnalysis'
);

-- 添加角色绑定（按照已有菜单的绑定方式）
INSERT INTO role_resource_bind (create_datetime, update_datetime, resource_id, role_id)
VALUES 
(NOW(), NOW(), 24, 1),  -- 管理员
(NOW(), NOW(), 24, 2),  -- 学生
(NOW(), NOW(), 24, 3);  -- 辅导员 