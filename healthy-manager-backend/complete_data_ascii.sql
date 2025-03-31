-- Clear all tables first
TRUNCATE TABLE smart_health_data;
TRUNCATE TABLE smart_health_alert;
TRUNCATE TABLE smart_health_suggestion;

-- Add 24-hour health data for user 1
-- Record for each hour to cover an entire day
INSERT INTO smart_health_data (user_id, data_time, steps, calories, heart_rate, blood_pressure_high, blood_pressure_low, blood_sugar, sleep_time, deep_sleep_time, light_sleep_time, active_minutes)
VALUES 
-- 0-5 AM (sleeping hours)
(1, DATE_SUB(NOW(), INTERVAL 24 HOUR), 0, 0, 62, 110, 70, 5.0, 0, 0, 0, 0),
(1, DATE_SUB(NOW(), INTERVAL 23 HOUR), 0, 0, 60, 108, 68, 4.9, 0, 0, 0, 0),
(1, DATE_SUB(NOW(), INTERVAL 22 HOUR), 0, 0, 58, 105, 65, 4.8, 0, 0, 0, 0),
(1, DATE_SUB(NOW(), INTERVAL 21 HOUR), 0, 0, 56, 102, 64, 4.7, 0, 0, 0, 0),
(1, DATE_SUB(NOW(), INTERVAL 20 HOUR), 0, 0, 55, 100, 65, 4.6, 0, 0, 0, 0),
(1, DATE_SUB(NOW(), INTERVAL 19 HOUR), 0, 0, 58, 102, 67, 4.5, 0, 0, 0, 0),

-- 6-11 AM (morning activity)
(1, DATE_SUB(NOW(), INTERVAL 18 HOUR), 500, 20, 65, 112, 72, 5.4, 0, 0, 0, 5),
(1, DATE_SUB(NOW(), INTERVAL 17 HOUR), 1200, 50, 72, 118, 75, 6.2, 0, 0, 0, 12), -- after breakfast
(1, DATE_SUB(NOW(), INTERVAL 16 HOUR), 2500, 100, 78, 120, 78, 5.8, 0, 0, 0, 20),
(1, DATE_SUB(NOW(), INTERVAL 15 HOUR), 3200, 130, 75, 118, 76, 5.5, 0, 0, 0, 15),
(1, DATE_SUB(NOW(), INTERVAL 14 HOUR), 4000, 160, 73, 115, 74, 5.2, 0, 0, 0, 18),
(1, DATE_SUB(NOW(), INTERVAL 13 HOUR), 4500, 180, 76, 117, 75, 5.1, 0, 0, 0, 22),

-- 12-5 PM (lunch, afternoon)
(1, DATE_SUB(NOW(), INTERVAL 12 HOUR), 5000, 200, 80, 122, 80, 6.5, 0, 0, 0, 10), -- after lunch
(1, DATE_SUB(NOW(), INTERVAL 11 HOUR), 5200, 210, 68, 115, 75, 6.0, 0, 0, 0, 5), -- rest
(1, DATE_SUB(NOW(), INTERVAL 10 HOUR), 5500, 220, 72, 118, 76, 5.4, 0, 0, 0, 15),
(1, DATE_SUB(NOW(), INTERVAL 9 HOUR), 6000, 240, 82, 120, 78, 5.2, 0, 0, 0, 25), -- exercise
(1, DATE_SUB(NOW(), INTERVAL 8 HOUR), 6800, 270, 85, 122, 80, 5.0, 0, 0, 0, 30),
(1, DATE_SUB(NOW(), INTERVAL 7 HOUR), 7200, 290, 78, 120, 78, 4.9, 0, 0, 0, 20),

