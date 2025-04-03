-- 添加智能健康分析菜单资源（注意URL与前端路由保持一致）
INSERT INTO `resources` (`create_datetime`, `update_datetime`, `icon`, `name`, `parent_id`, `permission`, `sort`, `type`, `url`) 
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'iconfont icon-jiankang', '智能健康分析', 0, '1', 3, 0, '/smartAnalysis');

-- 获取新插入的资源ID
SET @resourceId = LAST_INSERT_ID();

-- 添加管理员权限绑定
INSERT INTO `role_resource_bind` (`create_datetime`, `update_datetime`, `resource_id`, `role_id`) 
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, @resourceId, 1);

-- 添加学生权限绑定
INSERT INTO `role_resource_bind` (`create_datetime`, `update_datetime`, `resource_id`, `role_id`) 
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, @resourceId, 2);

-- 添加辅导员权限绑定
INSERT INTO `role_resource_bind` (`create_datetime`, `update_datetime`, `resource_id`, `role_id`) 
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, @resourceId, 3); 