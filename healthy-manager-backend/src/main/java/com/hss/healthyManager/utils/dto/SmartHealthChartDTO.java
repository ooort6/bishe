package com.hss.healthyManager.utils.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 智能健康图表数据传输对象
 */
@Data
@Accessors(chain = true)
public class SmartHealthChartDTO {
    /**
     * 心率图表数据
     */
    private ChartData heartRate;

    /**
     * 血压图表数据
     */
    private BloodPressureData bloodPressure;

    /**
     * 血糖图表数据
     */
    private ChartData bloodSugar;

    /**
     * 睡眠图表数据
     */
    private SleepData sleep;

    /**
     * 图表基础数据结构
     */
    @Data
    @Accessors(chain = true)
    public static class ChartData {
        private List<String> times;
        private List<Number> values;
    }

    /**
     * 血压图表数据结构
     */
    @Data
    @Accessors(chain = true)
    public static class BloodPressureData {
        private List<String> times;
        private List<Integer> systolic;
        private List<Integer> diastolic;
    }

    /**
     * 睡眠图表数据结构
     */
    @Data
    @Accessors(chain = true)
    public static class SleepData {
        private Double deepSleep;
        private Double lightSleep;
        private Double remSleep;
        private Double awake;
    }
}