-- 6-11 PM (evening)
(1, DATE_SUB(NOW(), INTERVAL 6 HOUR), 7500, 300, 82, 125, 82, 6.3, 0, 0, 0, 15), -- after dinner
(1, DATE_SUB(NOW(), INTERVAL 5 HOUR), 7800, 310, 76, 120, 78, 5.8, 0, 0, 0, 10),
(1, DATE_SUB(NOW(), INTERVAL 4 HOUR), 8000, 320, 70, 118, 76, 5.5, 0, 0, 0, 5),
(1, DATE_SUB(NOW(), INTERVAL 3 HOUR), 8200, 330, 68, 115, 74, 5.3, 0, 0, 0, 3),
(1, DATE_SUB(NOW(), INTERVAL 2 HOUR), 8300, 335, 65, 112, 72, 5.1, 0, 0, 0, 0),
(1, DATE_SUB(NOW(), INTERVAL 1 HOUR), 8350, 340, 62, 110, 70, 5.0, 0, 0, 0, 0);

-- Add sleep data for past 7 days
INSERT INTO smart_health_data (user_id, data_time, steps, calories, heart_rate, blood_pressure_high, blood_pressure_low, blood_sugar, sleep_time, deep_sleep_time, light_sleep_time, active_minutes)
VALUES 
(1, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '%Y-%m-%d 08:00:00'), 0, 0, 60, 110, 70, 5.0, 420, 150, 270, 0),
(1, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 2 DAY), '%Y-%m-%d 08:00:00'), 0, 0, 58, 112, 72, 5.1, 430, 160, 270, 0),
(1, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 3 DAY), '%Y-%m-%d 08:00:00'), 0, 0, 61, 114, 73, 5.0, 410, 140, 270, 0),
(1, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 4 DAY), '%Y-%m-%d 08:00:00'), 0, 0, 59, 113, 71, 5.2, 425, 155, 270, 0),
(1, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 5 DAY), '%Y-%m-%d 08:00:00'), 0, 0, 60, 111, 72, 5.1, 415, 145, 270, 0),
(1, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 6 DAY), '%Y-%m-%d 08:00:00'), 0, 0, 62, 113, 74, 5.0, 405, 135, 270, 0),
(1, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 7 DAY), '%Y-%m-%d 08:00:00'), 0, 0, 60, 110, 70, 5.1, 420, 150, 270, 0);

-- Add health alerts
INSERT INTO smart_health_alert (user_id, alert_time, alert_type, alert_level, alert_content, is_read)
VALUES 
(1, NOW(), 'Heart Rate Alert', 'M', 'Your heart rate was significantly higher than normal during activity, reaching 85 bpm. Please rest and avoid strenuous exercise.', 0),
(1, DATE_SUB(NOW(), INTERVAL 1 DAY), 'Blood Pressure Alert', 'H', 'Your systolic pressure reached 125mmHg and diastolic 82mmHg, slightly above normal range. Consider reducing salt intake.', 0),
(1, DATE_SUB(NOW(), INTERVAL 2 DAY), 'Blood Sugar Alert', 'M', 'Your post-meal blood sugar reached 6.5mmol/L. Consider adjusting your diet to reduce refined carbohydrates.', 0),
(1, DATE_SUB(NOW(), INTERVAL 3 DAY), 'Sleep Quality Alert', 'L', 'Your deep sleep time was low at only 35% of total sleep. Consider improving sleep environment and routine.', 1);

-- Add health suggestions
INSERT INTO smart_health_suggestion (user_id, suggestion_time, suggestion_title, suggestion_content, is_read)
VALUES 
(1, NOW(), 'Increase Aerobic Exercise', 'Recommend 30 minutes of moderate aerobic exercise daily such as brisk walking or jogging to improve cardiovascular health.', 0),
(1, DATE_SUB(NOW(), INTERVAL 1 DAY), 'Balanced Diet', 'Increase vegetable and fruit intake, reduce high-salt and high-fat foods, and maintain balanced meals.', 0),
(1, DATE_SUB(NOW(), INTERVAL 2 DAY), 'Improve Sleep Quality', 'Avoid electronic devices 1 hour before bedtime and create a quiet, comfortable sleep environment.', 1),
(1, DATE_SUB(NOW(), INTERVAL 3 DAY), 'Reduce Daily Stress', 'Try meditation or deep breathing exercises for 10-15 minutes daily to reduce stress and improve sleep quality.', 1); 