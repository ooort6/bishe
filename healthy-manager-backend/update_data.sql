-- 清空睡眠数据，重新插入
DELETE FROM smart_health_data WHERE sleep_time > 0;

-- 插入新的睡眠数据
INSERT INTO smart_health_data 
(user_id, data_time, steps, calories, heart_rate, blood_pressure_high, blood_pressure_low, blood_sugar, sleep_time, deep_sleep_time, light_sleep_time, active_minutes)
VALUES 
(1, DATE_FORMAT(DATE_SUB(NOW(), INTERVAL 1 DAY), '%Y-%m-%d 08:00:00'), 0, 0, 60, 110, 70, 5.0, 420, 150, 270, 0);

-- 更新所有心率数据，确保不为空
UPDATE smart_health_data SET heart_rate = 70 WHERE heart_rate IS NULL;

-- 更新所有血压数据，确保不为空
UPDATE smart_health_data SET 
  blood_pressure_high = 120, 
  blood_pressure_low = 80 
WHERE blood_pressure_high IS NULL OR blood_pressure_low IS NULL; 