-- 首先清空所有相关表的数据
TRUNCATE TABLE smart_health_data;
TRUNCATE TABLE smart_health_alert;
TRUNCATE TABLE smart_health_suggestion;

-- 为用户1添加完整的一天24小时心率、血压和血糖数据
-- 每小时一条记录，覆盖整天的变化
INSERT INTO smart_health_data (user_id, data_time, steps, calories, heart_rate, blood_pressure_high, blood_pressure_low, blood_sugar, sleep_time, deep_sleep_time, light_sleep_time, active_minutes)
VALUES 
-- 凌晨0点-5点（睡眠期间）
(1, DATE_SUB(NOW(), INTERVAL 24 HOUR), 0, 0, 62, 110, 70, 5.0, 0, 0, 0, 0),
(1, DATE_SUB(NOW(), INTERVAL 23 HOUR), 0, 0, 60, 108, 68, 4.9, 0, 0, 0, 0),
(1, DATE_SUB(NOW(), INTERVAL 22 HOUR), 0, 0, 58, 105, 65, 4.8, 0, 0, 0, 0),
(1, DATE_SUB(NOW(), INTERVAL 21 HOUR), 0, 0, 56, 102, 64, 4.7, 0, 0, 0, 0),
(1, DATE_SUB(NOW(), INTERVAL 20 HOUR), 0, 0, 55, 100, 65, 4.6, 0, 0, 0, 0),
(1, DATE_SUB(NOW(), INTERVAL 19 HOUR), 0, 0, 58, 102, 67, 4.5, 0, 0, 0, 0),

-- 早晨6点-11点（起床、早餐、上午活动）
(1, DATE_SUB(NOW(), INTERVAL 18 HOUR), 500, 20, 65, 112, 72, 5.4, 0, 0, 0, 5),
(1, DATE_SUB(NOW(), INTERVAL 17 HOUR), 1200, 50, 72, 118, 75, 6.2, 0, 0, 0, 12), -- 早餐后血糖升高
(1, DATE_SUB(NOW(), INTERVAL 16 HOUR), 2500, 100, 78, 120, 78, 5.8, 0, 0, 0, 20),
(1, DATE_SUB(NOW(), INTERVAL 15 HOUR), 3200, 130, 75, 118, 76, 5.5, 0, 0, 0, 15),
(1, DATE_SUB(NOW(), INTERVAL 14 HOUR), 4000, 160, 73, 115, 74, 5.2, 0, 0, 0, 18),
(1, DATE_SUB(NOW(), INTERVAL 13 HOUR), 4500, 180, 76, 117, 75, 5.1, 0, 0, 0, 22),

-- 中午12点-17点（午餐、午休、下午活动）
(1, DATE_SUB(NOW(), INTERVAL 12 HOUR), 5000, 200, 80, 122, 80, 6.5, 0, 0, 0, 10), -- 午餐后血糖升高
(1, DATE_SUB(NOW(), INTERVAL 11 HOUR), 5200, 210, 68, 115, 75, 6.0, 0, 0, 0, 5), -- 午休心率下降
(1, DATE_SUB(NOW(), INTERVAL 10 HOUR), 5500, 220, 72, 118, 76, 5.4, 0, 0, 0, 15),
(1, DATE_SUB(NOW(), INTERVAL 9 HOUR), 6000, 240, 82, 120, 78, 5.2, 0, 0, 0, 25), -- 下午运动
(1, DATE_SUB(NOW(), INTERVAL 8 HOUR), 6800, 270, 85, 122, 80, 5.0, 0, 0, 0, 30),
(1, DATE_SUB(NOW(), INTERVAL 7 HOUR), 7200, 290, 78, 120, 78, 4.9, 0, 0, 0, 20),

