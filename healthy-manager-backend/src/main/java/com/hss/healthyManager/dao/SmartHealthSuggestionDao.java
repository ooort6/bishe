package com.hss.healthyManager.dao;

import com.hss.healthyManager.entity.SmartHealthSuggestion;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SmartHealthSuggestionDao extends Mapper<SmartHealthSuggestion> {

    /**
     * 根据用户ID获取健康建议
     *
     * @param userId 用户ID
     * @param limit  限制记录数
     * @return 健康建议列表
     */
    List<SmartHealthSuggestion> getSuggestionsByUserId(Integer userId, Integer limit);
}