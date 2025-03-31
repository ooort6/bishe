package com.hss.healthyManager.service.Impl;

import com.hss.healthyManager.dao.SmartHealthAlertDao;
import com.hss.healthyManager.dao.SmartHealthDataDao;
import com.hss.healthyManager.dao.SmartHealthSuggestionDao;
import com.hss.healthyManager.entity.SmartHealthAlert;
import com.hss.healthyManager.entity.SmartHealthData;
import com.hss.healthyManager.entity.SmartHealthSuggestion;
import com.hss.healthyManager.service.SmartHealthService;
import com.hss.healthyManager.utils.dto.SmartHealthChartDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

/**
 * 智能健康分析服务实现类
 */
@Service
public class SmartHealthServiceImpl implements SmartHealthService {

    @Resource
    private SmartHealthDataDao smartHealthDataDao;

    @Resource
    private SmartHealthAlertDao smartHealthAlertDao;

    @Resource
    private SmartHealthSuggestionDao smartHealthSuggestionDao;

    /**
     * 获取用户最新的健康数据
     */
    @Override
    public SmartHealthData getLatestHealthData(Integer userId) {
        SmartHealthData data = smartHealthDataDao.getLatestDataByUserId(userId);
        if (data == null) {
            // 如果没有数据，生成一些模拟数据并保存
            data = generateMockHealthData(userId);
            smartHealthDataDao.insert(data);
        }

        // 补充目标值字段
        // 这些字段在数据库表中不存在，但前端需要，所以在这里手动设置
        data.setCaloriesTarget(400); // 卡路里目标：400卡
        data.setStepsTarget(8000); // 步数目标：8000步
        data.setActiveMinutesTarget(30); // 活跃时间目标：30分钟

        // 添加睡眠数据计算字段
        // 将睡眠时间从分钟转换为小时，更符合显示习惯
        double totalSleepHours = data.getSleepTime() != null ? data.getSleepTime() / 60.0 : 7.0;
        double deepSleepHours = data.getDeepSleepTime() != null ? data.getDeepSleepTime() / 60.0 : 2.5;
        double lightSleepHours = data.getLightSleepTime() != null ? data.getLightSleepTime() / 60.0 : 3.5;

        // 计算REM睡眠和清醒时间（根据睡眠研究，REM通常占总睡眠时间的20-25%，清醒时间约占5-10%）
        double remSleepHours = totalSleepHours * 0.20; // REM睡眠约占总睡眠时间的20%
        double awakeHours = totalSleepHours * 0.08; // 清醒时间约占总睡眠时间的8%

        data.setRemSleep(remSleepHours);
        data.setAwake(awakeHours);

        // 打印日志便于调试
        System.out.println("返回的睡眠数据: 总时长=" + totalSleepHours + "小时, 深度=" + deepSleepHours +
                "小时, 浅度=" + lightSleepHours + "小时, REM=" + remSleepHours + "小时, 清醒=" + awakeHours + "小时");

        return data;
    }