-- 晚上18点-23点（晚餐、休闲、睡前）
(1, DATE_SUB(NOW(), INTERVAL 6 HOUR), 7500, 300, 82, 125, 82, 6.3, 0, 0, 0, 15), -- 晚餐后血糖升高
(1, DATE_SUB(NOW(), INTERVAL 5 HOUR), 7800, 310, 76, 120, 78, 5.8, 0, 0, 0, 10),
(1, DATE_SUB(NOW(), INTERVAL 4 HOUR), 8000, 320, 70, 118, 76, 5.5, 0, 0, 0, 5),
(1, DATE_SUB(NOW(), INTERVAL 3 HOUR), 8200, 330, 68, 115, 74, 5.3, 0, 0, 0, 3),
(1, DATE_SUB(NOW(), INTERVAL 2 HOUR), 8300, 335, 65, 112, 72, 5.1, 0, 0, 0, 0),
(1, DATE_SUB(NOW(), INTERVAL 1 HOUR), 8350, 340, 62, 110, 70, 5.0, 0, 0, 0, 0);

-- 添加过去7天的睡眠数据
INSERT INTO smart_health_data (user_id, data_time, steps, calories, heart_rate, blood_pressure_high, blood_pressure_low, blood_sugar, sleep_time, deep_sleep_time, light_sleep_time, active_minutes)
VALUES 
(1, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '%Y-%m-%d 08:00:00'), 0, 0, 60, 110, 70, 5.0, 420, 150, 270, 0),
(1, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 2 DAY), '%Y-%m-%d 08:00:00'), 0, 0, 58, 112, 72, 5.1, 430, 160, 270, 0),
(1, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 3 DAY), '%Y-%m-%d 08:00:00'), 0, 0, 61, 114, 73, 5.0, 410, 140, 270, 0),
(1, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 4 DAY), '%Y-%m-%d 08:00:00'), 0, 0, 59, 113, 71, 5.2, 425, 155, 270, 0),
(1, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 5 DAY), '%Y-%m-%d 08:00:00'), 0, 0, 60, 111, 72, 5.1, 415, 145, 270, 0),
(1, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 6 DAY), '%Y-%m-%d 08:00:00'), 0, 0, 62, 113, 74, 5.0, 405, 135, 270, 0),
(1, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 7 DAY), '%Y-%m-%d 08:00:00'), 0, 0, 60, 110, 70, 5.1, 420, 150, 270, 0);

-- 添加健康预警
INSERT INTO smart_health_alert (user_id, alert_time, alert_type, alert_level, alert_content, is_read)
VALUES 
(1, NOW(), '心率异常', 'M', '您的心率在活动时段显著高于正常水平，达到了85次/分钟，请注意休息并避免剧烈运动', 0),
(1, DATE_SUB(NOW(), INTERVAL 1 DAY), '血压异常', 'H', '您的收缩压达到125mmHg，舒张压82mmHg，略高于正常范围，建议减少盐分摄入并保持规律作息', 0),
(1, DATE_SUB(NOW(), INTERVAL 2 DAY), '血糖异常', 'M', '您的餐后血糖值达到6.5mmol/L，建议调整饮食结构，减少精制碳水化合物的摄入', 0),
(1, DATE_SUB(NOW(), INTERVAL 3 DAY), '睡眠质量偏低', 'L', '您的深度睡眠时间比例较低，仅占总睡眠时间的35%，建议调整睡眠环境，保持规律作息', 1);

-- 添加健康建议
INSERT INTO smart_health_suggestion (user_id, suggestion_time, suggestion_type, suggestion_title, suggestion_content, is_read)
VALUES 
(1, NOW(), '运动建议', '增加有氧运动', '建议每天进行30分钟中等强度的有氧运动，如快走、慢跑等，有助于改善心血管健康和血压控制', 0),
(1, DATE_SUB(NOW(), INTERVAL 1 DAY), '饮食建议', '均衡饮食', '建议增加蔬果摄入，减少高盐高脂食物，保持饮食均衡，控制精制碳水化合物的摄入量', 0),
(1, DATE_SUB(NOW(), INTERVAL 2 DAY), '睡眠建议', '改善睡眠质量', '建议睡前1小时避免使用电子产品，营造安静舒适的睡眠环境，保持规律的作息时间', 1),
(1, DATE_SUB(NOW(), INTERVAL 3 DAY), '压力管理', '减轻日常压力', '建议尝试冥想或深呼吸练习，每天抽出10-15分钟进行放松，有助于降低压力水平和改善睡眠质量', 1); 