package com.hss.healthyManager.controller;

import com.hss.healthyManager.entity.SmartHealthAlert;
import com.hss.healthyManager.entity.SmartHealthData;
import com.hss.healthyManager.entity.SmartHealthSuggestion;
import com.hss.healthyManager.service.SmartHealthService;
import com.hss.healthyManager.utils.dto.SmartHealthChartDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 智能健康分析控制器
 */
@Api(description = "智能健康分析相关接口")
@RestController
@RequestMapping("/api/smart-health")
public class SmartHealthController {

    @Resource
    private SmartHealthService smartHealthService;

    /**
     * 获取用户最新的健康数据
     */
    @ApiOperation("获取用户最新的健康数据")
    @GetMapping("/latest-data/{userId}")
    public ResponseEntity<SmartHealthData> getLatestHealthData(
            @ApiParam("用户ID") @PathVariable Integer userId) {
        return ResponseEntity.ok(smartHealthService.getLatestHealthData(userId));
    }

    /**
     * 获取用户图表数据
     */
    @ApiOperation("获取用户图表数据")
    @GetMapping("/chart-data/{userId}")
    public ResponseEntity<?> getHealthChartData(
            @ApiParam("用户ID") @PathVariable Integer userId) {
        try {
            // 添加日志
            System.out.println("请求图表数据接口: 用户ID=" + userId);
            return ResponseEntity.ok(smartHealthService.getHealthChartData(userId));
        } catch (Exception e) {
            // 记录错误
            System.err.println("获取图表数据失败: " + e.getMessage());
            e.printStackTrace();

            // 返回错误信息
            return ResponseEntity.status(500).body("获取图表数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户健康预警
     */
    @ApiOperation("获取用户健康预警")
    @GetMapping("/alerts/{userId}")
    public ResponseEntity<List<SmartHealthAlert>> getHealthAlerts(
            @ApiParam("用户ID") @PathVariable Integer userId,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(smartHealthService.getHealthAlerts(userId, limit));
    }

    /**
     * 获取用户健康建议
     */
    @ApiOperation("获取用户健康建议")
    @GetMapping("/suggestions/{userId}")
    public ResponseEntity<List<SmartHealthSuggestion>> getHealthSuggestions(
            @ApiParam("用户ID") @PathVariable Integer userId,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(smartHealthService.getHealthSuggestions(userId, limit));
    }

    /**
     * 添加健康数据
     */
    @ApiOperation("添加健康数据")
    @RequiresPermissions("smartHealth:update")
    @PostMapping("/data")
    public ResponseEntity<Boolean> saveHealthData(
            @ApiParam("健康数据") @RequestBody SmartHealthData healthData) {
        return ResponseEntity.ok(smartHealthService.saveHealthData(healthData));
    }

    /**
     * 标记预警为已读
     */
    @ApiOperation("标记预警为已读")
    @PutMapping("/alert/read/{alertId}")
    public ResponseEntity<Boolean> markAlertAsRead(
            @ApiParam("预警ID") @PathVariable Integer alertId) {
        return ResponseEntity.ok(smartHealthService.markAlertAsRead(alertId));
    }

    /**
     * 标记建议为已读
     */
    @ApiOperation("标记建议为已读")
    @PutMapping("/suggestion/read/{suggestionId}")
    public ResponseEntity<Boolean> markSuggestionAsRead(
            @ApiParam("建议ID") @PathVariable Integer suggestionId) {
        return ResponseEntity.ok(smartHealthService.markSuggestionAsRead(suggestionId));
    }

    /**
     * 手动生成健康预警和建议
     */
    @ApiOperation("手动生成健康预警和建议")
    @RequiresPermissions("smartHealth:update")
    @PostMapping("/generate/{userId}")
    public ResponseEntity<Boolean> generateAlertsAndSuggestions(
            @ApiParam("用户ID") @PathVariable Integer userId) {
        return ResponseEntity.ok(smartHealthService.generateAlertsAndSuggestions(userId));
    }
}