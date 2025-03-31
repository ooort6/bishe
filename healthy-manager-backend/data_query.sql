USE healthy;
SELECT id, user_id, data_time, heart_rate, blood_pressure_high, blood_pressure_low 
FROM smart_health_data 
ORDER BY data_time DESC 
LIMIT 5; 