    /**
     * 获取用户图表数据
     */
    @Override
    public SmartHealthChartDTO getHealthChartData(Integer userId) {
        try {
            // 初始化返回对象
            SmartHealthChartDTO chartDTO = new SmartHealthChartDTO();

            // 初始化心率数据
            SmartHealthChartDTO.ChartData heartRateData = new SmartHealthChartDTO.ChartData();
            heartRateData.setTimes(new ArrayList<>());
            heartRateData.setValues(new ArrayList<>());
            chartDTO.setHeartRate(heartRateData);

            // 初始化血压数据
            SmartHealthChartDTO.BloodPressureData bloodPressureData = new SmartHealthChartDTO.BloodPressureData();
            bloodPressureData.setTimes(new ArrayList<>());
            bloodPressureData.setSystolic(new ArrayList<>());
            bloodPressureData.setDiastolic(new ArrayList<>());
            chartDTO.setBloodPressure(bloodPressureData);

            // 初始化血糖数据
            SmartHealthChartDTO.ChartData bloodSugarData = new SmartHealthChartDTO.ChartData();
            bloodSugarData.setTimes(new ArrayList<>());
            bloodSugarData.setValues(new ArrayList<>());
            chartDTO.setBloodSugar(bloodSugarData);

            // 初始化睡眠数据
            SmartHealthChartDTO.SleepData sleepData = new SmartHealthChartDTO.SleepData();
            sleepData.setDeepSleep(2.5);
            sleepData.setLightSleep(4.5);
            sleepData.setRemSleep(1.2);
            sleepData.setAwake(0.5);
            chartDTO.setSleep(sleepData);

            // 填充数据
            try {
                fillHeartRateData(userId, chartDTO);
                fillBloodPressureData(userId, chartDTO);
                fillBloodSugarData(userId, chartDTO);
                fillSleepData(userId, chartDTO);
            } catch (Exception e) {
                System.err.println("填充图表数据时出错: " + e.getMessage());
                e.printStackTrace();
                // 继续使用默认值返回
            }

            return chartDTO;
        } catch (Exception e) {
            System.err.println("获取图表数据时出错: " + e.getMessage());
            e.printStackTrace();

            // 创建一个默认的空图表数据返回
            SmartHealthChartDTO fallbackDTO = new SmartHealthChartDTO();

            // 心率数据
            SmartHealthChartDTO.ChartData emptyHeartRate = new SmartHealthChartDTO.ChartData();
            emptyHeartRate.setTimes(new ArrayList<>());
            emptyHeartRate.setValues(new ArrayList<>());
            fallbackDTO.setHeartRate(emptyHeartRate);

            // 血压数据
            SmartHealthChartDTO.BloodPressureData emptyBloodPressure = new SmartHealthChartDTO.BloodPressureData();
            emptyBloodPressure.setTimes(new ArrayList<>());
            emptyBloodPressure.setSystolic(new ArrayList<>());
            emptyBloodPressure.setDiastolic(new ArrayList<>());
            fallbackDTO.setBloodPressure(emptyBloodPressure);

            // 血糖数据
            SmartHealthChartDTO.ChartData emptyBloodSugar = new SmartHealthChartDTO.ChartData();
            emptyBloodSugar.setTimes(new ArrayList<>());
            emptyBloodSugar.setValues(new ArrayList<>());
            fallbackDTO.setBloodSugar(emptyBloodSugar);

            // 睡眠数据
            SmartHealthChartDTO.SleepData emptySleep = new SmartHealthChartDTO.SleepData();
            emptySleep.setDeepSleep(2.5);
            emptySleep.setLightSleep(4.5);
            emptySleep.setRemSleep(1.2);
            emptySleep.setAwake(0.5);
            fallbackDTO.setSleep(emptySleep);

            return fallbackDTO;
        }
    }

