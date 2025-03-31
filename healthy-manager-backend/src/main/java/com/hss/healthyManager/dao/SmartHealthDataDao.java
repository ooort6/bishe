package com.hss.healthyManager.dao;

import com.hss.healthyManager.entity.SmartHealthData;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SmartHealthDataDao extends Mapper<SmartHealthData> {

    /**
     * 根据用户ID获取最新的健康数据
     *
     * @param userId 用户ID
     * @return 健康数据
     */
    SmartHealthData getLatestDataByUserId(Integer userId);

    /**
     * 根据用户ID获取心率图表数据
     *
     * @param userId 用户ID
     * @param limit  限制记录数
     * @return 图表数据
     */
    List<SmartHealthData> getHeartRateDataByUserId(Integer userId, Integer limit);

    /**
     * 根据用户ID获取血压图表数据
     *
     * @param userId 用户ID
     * @param limit  限制记录数
     * @return 图表数据
     */
    List<SmartHealthData> getBloodPressureDataByUserId(Integer userId, Integer limit);

    /**
     * 根据用户ID获取血糖图表数据
     *
     * @param userId 用户ID
     * @param limit  限制记录数
     * @return 图表数据
     */
    List<SmartHealthData> getBloodSugarDataByUserId(Integer userId, Integer limit);

    /**
     * 根据用户ID获取最新的睡眠数据
     *
     * @param userId 用户ID
     * @return 睡眠数据
     */
    SmartHealthData getLatestSleepDataByUserId(Integer userId);
}