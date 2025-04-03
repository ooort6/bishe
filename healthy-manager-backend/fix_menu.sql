-- 使URL路径与前端路由完全匹配
INSERT INTO `t_resource` (`resource_name`, `resource_type`, `resource_url`, `parent_id`, `resource_permission`) 
VALUES ('智能健康分析', 'menu', '/smartAnalysis', 0, NULL);

-- 获取新插入的资源ID
SET @resourceId = LAST_INSERT_ID();

-- 添加角色资源绑定
INSERT INTO `t_role_resource` (`role_id`, `resource_id`) VALUES (1, @resourceId);
INSERT INTO `t_role_resource` (`role_id`, `resource_id`) VALUES (2, @resourceId);
INSERT INTO `t_role_resource` (`role_id`, `resource_id`) VALUES (3, @resourceId); 