    // 填充心率数据
    private void fillHeartRateData(Integer userId, SmartHealthChartDTO chartDTO) {
        List<SmartHealthData> heartRateData = smartHealthDataDao.getHeartRateDataByUserId(userId, 7);
        if (heartRateData == null || heartRateData.isEmpty()) {
            heartRateData = generateMockHeartRateData(userId, 7);
            for (SmartHealthData data : heartRateData) {
                smartHealthDataDao.insert(data);
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        List<String> times = new ArrayList<>();
        List<Number> values = new ArrayList<>();

        System.out.println("处理心率数据，共" + heartRateData.size() + "条记录");

        for (SmartHealthData data : heartRateData) {
            // 检查日期和心率是否为null
            if (data.getDataTime() != null && data.getHeartRate() != null) {
                try {
                    times.add(sdf.format(data.getDataTime()));
                    values.add(data.getHeartRate());
                    System.out.println("添加心率数据点: 时间=" + sdf.format(data.getDataTime()) + ", 心率=" + data.getHeartRate());
                } catch (Exception e) {
                    System.out.println("处理心率数据点时出错: " + e.getMessage());
                }
            } else {
                System.out.println("跳过空数据点: dataTime=" + data.getDataTime() + ", heartRate=" + data.getHeartRate());
            }
        }

        // 如果没有有效数据，添加一些默认值
        if (times.isEmpty() || values.isEmpty()) {
            System.out.println("没有有效的心率数据，使用默认值");
            times.add("00:00");
            times.add("06:00");
            times.add("12:00");
            times.add("18:00");
            times.add("23:00");

            values.add(65);
            values.add(70);
            values.add(75);
            values.add(80);
            values.add(68);
        }

        // 反转列表以便按时间顺序显示
        Collections.reverse(times);
        Collections.reverse(values);

        chartDTO.getHeartRate().setTimes(times);
        chartDTO.getHeartRate().setValues(values);
    }

    // 填充血压数据
    private void fillBloodPressureData(Integer userId, SmartHealthChartDTO chartDTO) {
        List<SmartHealthData> bloodPressureData = smartHealthDataDao.getBloodPressureDataByUserId(userId, 7);
        if (bloodPressureData == null || bloodPressureData.isEmpty()) {
            bloodPressureData = generateMockBloodPressureData(userId, 7);
            for (SmartHealthData data : bloodPressureData) {
                smartHealthDataDao.insert(data);
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        List<String> times = new ArrayList<>();
        List<Integer> systolicValues = new ArrayList<>();
        List<Integer> diastolicValues = new ArrayList<>();

        System.out.println("处理血压数据，共" + bloodPressureData.size() + "条记录");

        for (SmartHealthData data : bloodPressureData) {
            if (data.getDataTime() != null && data.getBloodPressureHigh() != null
                    && data.getBloodPressureLow() != null) {
                try {
                    times.add(sdf.format(data.getDataTime()));
                    systolicValues.add(data.getBloodPressureHigh());
                    diastolicValues.add(data.getBloodPressureLow());
                    System.out.println("添加血压数据点: 时间=" + sdf.format(data.getDataTime()) +
                            ", 高压=" + data.getBloodPressureHigh() + ", 低压=" + data.getBloodPressureLow());
                } catch (Exception e) {
                    System.out.println("处理血压数据点时出错: " + e.getMessage());
                }
            } else {
                System.out.println("跳过空血压数据点");
            }
        }

        // 如果没有有效数据，添加一些默认值
        if (times.isEmpty() || systolicValues.isEmpty() || diastolicValues.isEmpty()) {
            System.out.println("没有有效的血压数据，使用默认值");
            times.add("00:00");
            times.add("06:00");
            times.add("12:00");
            times.add("18:00");
            times.add("23:00");

            systolicValues.add(110);
            systolicValues.add(115);
            systolicValues.add(120);
            systolicValues.add(125);
            systolicValues.add(115);

            diastolicValues.add(70);
            diastolicValues.add(75);
            diastolicValues.add(80);
            diastolicValues.add(85);
            diastolicValues.add(75);
        }

        // 反转列表以便按时间顺序显示
        Collections.reverse(times);
        Collections.reverse(systolicValues);
        Collections.reverse(diastolicValues);

        chartDTO.getBloodPressure().setTimes(times);
        chartDTO.getBloodPressure().setSystolic(systolicValues);
        chartDTO.getBloodPressure().setDiastolic(diastolicValues);
    }

    // 填充血糖数据
    private void fillBloodSugarData(Integer userId, SmartHealthChartDTO chartDTO) {
        List<SmartHealthData> bloodSugarData = smartHealthDataDao.getBloodSugarDataByUserId(userId, 7);
        if (bloodSugarData == null || bloodSugarData.isEmpty()) {
            bloodSugarData = generateMockBloodSugarData(userId, 7);
            for (SmartHealthData data : bloodSugarData) {
                smartHealthDataDao.insert(data);
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        List<String> times = new ArrayList<>();
        List<Number> values = new ArrayList<>();

        System.out.println("处理血糖数据，共" + bloodSugarData.size() + "条记录");

        for (SmartHealthData data : bloodSugarData) {
            if (data.getDataTime() != null && data.getBloodSugar() != null) {
                try {
                    times.add(sdf.format(data.getDataTime()));
                    values.add(data.getBloodSugar());
                    System.out.println("添加血糖数据点: 时间=" + sdf.format(data.getDataTime()) +
                            ", 血糖=" + data.getBloodSugar());
                } catch (Exception e) {
                    System.out.println("处理血糖数据点时出错: " + e.getMessage());
                }
            } else {
                System.out.println("跳过空血糖数据点");
            }
        }

        // 如果没有有效数据，添加一些默认值
        if (times.isEmpty() || values.isEmpty()) {
            System.out.println("没有有效的血糖数据，使用默认值");
            times.add("00:00");
            times.add("06:00");
            times.add("12:00");
            times.add("18:00");
            times.add("23:00");

            values.add(5.0);
            values.add(5.5);
            values.add(6.0);
            values.add(5.8);
            values.add(5.2);
        }

        // 反转列表以便按时间顺序显示
        Collections.reverse(times);
        Collections.reverse(values);

        chartDTO.getBloodSugar().setTimes(times);
        chartDTO.getBloodSugar().setValues(values);
    }

    // 填充睡眠数据
    private void fillSleepData(Integer userId, SmartHealthChartDTO chartDTO) {
        SmartHealthData sleepData = smartHealthDataDao.getLatestSleepDataByUserId(userId);
        if (sleepData == null) {
            sleepData = generateMockSleepData(userId);
            smartHealthDataDao.insert(sleepData);
        }

        // 固定睡眠数据，即使数据库中存在数据也使用固定值
        chartDTO.getSleep().setDeepSleep(2.5);
        chartDTO.getSleep().setLightSleep(4.5);
        chartDTO.getSleep().setRemSleep(1.2);
        chartDTO.getSleep().setAwake(0.5);
    }

    /**
     * 获取用户健康预警
     */
    @Override
    public List<SmartHealthAlert> getHealthAlerts(Integer userId, Integer limit) {
        // 直接生成并返回模拟数据，不从数据库查询
        List<SmartHealthAlert> alerts = generateMockAlerts(userId, 4);

        // 打印日志，便于调试
        System.out.println("生成健康预警数量: " + alerts.size());
        return alerts;
    }

    /**
     * 获取用户健康建议
     */
    @Override
    public List<SmartHealthSuggestion> getHealthSuggestions(Integer userId, Integer limit) {
        // 直接生成并返回模拟数据，不从数据库查询
        List<SmartHealthSuggestion> suggestions = generateMockSuggestions(userId, 4);

        // 打印日志，便于调试
        System.out.println("生成健康建议数量: " + suggestions.size());
        return suggestions;
    }

    /**
     * 添加或更新健康数据
     */
    @Override
    public boolean saveHealthData(SmartHealthData healthData) {
        if (healthData.getId() == null) {
            return smartHealthDataDao.insert(healthData) > 0;
        } else {
            return smartHealthDataDao.updateByPrimaryKey(healthData) > 0;
        }
    }

    /**
     * 添加健康预警
     */
    @Override
    public boolean addHealthAlert(SmartHealthAlert alert) {
        return smartHealthAlertDao.insert(alert) > 0;
    }

    /**
     * 添加健康建议
     */
    @Override
    public boolean addHealthSuggestion(SmartHealthSuggestion suggestion) {
        return smartHealthSuggestionDao.insert(suggestion) > 0;
    }

    /**
     * 标记预警为已读
     */
    @Override
    public boolean markAlertAsRead(Integer alertId) {
        SmartHealthAlert alert = smartHealthAlertDao.selectByPrimaryKey(alertId);
        if (alert != null) {
            alert.setIsRead(true);
            return smartHealthAlertDao.updateByPrimaryKey(alert) > 0;
        }
        return false;
    }

    /**
     * 标记建议为已读
     */
    @Override
    public boolean markSuggestionAsRead(Integer suggestionId) {
        SmartHealthSuggestion suggestion = smartHealthSuggestionDao.selectByPrimaryKey(suggestionId);
        if (suggestion != null) {
            suggestion.setIsRead(true);
            return smartHealthSuggestionDao.updateByPrimaryKey(suggestion) > 0;
        }
        return false;
    }

    /**
     * 手动生成健康预警和建议
     */
    @Override
    public boolean generateAlertsAndSuggestions(Integer userId) {
        try {
            // 获取最新的健康数据
            SmartHealthData latestData = getLatestHealthData(userId);

            // 生成健康预警
            generateHealthAlerts(userId, latestData);

            // 生成健康建议
            generateHealthSuggestions(userId, latestData);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 生成健康预警
     */
    private void generateHealthAlerts(Integer userId, SmartHealthData data) {
        List<SmartHealthAlert> alerts = new ArrayList<>();

        // 检查血压
        if (data.getBloodPressureHigh() != null && data.getBloodPressureHigh() > 140) {
            SmartHealthAlert alert = new SmartHealthAlert();
            alert.setUserId(userId)
                    .setAlertTime(new Date())
                    .setAlertType("血压异常")
                    .setAlertLevel("高")
                    .setAlertContent(String.format("您的高压为%dmmHg，低压为%dmmHg，属于高血压范围，请注意监测",
                            data.getBloodPressureHigh(), data.getBloodPressureLow()))
                    .setIsRead(false);
            alerts.add(alert);
        }

        // 检查心率
        if (data.getHeartRate() != null && data.getHeartRate() > 100) {
            SmartHealthAlert alert = new SmartHealthAlert();
            alert.setUserId(userId)
                    .setAlertTime(new Date())
                    .setAlertType("心率异常")
                    .setAlertLevel("高")
                    .setAlertContent(String.format("心率%d次/分钟，高于正常范围。建议减少咖啡因摄入，保持充分休息。",
                            data.getHeartRate()))
                    .setIsRead(false);
            alerts.add(alert);
        }

        // 检查睡眠
        if (data.getDeepSleepTime() != null && data.getDeepSleepTime() < 90) {
            SmartHealthAlert alert = new SmartHealthAlert();
            alert.setUserId(userId)
                    .setAlertTime(new Date())
                    .setAlertType("睡眠异常")
                    .setAlertLevel("低")
                    .setAlertContent(String.format("您的深度睡眠时间仅为%.1f分钟，低于健康标准，请注意改善睡眠质量",
                            data.getDeepSleepTime()))
                    .setIsRead(false);
            alerts.add(alert);
        }

        // 保存预警
        for (SmartHealthAlert alert : alerts) {
            smartHealthAlertDao.insert(alert);
        }
    }

    /**
     * 生成健康建议
     */
    private void generateHealthSuggestions(Integer userId, SmartHealthData data) {
        List<SmartHealthSuggestion> suggestions = new ArrayList<>();

        // 水分摄入建议
        SmartHealthSuggestion suggestion = new SmartHealthSuggestion();
        suggestion.setUserId(userId)
                .setSuggestionTime(new Date())
                .setIcon("el-icon-cold-drink")
                .setSuggestionTitle("增加水分摄入")
                .setSuggestionContent("保持充足的水分摄入对维持身体健康非常重要。建议每天饮水量达到1.5-2升，尤其是在运动后或天气炎热时更应及时补充水分。")
                .setIsRead(false);
        suggestions.add(suggestion);

        // 保存建议
        for (SmartHealthSuggestion sug : suggestions) {
            smartHealthSuggestionDao.insert(sug);
        }
    }

    /**
     * 生成模拟的健康数据
     */
    private SmartHealthData generateMockHealthData(Integer userId) {
        SmartHealthData data = new SmartHealthData();
        data.setUserId(userId)
                .setDataTime(new Date())
                .setCalories(37)
                .setSteps(8500)
                .setActiveMinutes(45)
                .setHeartRate(75)
                .setBloodPressureHigh(120)
                .setBloodPressureLow(80)
                .setBloodSugar(5.6)
                .setDeepSleepTime(150)
                .setLightSleepTime(270)
                .setSleepTime(420);
        return data;
    }

    /**
     * 生成模拟的心率数据
     */
    private List<SmartHealthData> generateMockHeartRateData(Integer userId, int count) {
        List<SmartHealthData> dataList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // 更真实的心率数据曲线，呈现出日常活动规律
        int[] timePoints = { 0, 4, 8, 12, 16, 20, 23 };
        int[] values = { 62, 65, 70, 85, 80, 75, 68 }; // 睡眠时低，活动时高

        for (int i = 0; i < timePoints.length; i++) {
            SmartHealthData data = new SmartHealthData();
            calendar.set(Calendar.HOUR_OF_DAY, timePoints[i]);
            data.setUserId(userId)
                    .setDataTime(calendar.getTime())
                    .setHeartRate(values[i]);

            // 添加更多相关指标，使数据更完整
            if (i >= 2 && i <= 5) { // 白天活动时段
                data.setSteps(2000 * (i - 1))
                        .setCalories(100 * (i - 1))
                        .setActiveMinutes(15 * (i - 1));
            } else { // 夜间休息时段
                data.setSteps(500)
                        .setCalories(30)
                        .setActiveMinutes(5);
            }

            // 血压数据
            data.setBloodPressureHigh(110 + values[i] - 60)
                    .setBloodPressureLow(70 + (values[i] - 60) / 2);

            // 血糖数据 - 早晨空腹低，餐后高
            if (timePoints[i] == 8) {
                data.setBloodSugar(5.0); // 早晨空腹
            } else if (timePoints[i] == 12 || timePoints[i] == 20) {
                data.setBloodSugar(6.2); // 餐后
            } else {
                data.setBloodSugar(5.5); // 平时
            }

            // 打印日志便于调试
            System.out.println("添加心率数据: 时间=" + data.getDataTime() + ", 心率=" + data.getHeartRate());

            dataList.add(data);
        }

        return dataList;
    }

    /**
     * 生成模拟的血压数据
     */
    private List<SmartHealthData> generateMockBloodPressureData(Integer userId, int count) {
        List<SmartHealthData> dataList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // 添加更多时间点，确保有足够的数据显示趋势
        int[] timePoints = { 0, 4, 8, 12, 16, 20, 23 };
        int[] systolic = { 115, 110, 120, 135, 130, 125, 118 };
        int[] diastolic = { 75, 72, 78, 90, 85, 82, 76 };

        for (int i = 0; i < timePoints.length; i++) {
            SmartHealthData data = new SmartHealthData();
            calendar.set(Calendar.HOUR_OF_DAY, timePoints[i]);
            data.setUserId(userId)
                    .setDataTime(calendar.getTime())
                    .setBloodPressureHigh(systolic[i])
                    .setBloodPressureLow(diastolic[i])
                    .setSteps(1000 + i * 1000) // 添加其他数据以使记录更完整
                    .setCalories(50 + i * 30)
                    .setActiveMinutes(10 + i * 5)
                    .setHeartRate(60 + i * 3)
                    .setBloodSugar(5.0 + (i % 3) * 0.5); // 添加血糖数据
            dataList.add(data);

            // 打印日志便于调试
            System.out.println("添加血压数据: 时间=" + data.getDataTime() + ", 高压=" + data.getBloodPressureHigh() +
                    ", 低压=" + data.getBloodPressureLow());
        }

        return dataList;
    }

    /**
     * 生成模拟的血糖数据
     */
    private List<SmartHealthData> generateMockBloodSugarData(Integer userId, int count) {
        List<SmartHealthData> dataList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // 添加更多时间点，确保有足够的数据显示趋势
        int[] timePoints = { 0, 4, 8, 12, 16, 20, 23 };
        double[] values = { 5.1, 4.9, 5.8, 6.3, 5.9, 5.7, 5.3 };

        for (int i = 0; i < timePoints.length; i++) {
            SmartHealthData data = new SmartHealthData();
            calendar.set(Calendar.HOUR_OF_DAY, timePoints[i]);
            data.setUserId(userId)
                    .setDataTime(calendar.getTime())
                    .setBloodSugar(values[i])
                    .setSteps(1000 + i * 1000) // 添加其他数据以使记录更完整
                    .setCalories(50 + i * 30)
                    .setActiveMinutes(10 + i * 5)
                    .setHeartRate(60 + i * 3)
                    .setBloodPressureHigh(110 + i * 2) // 添加血压数据
                    .setBloodPressureLow(70 + i);
            dataList.add(data);

            // 打印日志便于调试
            System.out.println("添加血糖数据: 时间=" + data.getDataTime() + ", 血糖=" + data.getBloodSugar());
        }

        return dataList;
    }

    /**
     * 生成模拟的睡眠数据
     */
    private SmartHealthData generateMockSleepData(Integer userId) {
        SmartHealthData data = new SmartHealthData();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1); // 前一天的睡眠数据

        // 更真实的睡眠数据，总时长7小时，其中深度睡眠2.5小时，浅度睡眠4.5小时
        int totalSleepMinutes = 420; // 7小时 = 420分钟
        int deepSleepMinutes = 150; // 2.5小时 = 150分钟
        int lightSleepMinutes = 270; // 4.5小时 = 270分钟

        data.setUserId(userId)
                .setDataTime(calendar.getTime())
                .setDeepSleepTime(deepSleepMinutes)
                .setLightSleepTime(lightSleepMinutes)
                .setSleepTime(totalSleepMinutes)
                .setHeartRate(60) // 睡眠时心率较低
                .setBloodPressureHigh(110)
                .setBloodPressureLow(70)
                .setBloodSugar(5.0);
        return data;
    }

    /**
     * 生成模拟的健康预警
     */
    private List<SmartHealthAlert> generateMockAlerts(Integer userId, int count) {
        List<SmartHealthAlert> alerts = new ArrayList<>();

        // 血压预警
        SmartHealthAlert alert1 = new SmartHealthAlert();
        alert1.setUserId(userId)
                .setAlertTime(new Date())
                .setAlertType("血压异常")
                .setAlertLevel("高")
                .setAlertContent("收缩压135mmHg，舒张压90mmHg，略高于标准值。请注意降低盐分摄入，增加有氧运动。")
                .setColor("red")
                .setIcon("el-icon-warning")
                .setIsRead(false);
        alerts.add(alert1);

        // 心率预警
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -3);
        SmartHealthAlert alert2 = new SmartHealthAlert();
        alert2.setUserId(userId)
                .setAlertTime(cal.getTime())
                .setAlertType("心率异常")
                .setAlertLevel("高")
                .setAlertContent("在过去的一小时内，您的心率从75次/分钟升高到112次/分钟。若无剧烈运动，请注意休息。")
                .setColor("red")
                .setIcon("el-icon-warning")
                .setIsRead(false);
        alerts.add(alert2);

        // 睡眠预警
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DAY_OF_MONTH, -1);
        SmartHealthAlert alert3 = new SmartHealthAlert();
        alert3.setUserId(userId)
                .setAlertTime(cal2.getTime())
                .setAlertType("睡眠异常")
                .setAlertLevel("低")
                .setAlertContent("昨晚深度睡眠仅占总睡眠时间的22%，低于健康标准。建议调整睡眠环境，保持规律作息。")
                .setColor("blue")
                .setIcon("el-icon-info")
                .setIsRead(true);
        alerts.add(alert3);

        // 血糖预警
        Calendar cal3 = Calendar.getInstance();
        cal3.add(Calendar.DAY_OF_MONTH, -2);
        SmartHealthAlert alert4 = new SmartHealthAlert();
        alert4.setUserId(userId)
                .setAlertTime(cal3.getTime())
                .setAlertType("血糖异常")
                .setAlertLevel("中")
                .setAlertContent("餐后血糖值达到7.2mmol/L，建议调整饮食结构，减少精制碳水化合物的摄入。")
                .setColor("orange")
                .setIcon("el-icon-warning-outline")
                .setIsRead(false);
        alerts.add(alert4);

        return alerts;
    }

    /**
     * 生成模拟的健康建议
     */
    private List<SmartHealthSuggestion> generateMockSuggestions(Integer userId, int count) {
        List<SmartHealthSuggestion> suggestions = new ArrayList<>();

        // 运动建议
        SmartHealthSuggestion suggestion1 = new SmartHealthSuggestion();
        suggestion1.setUserId(userId)
                .setSuggestionTime(new Date())
                .setIcon("el-icon-bicycle")
                .setSuggestionTitle("增加运动时间")
                .setSuggestionContent("建议每天保持至少30分钟中等强度的运动，如快走、游泳或骑自行车。规律的运动有助于改善心肺功能，降低慢性疾病风险。")
                .setIsRead(false);
        suggestions.add(suggestion1);

        // 饮食建议
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        SmartHealthSuggestion suggestion2 = new SmartHealthSuggestion();
        suggestion2.setUserId(userId)
                .setSuggestionTime(cal.getTime())
                .setIcon("el-icon-food")
                .setSuggestionTitle("调整饮食结构")
                .setSuggestionContent("建议采用均衡饮食模式，增加蔬菜水果摄入，适量摄取全谷物和优质蛋白，减少精加工食品和含糖饮料的消费。")
                .setIsRead(false);
        suggestions.add(suggestion2);

        // 水分建议
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DAY_OF_MONTH, -2);
        SmartHealthSuggestion suggestion3 = new SmartHealthSuggestion();
        suggestion3.setUserId(userId)
                .setSuggestionTime(cal2.getTime())
                .setIcon("el-icon-cold-drink")
                .setSuggestionTitle("增加水分摄入")
                .setSuggestionContent("保持充足的水分摄入对维持身体健康非常重要。建议每天饮水量达到1.5-2升，尤其是在运动后或天气炎热时更应及时补充水分。")
                .setIsRead(false);
        suggestions.add(suggestion3);

        // 睡眠建议
        Calendar cal3 = Calendar.getInstance();
        cal3.add(Calendar.DAY_OF_MONTH, -3);
        SmartHealthSuggestion suggestion4 = new SmartHealthSuggestion();
        suggestion4.setUserId(userId)
                .setSuggestionTime(cal3.getTime())
                .setIcon("el-icon-moon")
                .setSuggestionTitle("改善睡眠质量")
                .setSuggestionContent("建议睡前1小时避免使用电子产品，营造安静舒适的睡眠环境，保持规律的作息时间，有助于提高睡眠质量。")
                .setIsRead(true);
        suggestions.add(suggestion4);

        return suggestions;
    }
}