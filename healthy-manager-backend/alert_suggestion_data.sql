-- 先清空健康预警和健康建议表
DELETE FROM smart_health_alert WHERE user_id = 1;
DELETE FROM smart_health_suggestion WHERE user_id = 1;

-- 添加健康预警测试数据
INSERT INTO smart_health_alert (user_id, alert_time, alert_type, alert_level, alert_content, is_read, icon, color)
VALUES
(1, NOW(), 'Heart Rate Alert', 'H', 'Your heart rate reached 95 bpm during rest period, which is higher than normal. Please avoid caffeine and consider relaxation techniques.', 0, 'el-icon-warning', 'red'),
(1, DATE_SUB(NOW(), INTERVAL 1 DAY), 'Blood Pressure Alert', 'M', 'Your systolic pressure was 132mmHg and diastolic was 88mmHg, slightly above normal range. Consider reducing salt intake.', 0, 'el-icon-warning-outline', 'orange'),
(1, DATE_SUB(NOW(), INTERVAL 2 DAY), 'Blood Sugar Alert', 'H', 'Post-meal blood sugar reached 7.2mmol/L. Please watch your carbohydrate intake and consider more physical activity.', 0, 'el-icon-warning', 'red'),
(1, DATE_SUB(NOW(), INTERVAL 3 DAY), 'Sleep Quality Alert', 'L', 'Deep sleep was only 18% of total sleep time last night. Try improving your sleep environment and maintain a regular schedule.', 1, 'el-icon-info', 'blue');

-- 添加健康建议测试数据
INSERT INTO smart_health_suggestion (user_id, suggestion_time, suggestion_title, suggestion_content, is_read, icon) 
VALUES
(1, NOW(), 'Improve Cardiovascular Health', 'Consider including 30 minutes of moderate aerobic exercise daily, such as brisk walking, cycling, or swimming. This can help improve heart health and regulate blood pressure.', 0, 'el-icon-bicycle'),
(1, DATE_SUB(NOW(), INTERVAL 1 DAY), 'Balance Your Diet', 'Increase consumption of fruits, vegetables, and whole grains. Limit processed foods and reduce sodium intake to help maintain healthy blood pressure levels.', 0, 'el-icon-food'),
(1, DATE_SUB(NOW(), INTERVAL 2 DAY), 'Enhance Sleep Quality', 'Establish a regular sleep schedule and create a restful environment. Avoid screens one hour before bedtime and consider relaxation techniques like deep breathing.', 0, 'el-icon-moon'),
(1, DATE_SUB(NOW(), INTERVAL 3 DAY), 'Stay Hydrated', 'Drink at least 2 liters of water daily. Proper hydration helps maintain energy levels, supports metabolism, and promotes overall health.', 1, 'el-icon-cold-drink'); 