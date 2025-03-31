package com.hss.healthyManager.dao;

import com.hss.healthyManager.entity.SmartHealthAlert;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SmartHealthAlertDao extends Mapper<SmartHealthAlert> {

    /**
     * 根据用户ID获取健康预警
     *
     * @param userId 用户ID
     * @param limit  限制记录数
     * @return 健康预警列表
     */
    List<SmartHealthAlert> getAlertsByUserId(Integer userId, Integer limit);

    /**
     * 根据用户ID获取未读的健康预警数量
     *
     * @param userId 用户ID
     * @return 未读预警数量
     */
    Integer getUnreadAlertsCountByUserId(Integer userId);
}