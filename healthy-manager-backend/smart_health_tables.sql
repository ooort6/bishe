-- 智能健康分析相关的数据库表SQL脚本

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for smart_health_data
-- ----------------------------
DROP TABLE IF EXISTS `smart_health_data`;
CREATE TABLE `smart_health_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_datetime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_datetime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `data_time` timestamp(0) NOT NULL COMMENT '数据时间',
  `steps` int(11) NULL DEFAULT 0 COMMENT '步数',
  `calories` int(11) NULL DEFAULT 0 COMMENT '消耗卡路里',
  `heart_rate` int(11) NULL DEFAULT 0 COMMENT '心率',
  `blood_pressure_high` int(11) NULL DEFAULT 0 COMMENT '血压-高压',
  `blood_pressure_low` int(11) NULL DEFAULT 0 COMMENT '血压-低压',
  `blood_sugar` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '血糖值',
  `sleep_time` int(11) NULL DEFAULT 0 COMMENT '睡眠时长(分钟)',
  `deep_sleep_time` int(11) NULL DEFAULT 0 COMMENT '深度睡眠时长(分钟)',
  `light_sleep_time` int(11) NULL DEFAULT 0 COMMENT '浅度睡眠时长(分钟)',
  `active_minutes` int(11) NULL DEFAULT 0 COMMENT '活跃时长(分钟)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_data_time`(`data_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '智能健康数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for smart_health_alert
-- ----------------------------
DROP TABLE IF EXISTS `smart_health_alert`;
CREATE TABLE `smart_health_alert`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_datetime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_datetime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `alert_time` timestamp(0) NOT NULL COMMENT '提醒时间',
  `alert_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '提醒类型(心率异常、血压异常、血糖异常、睡眠不足等)',
  `alert_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '提醒级别(高、中、低)',
  `alert_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '提醒内容',
  `is_read` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已读(0:未读,1:已读)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_alert_time`(`alert_time`) USING BTREE,
  INDEX `idx_is_read`(`is_read`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '智能健康提醒表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for smart_health_suggestion
-- ----------------------------
DROP TABLE IF EXISTS `smart_health_suggestion`;
CREATE TABLE `smart_health_suggestion`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_datetime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_datetime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `suggestion_time` timestamp(0) NOT NULL COMMENT '建议时间',
  `suggestion_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '建议类型(运动、饮食、睡眠、生活习惯等)',
  `suggestion_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '建议标题',
  `suggestion_content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '建议内容',
  `is_read` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已读(0:未读,1:已读)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_suggestion_time`(`suggestion_time`) USING BTREE,
  INDEX `idx_is_read`(`is_read`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '智能健康建议表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- 添加智能健康分析菜单资源
-- ----------------------------
INSERT INTO `resources` (`create_datetime`, `update_datetime`, `icon`, `name`, `parent_id`, `permission`, `sort`, `type`, `url`) 
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'iconfont icon-jiankang', '智能健康分析', 0, '1', 3, 0, '/health/smartAnalysis');

-- ----------------------------
-- 添加智能健康分析菜单权限绑定（所有角色都可访问）
-- ----------------------------
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

SET FOREIGN_KEY_CHECKS = 1;
