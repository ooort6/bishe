package com.hss.healthyManager.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 智能健康分析数据
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Accessors(chain = true)
@Table(name = "smart_health_data")
public class SmartHealthData extends BaseEntity {

    /**
     * 关联用户id
     */
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    /**
     * 记录日期时间
     */
    @Column(name = "data_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dataTime;

    /**
     * 步数
     */
    @Column(name = "steps")
    private Integer steps;

    /**
     * 卡路里消耗
     */
    @Column(name = "calories")
    private Integer calories;

    /**
     * 心率
     */
    @Column(name = "heart_rate")
    private Integer heartRate;

    /**
     * 血压-高压
     */
    @Column(name = "blood_pressure_high")
    private Integer bloodPressureHigh;

    /**
     * 血压-低压
     */
    @Column(name = "blood_pressure_low")
    private Integer bloodPressureLow;

    /**
     * 血糖值
     */
    @Column(name = "blood_sugar")
    private Double bloodSugar;

    /**
     * 睡眠时长(分钟)
     */
    @Column(name = "sleep_time")
    private Integer sleepTime;

    /**
     * 深度睡眠时长(分钟)
     */
    @Column(name = "deep_sleep_time")
    private Integer deepSleepTime;

    /**
     * 浅度睡眠时长(分钟)
     */
    @Column(name = "light_sleep_time")
    private Integer lightSleepTime;

    /**
     * 活跃时长(分钟)
     */
    @Column(name = "active_minutes")
    private Integer activeMinutes;

    /**
     * 卡路里目标
     * 不映射到数据库表字段，仅用于前端显示
     */
    @Transient
    private Integer caloriesTarget;

    /**
     * 步数目标
     * 不映射到数据库表字段，仅用于前端显示
     */
    @Transient
    private Integer stepsTarget;

    /**
     * 活跃时长目标
     * 不映射到数据库表字段，仅用于前端显示
     */
    @Transient
    private Integer activeMinutesTarget;

    /**
     * REM睡眠时长
     * 不映射到数据库表字段，仅用于前端显示
     */
    @Transient
    private Double remSleep;

    /**
     * 清醒时长
     * 不映射到数据库表字段，仅用于前端显示
     */
    @Transient
    private Double awake;
}