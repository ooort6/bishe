package com.hss.healthyManager.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 智能健康建议
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Accessors(chain = true)
@Table(name = "smart_health_suggestion")
public class SmartHealthSuggestion extends BaseEntity {

    /**
     * 关联用户id
     */
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    /**
     * 建议生成时间
     */
    @Column(name = "suggestion_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date suggestionTime;

    /**
     * 图标
     */
    @Column
    private String icon;

    /**
     * 建议标题
     */
    @Column(name = "suggestion_title")
    private String suggestionTitle;

    /**
     * 建议内容
     */
    @Column(name = "suggestion_content", columnDefinition = "text")
    private String suggestionContent;

    /**
     * 关联的健康数据指标
     */
    @Column
    private String relatedMetric;

    /**
     * 是否已读
     */
    @Column(name = "is_read")
    private Boolean isRead;
}