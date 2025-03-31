-- 先清空健康预警和健康建议表
DELETE FROM smart_health_alert WHERE user_id = 1;
DELETE FROM smart_health_suggestion WHERE user_id = 1;

-- 添加健康预警测试数据
INSERT INTO smart_health_alert (user_id, alert_time, alert_type, alert_level, alert_content, is_read, icon, color)
VALUES
(1, NOW(), '心率异常', 'H', '您的静息心率达到95次/分钟，高于正常水平。建议避免咖啡因摄入，考虑放松技巧。', b'0', 'el-icon-warning', 'red'),
(1, DATE_SUB(NOW(), INTERVAL 1 DAY), '血压异常', 'M', '您的收缩压132mmHg，舒张压88mmHg，略高于正常范围。请考虑减少盐分摄入。', b'0', 'el-icon-warning-outline', 'orange'),
(1, DATE_SUB(NOW(), INTERVAL 2 DAY), '血糖异常', 'H', '餐后血糖达到7.2mmol/L。请注意碳水化合物摄入，并考虑增加身体活动。', b'0', 'el-icon-warning', 'red'),
(1, DATE_SUB(NOW(), INTERVAL 3 DAY), '睡眠质量低', 'L', '昨晚深度睡眠仅占总睡眠时间的18%。尝试改善睡眠环境，保持规律作息。', b'1', 'el-icon-info', 'blue');

-- 添加健康建议测试数据
INSERT INTO smart_health_suggestion (user_id, suggestion_time, suggestion_title, suggestion_content, is_read, icon) 
VALUES
(1, NOW(), '提高心血管健康', '建议每天进行30分钟中等强度的有氧运动，如快走、骑车或游泳。这有助于改善心脏健康和调节血压。', b'0', 'el-icon-bicycle'),
(1, DATE_SUB(NOW(), INTERVAL 1 DAY), '均衡饮食', '增加水果、蔬菜和全谷物的摄入。限制加工食品并减少钠摄入，有助于维持健康的血压水平。', b'0', 'el-icon-food'),
(1, DATE_SUB(NOW(), INTERVAL 2 DAY), '提高睡眠质量', '建立规律的睡眠计划，创造舒适的睡眠环境。睡前一小时避免使用电子屏幕，考虑使用深呼吸等放松技巧。', b'0', 'el-icon-moon'),
(1, DATE_SUB(NOW(), INTERVAL 3 DAY), '保持水分', '每天至少喝2升水。适当的水分摄入有助于维持能量水平，支持新陈代谢，促进整体健康。', b'1', 'el-icon-cold-drink'); 