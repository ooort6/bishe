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
 * 智能健康预警
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Accessors(chain = true)
@Table(name = "smart_health_alert")
public class SmartHealthAlert extends BaseEntity {

    /**
     * 关联用户id
     */
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    /**
     * 预警时间
     */
    @Column(name = "alert_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date alertTime;

    /**
     * 预警类型
     */
    @Column(name = "alert_type")
    private String alertType;

    /**
     * 预警级别
     */
    @Column(name = "alert_level")
    private String alertLevel;

    /**
     * 预警内容
     */
    @Column(name = "alert_content")
    private String alertContent;

    /**
     * 预警颜色
     */
    @Column
    private String color;

    /**
     * 预警图标
     */
    @Column
    private String icon;

    /**
     * 预警消息
     */
    @Column
    private String message;

    /**
     * 详细信息
     */
    @Column(columnDefinition = "text")
    private String detail;

    /**
     * 是否已读
     */
    @Column(name = "is_read")
    private Boolean isRead;
}