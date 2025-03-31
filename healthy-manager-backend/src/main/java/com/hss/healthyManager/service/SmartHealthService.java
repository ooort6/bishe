package com.hss.healthyManager.service;

import com.hss.healthyManager.entity.SmartHealthAlert;
import com.hss.healthyManager.entity.SmartHealthData;
import com.hss.healthyManager.entity.SmartHealthSuggestion;
import com.hss.healthyManager.utils.dto.SmartHealthChartDTO;

import java.util.List;

/**
 * 智能健康分析服务接口
 */
public interface SmartHealthService {

    /**
     * 获取用户最新的健康数据
     * 
     * @param userId 用户ID
     * @return 健康数据
     */
    SmartHealthData getLatestHealthData(Integer userId);

    /**
     * 获取用户图表数据
     * 
     * @param userId 用户ID
     * @return 图表数据
     */
    SmartHealthChartDTO getHealthChartData(Integer userId);

    /**
     * 获取用户健康预警
     * 
     * @param userId 用户ID
     * @param limit  限制数量
     * @return 健康预警列表
     */
    List<SmartHealthAlert> getHealthAlerts(Integer userId, Integer limit);

    /**
     * 获取用户健康建议
     * 
     * @param userId 用户ID
     * @param limit  限制数量
     * @return 健康建议列表
     */
    List<SmartHealthSuggestion> getHealthSuggestions(Integer userId, Integer limit);

    /**
     * 添加或更新健康数据
     * 
     * @param healthData 健康数据
     * @return 是否成功
     */
    boolean saveHealthData(SmartHealthData healthData);

    /**
     * 添加健康预警
     * 
     * @param alert 健康预警
     * @return 是否成功
     */
    boolean addHealthAlert(SmartHealthAlert alert);

    /**
     * 添加健康建议
     * 
     * @param suggestion 健康建议
     * @return 是否成功
     */
    boolean addHealthSuggestion(SmartHealthSuggestion suggestion);

    /**
     * 标记预警为已读
     * 
     * @param alertId 预警ID
     * @return 是否成功
     */
    boolean markAlertAsRead(Integer alertId);

    /**
     * 标记建议为已读
     * 
     * @param suggestionId 建议ID
     * @return 是否成功
     */
    boolean markSuggestionAsRead(Integer suggestionId);

    /**
     * 手动生成健康预警和建议
     * 
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean generateAlertsAndSuggestions(Integer userId);
}