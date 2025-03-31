-- 清空现有数据
TRUNCATE TABLE smart_health_data;

-- 添加心率数据 - 每4小时一条记录，覆盖一天的变化
INSERT INTO smart_health_data (user_id, data_time, steps, calories, heart_rate, blood_pressure_high, blood_pressure_low, blood_sugar, sleep_time, deep_sleep_time, light_sleep_time, active_minutes)
VALUES 
(1, DATE_SUB(NOW(), INTERVAL 24 HOUR), 0, 0, 62, 115, 75, 5.2, 420, 150, 270, 0),
(1, DATE_SUB(NOW(), INTERVAL 20 HOUR), 1200, 50, 65, 118, 76, 5.0, 0, 0, 0, 15),
(1, DATE_SUB(NOW(), INTERVAL 16 HOUR), 3500, 150, 70, 120, 78, 5.8, 0, 0, 0, 25),
(1, DATE_SUB(NOW(), INTERVAL 12 HOUR), 6000, 250, 85, 125, 82, 6.2, 0, 0, 0, 35),
(1, DATE_SUB(NOW(), INTERVAL 8 HOUR), 7200, 300, 80, 122, 80, 5.9, 0, 0, 0, 40),
(1, DATE_SUB(NOW(), INTERVAL 4 HOUR), 8000, 330, 72, 120, 78, 5.7, 0, 0, 0, 42),
(1, NOW(), 8500, 350, 68, 118, 76, 5.6, 0, 0, 0, 45);

-- 添加睡眠数据记录 - 多天的记录
INSERT INTO smart_health_data (user_id, data_time, steps, calories, heart_rate, blood_pressure_high, blood_pressure_low, blood_sugar, sleep_time, deep_sleep_time, light_sleep_time, active_minutes)
VALUES 
(1, DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 0, 60, 110, 70, 5.0, 420, 150, 270, 0),
(1, DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 0, 58, 112, 72, 5.1, 400, 140, 260, 0),
(1, DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 0, 61, 115, 75, 5.0, 430, 160, 270, 0),
(1, DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 0, 59, 110, 70, 5.2, 410, 145, 265, 0),
(1, DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 0, 60, 112, 72, 5.1, 420, 150, 270, 0),
(1, DATE_SUB(NOW(), INTERVAL 6 DAY), 0, 0, 62, 114, 74, 5.0, 415, 155, 260, 0),
(1, DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 0, 60, 110, 70, 5.1, 425, 160, 265, 0); 