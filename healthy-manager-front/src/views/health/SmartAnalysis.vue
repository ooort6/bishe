<template>
  <div class="smart-analysis-container">
    <div class="header">
      <h1>智能健康分析</h1>
      <div class="action-buttons">
        <el-button type="primary" @click="startDataCollection"
          >开始数据采集</el-button
        >
        <el-button type="danger" @click="stopDataCollection"
          >停止采集</el-button
        >
        <el-button type="success" @click="refreshData">刷新数据</el-button>
      </div>
    </div>

    <el-card class="data-source-card">
      <div slot="header" class="clearfix">
        <span>监测配置</span>
      </div>
      <div class="data-source-content">
        <el-form
          label-position="left"
          label-width="100px"
          :model="dataSourceConfig"
        >
          <el-form-item label="同步频率">
            <el-select
              v-model="dataSourceConfig.frequency"
              placeholder="请选择同步频率"
              style="width: 100%"
            >
              <el-option label="实时" value="realtime"></el-option>
              <el-option label="每小时" value="hourly"></el-option>
              <el-option label="每天" value="daily"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="监测人员" required>
            <div class="user-select-container">
              <el-select
                v-model="dataSourceConfig.userId"
                filterable
                placeholder="请选择监测人员"
                style="width: 100%"
              >
                <el-option
                  v-for="user in userList"
                  :key="user.id"
                  :label="user.name"
                  :value="user.id"
                >
                </el-option>
              </el-select>
            </div>
            <div class="user-select-tip" v-if="!dataSourceConfig.userId">
              <i class="el-icon-warning"></i> 请先选择监测人员查看健康数据
            </div>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-row :gutter="20" class="dashboard-row">
      <el-col :span="8">
        <el-card class="data-card">
          <div slot="header" class="clearfix">
            <span>卡路里</span>
            <el-tag type="warning" size="small" style="margin-left: 10px"
              >实时</el-tag
            >
          </div>
          <div class="card-content">
            <div class="data-value">{{ healthData.calories }}</div>
            <div class="data-label">/ {{ healthData.caloriesTarget }} 卡</div>
            <el-progress
              :percentage="
                calculatePercentage(
                  healthData.calories,
                  healthData.caloriesTarget
                )
              "
              :color="progressColors"
            ></el-progress>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="data-card">
          <div slot="header" class="clearfix">
            <span>步数</span>
            <el-tag type="success" size="small" style="margin-left: 10px"
              >实时</el-tag
            >
          </div>
          <div class="card-content">
            <div class="data-value">{{ healthData.steps }}</div>
            <div class="data-label">/ {{ healthData.stepsTarget }} 步</div>
            <el-progress
              :percentage="
                calculatePercentage(healthData.steps, healthData.stepsTarget)
              "
              :color="progressColors"
            ></el-progress>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="data-card">
          <div slot="header" class="clearfix">
            <span>中高强度活动</span>
            <el-tag type="info" size="small" style="margin-left: 10px"
              >实时</el-tag
            >
          </div>
          <div class="card-content">
            <div class="data-value">{{ healthData.activeMinutes }}</div>
            <div class="data-label">
              / {{ healthData.activeMinutesTarget }} 分钟
            </div>
            <el-progress
              :percentage="
                calculatePercentage(
                  healthData.activeMinutes,
                  healthData.activeMinutesTarget
                )
              "
              :color="progressColors"
            ></el-progress>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="dashboard-row">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="clearfix">
            <span>心率趋势</span>
          </div>
          <div id="heartRateChart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="clearfix">
            <span>睡眠分析</span>
          </div>
          <div id="sleepChart" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="dashboard-row">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="clearfix">
            <span>血压监测</span>
          </div>
          <div id="bloodPressureChart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="clearfix">
            <span>血糖监测</span>
          </div>
          <div id="bloodSugarChart" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="alert-card">
      <div slot="header" class="clearfix card-header">
        <span class="card-title"
          ><i class="el-icon-warning-outline"></i> 健康预警</span
        >
        <el-badge
          :value="alerts.length"
          class="item"
          :hidden="alerts.length === 0"
        >
          <el-button size="small" type="danger" plain>查看所有</el-button>
        </el-badge>
      </div>
      <div v-if="alerts.length === 0" class="no-alerts">
        目前没有健康预警，继续保持良好习惯！
      </div>
      <el-timeline v-else class="alert-timeline">
        <el-timeline-item
          v-for="(alert, index) in alerts"
          :key="index"
          :timestamp="alert.time"
          :color="alert.color"
          :icon="alert.icon"
          class="custom-timeline-item"
        >
          <div class="alert-item">
            <div class="alert-item-content">
              <div class="alert-title" :style="{ color: alert.color }">
                <strong>{{ alert.type }}</strong>
              </div>
              <div class="alert-message">{{ alert.message }}</div>
            </div>
            <div class="alert-action">
              <el-button
                size="mini"
                type="primary"
                class="detail-btn"
                @click="showAlertDetail(alert)"
                :style="{
                  backgroundColor: alert.color,
                  borderColor: alert.color,
                }"
                >查看详情</el-button
              >
            </div>
          </div>
        </el-timeline-item>
      </el-timeline>
    </el-card>

    <el-card class="suggestion-card">
      <div slot="header" class="clearfix card-header">
        <span class="card-title"
          ><i class="el-icon-s-opportunity"></i> 智能建议</span
        >
      </div>
      <div class="suggestion-content">
        <div v-if="suggestions.length === 0" class="no-suggestions">
          目前没有智能建议，请继续保持健康的生活方式！
        </div>
        <div
          v-for="(suggestion, index) in suggestions"
          :key="index"
          class="suggestion-item"
          v-else
        >
          <div class="suggestion-icon">
            <i :class="suggestion.icon"></i>
          </div>
          <div class="suggestion-text">
            <h4>{{ suggestion.title || "健康建议" }}</h4>
            <p>{{ suggestion.content }}</p>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "SmartAnalysis",
  data() {
    return {
      collecting: false,
      timer: null,
      userList: [],
      dataSourceConfig: {
        frequency: "realtime",
        userId: "",
      },
      healthData: {
        calories: 0,
        caloriesTarget: 400,
        steps: 0,
        stepsTarget: 6000,
        activeMinutes: 0,
        activeMinutesTarget: 30,
        weight: 0,
        lastWeight: 0,
        weightDate: "",
        heartRate: 0,
        bloodPressure: {
          systolic: 0,
          diastolic: 0,
        },
        bloodSugar: 0,
        sleep: {
          deepSleep: 0,
          lightSleep: 0,
          remSleep: 0,
          awake: 0,
        },
      },
      alerts: [],
      suggestions: [],
      progressColors: [
        { color: "#f56c6c", percentage: 20 },
        { color: "#e6a23c", percentage: 40 },
        { color: "#5cb87a", percentage: 60 },
        { color: "#1989fa", percentage: 80 },
        { color: "#6f7ad3", percentage: 100 },
      ],
      chartData: {
        heartRate: {
          times: [],
          values: [],
        },
        bloodPressure: {
          times: [],
          systolic: [],
          diastolic: [],
        },
        bloodSugar: {
          times: [],
          values: [],
        },
      },
    };
  },
  watch: {
    "dataSourceConfig.userId": {
      handler(newValue, oldValue) {
        if (newValue && newValue !== oldValue) {
          console.log("用户ID已变更，重新获取数据:", newValue);
          this.fetchData();
          if (this.collecting) {
            this.stopDataCollection();
            this.startDataCollection();
          }
        } else if (!newValue) {
          this.clearAllData();
        }
      },
    },
  },
  methods: {
    startDataCollection() {
      if (this.collecting) return;

      if (!this.dataSourceConfig.userId) {
        this.$message({
          message: "请先选择监测人员",
          type: "warning",
        });
        return;
      }

      this.$message({
        message: "开始实时采集健康数据",
        type: "success",
      });

      this.collecting = true;
      this.timer = setInterval(() => {
        this.fetchData();
      }, 5000);
    },
    stopDataCollection() {
      if (!this.collecting) return;

      this.$message({
        message: "已停止数据采集",
        type: "info",
      });

      this.collecting = false;
      clearInterval(this.timer);
    },
    refreshData() {
      if (!this.dataSourceConfig.userId) {
        this.$message({
          message: "请先选择监测人员",
          type: "warning",
        });
        return;
      }

      this.$message({
        message: "数据已刷新",
        type: "success",
      });

      this.fetchData();
    },
    calculatePercentage(value, target) {
      if (!target || target === 0) {
        return 0; // 如果目标值为0或不存在，返回0%
      }
      return Math.min(Math.round((value / target) * 100), 100);
    },
    fetchData() {
      const userId = this.dataSourceConfig.userId;
      if (!userId) {
        // 如果没有选择用户，清空所有数据
        this.clearAllData();
        return;
      }

      // 显示加载中提示
      const loading = this.$loading({
        lock: true,
        text: "正在加载数据...",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });

      // 创建请求计数器，用于判断所有请求是否完成
      let requestCount = 4;
      const completeRequest = () => {
        requestCount--;
        if (requestCount <= 0) {
          // 所有请求完成后关闭加载提示
          loading.close();
        }
      };

      // 获取最新健康数据
      this.$http
        .get(`/smart-health/latest-data/${userId}`)
        .then((res) => {
          if (res.status === 200) {
            const data = res.data;
            this.healthData.calories = data.calories || 0;
            this.healthData.caloriesTarget = data.caloriesTarget || 400;
            this.healthData.steps = data.steps || 0;
            this.healthData.stepsTarget = data.stepsTarget || 8000;
            this.healthData.activeMinutes = data.activeMinutes || 0;
            this.healthData.activeMinutesTarget =
              data.activeMinutesTarget || 30;
            this.healthData.weight = data.weight || 0;
            this.healthData.lastWeight = data.lastWeight || 0;
            this.healthData.weightDate = data.weightDate || "";
            this.healthData.heartRate = data.heartRate || 0;
            // 修正字段名，与后端对应
            this.healthData.bloodPressure.systolic =
              data.bloodPressureHigh || 0;
            this.healthData.bloodPressure.diastolic =
              data.bloodPressureLow || 0;
            this.healthData.bloodSugar = data.bloodSugar || 0;
            // 注意：这些字段在实体类中不存在，需要在后端或前端处理
            this.healthData.sleep.deepSleep = data.deepSleepTime || 0;
            this.healthData.sleep.lightSleep = data.lightSleepTime || 0;
            this.healthData.sleep.remSleep = data.remSleep || 0;
            this.healthData.sleep.awake = data.awake || 0;

            console.log("获取到的健康数据:", data);
          }
          completeRequest();
        })
        .catch((err) => {
          console.error("获取健康数据失败:", err);
          this.$message.error("获取健康数据失败");
          completeRequest();
        });

      // 获取图表数据
      this.$http
        .get(`/smart-health/chart-data/${userId}`)
        .then((res) => {
          if (res.status === 200) {
            const chartData = res.data;

            // 心率图表数据
            if (chartData.heartRate) {
              this.chartData.heartRate.times = chartData.heartRate.times;
              this.chartData.heartRate.values = chartData.heartRate.values;
            }

            // 血压图表数据
            if (chartData.bloodPressure) {
              this.chartData.bloodPressure.times =
                chartData.bloodPressure.times;
              this.chartData.bloodPressure.systolic =
                chartData.bloodPressure.systolic;
              this.chartData.bloodPressure.diastolic =
                chartData.bloodPressure.diastolic;
            }

            // 血糖图表数据
            if (chartData.bloodSugar) {
              this.chartData.bloodSugar.times = chartData.bloodSugar.times;
              this.chartData.bloodSugar.values = chartData.bloodSugar.values;
            }

            // 更新图表
            this.initCharts();
          }
          completeRequest();
        })
        .catch((err) => {
          console.error("获取图表数据失败:", err);
          this.$message.error("获取图表数据失败");
          completeRequest();
        });

      // 获取健康预警
      this.$http
        .get(`/smart-health/alerts/${userId}`)
        .then((res) => {
          if (res.status === 200) {
            console.log("原始预警数据:", res.data);
            this.alerts = res.data.map((alert) => {
              // 格式化时间
              let formattedTime = "";
              try {
                if (alert.alertTime) {
                  const time = new Date(alert.alertTime);
                  formattedTime = `${time.getFullYear()}-${
                    time.getMonth() + 1
                  }-${time.getDate()} ${time.getHours()}:${time.getMinutes()}`;
                }
              } catch (error) {
                console.error("时间格式化失败:", error);
                formattedTime = alert.alertTime || "未知时间";
              }

              let alertColor = "blue";
              if (alert.color) {
                alertColor = alert.color;
              } else if (alert.alertLevel) {
                alertColor =
                  alert.alertLevel === "H"
                    ? "red"
                    : alert.alertLevel === "M"
                    ? "orange"
                    : "blue";
              }

              return {
                id: alert.id,
                type: alert.alertType || "健康预警",
                color: alertColor,
                icon: alert.icon || "el-icon-warning",
                time: formattedTime,
                message:
                  alert.alertContent || alert.message || "请注意您的健康状况",
                detail:
                  alert.detail ||
                  alert.alertContent ||
                  alert.message ||
                  "暂无详细信息",
                isRead: alert.isRead,
              };
            });
            console.log("处理后的健康预警数据:", this.alerts);
          }
          completeRequest();
        })
        .catch((err) => {
          console.error("获取健康预警失败:", err);
          this.$message.error("获取健康预警失败");
          completeRequest();
        });

      // 获取健康建议
      this.$http
        .get(`/smart-health/suggestions/${userId}`)
        .then((res) => {
          if (res.status === 200) {
            console.log("原始建议数据:", res.data);
            this.suggestions = res.data.map((suggestion) => {
              return {
                id: suggestion.id,
                icon: suggestion.icon || "el-icon-info",
                title:
                  suggestion.suggestionTitle || suggestion.title || "健康建议",
                content:
                  suggestion.suggestionContent ||
                  suggestion.content ||
                  "暂无建议内容",
                isRead: suggestion.isRead,
              };
            });
            console.log("处理后的健康建议数据:", this.suggestions);
          }
          completeRequest();
        })
        .catch((err) => {
          console.error("获取健康建议失败:", err);
          this.$message.error("获取健康建议失败");
          completeRequest();
        });
    },
    showAlertDetail(alert) {
      // 根据警告级别设置标题背景色
      const headerBgColor = this.getAlertColorClass(alert.color);

      // 使用更美观的弹窗
      this.$msgbox({
        title: alert.type,
        message: `
          <div class="alert-detail-container">
            <div class="alert-info">
              <p><i class="${alert.icon}" style="color: ${
          alert.color
        }; margin-right: 5px;"></i> <strong>${alert.message}</strong></p>
              <p class="alert-time">时间: ${alert.time}</p>
              <p class="alert-level">级别: <span class="level-tag" style="background-color: ${
                alert.color
              };">${this.getAlertLevelText(alert.type, alert.color)}</span></p>
            </div>
            ${
              alert.detail && alert.detail !== alert.message
                ? `<div class="alert-detail-content">
                <h4>详细说明</h4>
                <p>${alert.detail}</p>
               </div>`
                : ""
            }
            <div class="alert-suggestion">
              <h4>建议措施</h4>
              <p>${this.generateSuggestionForAlert(alert.type)}</p>
            </div>
          </div>
        `,
        showCancelButton: false,
        confirmButtonText: "我知道了",
        dangerouslyUseHTMLString: true,
        customClass: `custom-alert-detail alert-${headerBgColor}`,
        beforeClose: (action, instance, done) => {
          if (!alert.isRead && alert.id) {
            this.$http
              .put(`/smart-health/alert/read/${alert.id}`)
              .then((res) => {
                if (res.status === 200 && res.data) {
                  alert.isRead = true;
                  this.$message({
                    type: "success",
                    message: "已标记为已读",
                    duration: 1500,
                  });
                }
                done();
              })
              .catch((err) => {
                console.error("标记预警为已读失败:", err);
                done();
              });
          } else {
            done();
          }
        },
      });
    },
    generateSuggestionForAlert(alertType) {
      const suggestions = {
        血压异常:
          "定期监测血压，减少盐分摄入，保持适量运动，避免过度劳累和压力。",
        心率异常:
          "避免咖啡因摄入，保持充分休息，练习深呼吸放松技巧，若症状持续建议咨询医生。",
        血糖异常:
          "控制碳水化合物摄入，增加蔬菜水果和优质蛋白质的比例，保持规律运动，监测血糖变化。",
        睡眠异常:
          "保持规律作息，睡前避免使用电子设备，创造安静舒适的睡眠环境，考虑睡前放松活动如冥想。",
        健康预警: "请注意您的健康状况，保持良好的生活习惯，定期体检。",
      };

      return suggestions[alertType] || suggestions["健康预警"];
    },
    loadUserList() {
      // 从localStorage获取用户列表
      try {
        const usersStr = localStorage.getItem("users");
        if (usersStr) {
          this.userList = JSON.parse(usersStr);
        } else {
          // 如果localStorage中没有，则从API获取
          this.getUsersFromApi();
        }
      } catch (error) {
        console.error("获取用户列表出错:", error);
        this.getUsersFromApi();
      }

      // 设置默认用户为当前登录用户
      const currentUser = JSON.parse(localStorage.getItem("user"));
      if (currentUser && currentUser.id) {
        // 使用nextTick确保在DOM更新后再设置值，以触发watch
        this.$nextTick(() => {
          this.dataSourceConfig.userId = currentUser.id;
          console.log("已设置默认用户:", currentUser.id);
          // 由于watch的immediate为false，需要手动触发一次fetchData
          this.fetchData();
        });
      }
    },
    getUsersFromApi() {
      this.$http
        .get("/user/all")
        .then((res) => {
          if (res.status === 200) {
            this.userList = res.data;
          }
        })
        .catch((err) => {
          console.error("从API获取用户列表失败:", err);
        });
    },
    initCharts() {
      this.$nextTick(() => {
        try {
          // 检查DOM元素是否存在
          const heartRateChartElement =
            document.getElementById("heartRateChart");
          const sleepChartElement = document.getElementById("sleepChart");
          const bloodPressureChartElement =
            document.getElementById("bloodPressureChart");
          const bloodSugarChartElement =
            document.getElementById("bloodSugarChart");

          if (
            !heartRateChartElement ||
            !sleepChartElement ||
            !bloodPressureChartElement ||
            !bloodSugarChartElement
          ) {
            console.error("图表DOM元素不存在，无法初始化图表");
            return;
          }

          // 数据已经在clearAllData方法中设置了默认值，这里不再重复设置

          // 心率图表
          const heartRateChart = echarts.init(heartRateChartElement);
          heartRateChart.setOption({
            title: {
              text: "心率变化趋势",
            },
            tooltip: {
              trigger: "axis",
            },
            xAxis: {
              type: "category",
              data: this.chartData.heartRate.times,
            },
            yAxis: {
              type: "value",
              name: "心率(bpm)",
              min: 50,
            },
            series: [
              {
                data: this.chartData.heartRate.values,
                type: "line",
                smooth: true,
                lineStyle: {
                  color: "#f56c6c",
                },
                areaStyle: {
                  color: {
                    type: "linear",
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [
                      {
                        offset: 0,
                        color: "rgba(245, 108, 108, 0.5)",
                      },
                      {
                        offset: 1,
                        color: "rgba(245, 108, 108, 0.1)",
                      },
                    ],
                  },
                },
              },
            ],
          });

          // 睡眠图表
          const sleepChart = echarts.init(sleepChartElement);
          sleepChart.setOption({
            title: {
              text: "睡眠质量分析",
            },
            tooltip: {
              trigger: "item",
              formatter: "{a} <br/>{b}: {c} ({d}%)",
            },
            legend: {
              orient: "vertical",
              left: 10,
              top: 30,
              data: ["深度睡眠", "浅度睡眠", "REM睡眠", "清醒"],
            },
            series: [
              {
                name: "睡眠时长",
                type: "pie",
                radius: ["50%", "70%"],
                avoidLabelOverlap: false,
                label: {
                  show: false,
                  position: "center",
                },
                emphasis: {
                  label: {
                    show: true,
                    fontSize: "18",
                    fontWeight: "bold",
                  },
                },
                labelLine: {
                  show: false,
                },
                data: [
                  { value: this.healthData.sleep.deepSleep, name: "深度睡眠" },
                  { value: this.healthData.sleep.lightSleep, name: "浅度睡眠" },
                  { value: this.healthData.sleep.remSleep, name: "REM睡眠" },
                  { value: this.healthData.sleep.awake, name: "清醒" },
                ],
              },
            ],
          });

          // 血压图表
          const bloodPressureChart = echarts.init(bloodPressureChartElement);
          bloodPressureChart.setOption({
            title: {
              text: "血压监测",
            },
            tooltip: {
              trigger: "axis",
            },
            legend: {
              data: ["收缩压", "舒张压"],
            },
            xAxis: {
              type: "category",
              data: this.chartData.bloodPressure.times,
            },
            yAxis: {
              type: "value",
              name: "mmHg",
            },
            series: [
              {
                name: "收缩压",
                type: "line",
                data: this.chartData.bloodPressure.systolic,
                markLine: {
                  data: [
                    {
                      name: "正常上限",
                      yAxis: 120,
                      lineStyle: {
                        color: "#e6a23c",
                      },
                    },
                  ],
                },
              },
              {
                name: "舒张压",
                type: "line",
                data: this.chartData.bloodPressure.diastolic,
                markLine: {
                  data: [
                    {
                      name: "正常上限",
                      yAxis: 80,
                      lineStyle: {
                        color: "#e6a23c",
                      },
                    },
                  ],
                },
              },
            ],
          });

          // 血糖图表
          const bloodSugarChart = echarts.init(bloodSugarChartElement);
          bloodSugarChart.setOption({
            title: {
              text: "血糖监测",
            },
            tooltip: {
              trigger: "axis",
            },
            xAxis: {
              type: "category",
              data: this.chartData.bloodSugar.times,
            },
            yAxis: {
              type: "value",
              name: "mmol/L",
              min: 4,
            },
            series: [
              {
                data: this.chartData.bloodSugar.values,
                type: "line",
                smooth: true,
                markArea: {
                  data: [
                    [
                      {
                        name: "正常范围",
                        yAxis: 4.4,
                      },
                      {
                        yAxis: 6.1,
                      },
                    ],
                  ],
                  itemStyle: {
                    color: "rgba(92, 184, 122, 0.2)",
                  },
                },
              },
            ],
          });

          // 监听窗口大小变化，重绘图表
          window.addEventListener("resize", () => {
            heartRateChart.resize();
            sleepChart.resize();
            bloodPressureChart.resize();
            bloodSugarChart.resize();
          });
        } catch (error) {
          console.error("初始化图表时出错:", error);
        }
      });
    },
    // 根据预警颜色获取对应的CSS类名
    getAlertColorClass(color) {
      const colorMap = {
        red: "danger",
        orange: "warning",
        blue: "info",
        "#f56c6c": "danger",
        "#e6a23c": "warning",
        "#409eff": "info",
      };
      return colorMap[color] || "info";
    },

    // 根据预警类型获取级别文字
    getAlertLevelText(type, color) {
      if (color === "red" || color === "#f56c6c") return "高";
      if (color === "orange" || color === "#e6a23c") return "中";
      return "低";
    },

    // 清空所有数据
    clearAllData() {
      // 重置健康数据
      this.healthData = {
        calories: 0,
        caloriesTarget: 400,
        steps: 0,
        stepsTarget: 6000,
        activeMinutes: 0,
        activeMinutesTarget: 30,
        weight: 0,
        lastWeight: 0,
        weightDate: "",
        heartRate: 0,
        bloodPressure: {
          systolic: 0,
          diastolic: 0,
        },
        bloodSugar: 0,
        sleep: {
          deepSleep: 2.5, // 默认睡眠数据
          lightSleep: 4.5,
          remSleep: 1.2,
          awake: 0.5,
        },
      };

      // 重置图表数据为默认值
      const defaultTimes = ["00:00", "06:00", "12:00", "18:00", "23:59"];

      this.chartData = {
        heartRate: {
          times: [...defaultTimes],
          values: [65, 70, 75, 72, 68],
        },
        bloodPressure: {
          times: [...defaultTimes],
          systolic: [110, 115, 120, 118, 112],
          diastolic: [70, 75, 80, 78, 72],
        },
        bloodSugar: {
          times: [...defaultTimes],
          values: [5.0, 5.5, 6.0, 5.8, 5.2],
        },
      };

      // 重置预警和建议
      this.alerts = [];
      this.suggestions = [];

      // 重新初始化图表（在下一个周期，确保数据已更新）
      this.$nextTick(() => {
        this.initCharts();
      });
    },
    clearUserSelection() {
      this.dataSourceConfig.userId = "";
      this.$message({
        message: "已清空用户选择，数据已重置",
        type: "info",
      });
      // 数据将通过 watch 自动清空
    },
    refreshCharts() {
      try {
        // 获取图表元素
        const heartRateChartElement = document.getElementById("heartRateChart");
        const sleepChartElement = document.getElementById("sleepChart");
        const bloodPressureChartElement =
          document.getElementById("bloodPressureChart");
        const bloodSugarChartElement =
          document.getElementById("bloodSugarChart");

        if (
          heartRateChartElement &&
          sleepChartElement &&
          bloodPressureChartElement &&
          bloodSugarChartElement
        ) {
          // 检查图表实例是否已存在，如果存在则销毁
          const existingCharts = echarts.getInstanceByDom(
            heartRateChartElement
          );
          if (existingCharts) echarts.dispose(heartRateChartElement);

          const existingSleepChart =
            echarts.getInstanceByDom(sleepChartElement);
          if (existingSleepChart) echarts.dispose(sleepChartElement);

          const existingBPChart = echarts.getInstanceByDom(
            bloodPressureChartElement
          );
          if (existingBPChart) echarts.dispose(bloodPressureChartElement);

          const existingBSChart = echarts.getInstanceByDom(
            bloodSugarChartElement
          );
          if (existingBSChart) echarts.dispose(bloodSugarChartElement);

          // 短暂延迟后重新初始化图表，确保DOM已更新
          setTimeout(() => {
            // 重新初始化图表
            this.initCharts();
            // 触发resize事件以确保图表尺寸正确
            window.dispatchEvent(new Event("resize"));
            console.log("图表已强制刷新");
          }, 100);
        } else {
          console.warn("图表元素不存在，无法刷新");
        }
      } catch (error) {
        console.error("刷新图表时出错:", error);
      }
    },
  },
  mounted() {
    console.log("组件挂载 - 开始初始化过程");

    // 设置初始默认数据
    this.clearAllData();
    // 加载用户列表
    this.loadUserList();

    // 给页面足够的时间进行初始渲染
    setTimeout(() => {
      console.log("延迟后初始化图表");

      // 初始化图表
      this.initCharts();

      // 创建一个初始化完成后的回调链
      const setupComplete = () => {
        console.log("执行setupComplete - 调整图表大小");

        // 在初始化完成后触发窗口大小调整事件，强制图表重绘
        window.dispatchEvent(new Event("resize"));

        // 强制刷新图表
        this.refreshCharts();

        // 显示欢迎消息
        if (!this.dataSourceConfig.userId) {
          setTimeout(() => {
            this.$notify({
              title: "欢迎",
              message: "请选择一个监测人员以查看健康数据",
              type: "info",
              duration: 5000,
              position: "top-right",
            });
          }, 500);
        }
      };

      // 使用延时确保样式和图表完全渲染
      setTimeout(setupComplete, 1000);

      // 刷新页面
      // window.location.reload();
    }, 1500); // 增加初始化延迟
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer);
    }
  },
};
</script>

<style scoped>
.smart-analysis-container {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.dashboard-row {
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap;
}

.data-card,
.chart-card,
.alert-card,
.suggestion-card,
.data-source-card {
  margin-bottom: 20px;
  width: 100%;
}

.data-source-card {
  margin-bottom: 20px;
  width: 100%;
}

.data-source-content {
  padding: 10px 0;
}

/* 修复表单对齐问题 */
.data-source-card .el-form {
  width: 100% !important;
  display: block !important;
}

.data-source-card .el-form-item {
  margin-bottom: 18px !important;
  display: flex !important;
  align-items: center !important;
}

.data-source-card .el-form-item__content {
  flex: 1 !important;
  /* margin-left: 0 !important; */
}

.data-source-card .el-select {
  width: 100%;
}

/* 确保图表容器有固定尺寸 */
.chart-card {
  min-height: 370px;
}

#heartRateChart,
#sleepChart,
#bloodPressureChart,
#bloodSugarChart {
  height: 300px !important;
  width: 100% !important;
  min-height: 300px !important;
  display: block !important;
}

.card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px 0;
}

.data-value {
  font-size: 36px;
  font-weight: bold;
  color: #303133;
}

.data-label {
  font-size: 14px;
  color: #909399;
  margin: 5px 0 15px 0;
}

.no-alerts,
.no-suggestions {
  text-align: center;
  padding: 20px;
  color: #909399;
  font-size: 14px;
}

.alert-card,
.suggestion-card {
  margin-top: 20px;
}

/* 智能建议样式优化 */
.suggestion-content {
  padding: 15px;
}

.suggestion-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 15px;
  padding: 15px;
  border-radius: 6px;
  background-color: #f5f7fa;
  transition: all 0.3s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  border-left: 4px solid #409eff;
}

.suggestion-item:last-child {
  margin-bottom: 0;
}

.suggestion-item:hover {
  background-color: #ecf5ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.suggestion-icon {
  flex: 0 0 50px;
  height: 50px;
  line-height: 50px;
  text-align: center;
  font-size: 24px;
  color: #409eff;
  margin-right: 15px;
  background-color: rgba(64, 158, 255, 0.1);
  border-radius: 50%;
}

.suggestion-text {
  text-align: left;
  /* flex: 1; */
}

.suggestion-text h4 {
  margin: 0 0 10px 0;
  font-weight: 500;
  color: #303133;
  font-size: 16px;
}

.suggestion-text p {
  margin: 0;
  line-height: 1.6;
  color: #606266;
  font-size: 14px;
}

.el-timeline-item {
  padding-bottom: 20px;
}

.el-timeline-item .el-timeline-item__content {
  color: #303133;
}

.el-timeline-item strong {
  color: #409eff;
  margin-right: 5px;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

/* 详情弹框样式 */
::v-deep .custom-alert-detail {
  border-radius: 8px;
  overflow: hidden;
}

::v-deep .custom-alert-detail .el-message-box__header {
  padding: 15px 20px;
}

::v-deep .alert-danger .el-message-box__header {
  background-color: #f56c6c;
}

::v-deep .alert-warning .el-message-box__header {
  background-color: #e6a23c;
}

::v-deep .alert-info .el-message-box__header {
  background-color: #409eff;
}

::v-deep .custom-alert-detail .el-message-box__title {
  color: white;
  font-weight: bold;
  font-size: 18px;
}

::v-deep
  .custom-alert-detail
  .el-message-box__headerbtn
  .el-message-box__close {
  color: white;
}

::v-deep .custom-alert-detail .el-message-box__content {
  padding: 20px;
}

::v-deep .alert-detail-container {
  padding: 0;
}

::v-deep .alert-info {
  margin-bottom: 15px;
}

::v-deep .alert-info p {
  margin: 5px 0;
  line-height: 1.6;
}

::v-deep .alert-info .alert-time {
  color: #909399;
  font-size: 13px;
}

::v-deep .alert-level .level-tag {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 10px;
  color: white;
  font-size: 12px;
  font-weight: bold;
}

::v-deep .alert-detail-content,
::v-deep .alert-suggestion {
  background-color: #f5f7fa;
  border-radius: 6px;
  padding: 10px 15px;
  margin-top: 15px;
}

::v-deep .alert-detail-content h4,
::v-deep .alert-suggestion h4 {
  margin: 0 0 10px 0;
  font-size: 15px;
  color: #606266;
  font-weight: 500;
}

::v-deep .alert-detail-content p,
::v-deep .alert-suggestion p {
  margin: 0;
  color: #303133;
  line-height: 1.6;
}

::v-deep .custom-alert-detail .el-button--primary {
  width: 100%;
  margin-top: 10px;
}

::v-deep .alert-danger .el-button--primary {
  background-color: #f56c6c;
  border-color: #f56c6c;
}

::v-deep .alert-warning .el-button--primary {
  background-color: #e6a23c;
  border-color: #e6a23c;
}

::v-deep .alert-info .el-button--primary {
  background-color: #409eff;
  border-color: #409eff;
}

.user-select-container {
  display: flex;
  align-items: center;
  width: 100%;
}

.user-select-tip {
  margin-top: 8px;
  color: #e6a23c;
  font-size: 12px;
}

.user-select-tip i {
  margin-right: 4px;
}

.alert-timeline {
  padding: 0;
}

.custom-timeline-item {
  padding-bottom: 0;
}

.alert-item {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 12px 15px;
  margin-bottom: 5px;
  border-radius: 4px;
  border-bottom: 1px solid #ebeef5;
  background-color: #fafafa;
  transition: all 0.3s;
}

.alert-item:hover {
  background-color: #f5f7fa;
}

.alert-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
}

.alert-item-content {
  text-align: left;

  /* flex: 1; */
}

.alert-title {
  font-size: 15px;
  margin-bottom: 6px;
  font-weight: 500;
}

.alert-message {
  text-align: left;
  color: #606266;
  font-size: 13px;
  line-height: 1.5;
  margin-bottom: 5px;
}

.alert-action {
  text-align: left;

  /* flex: 0 0 auto; */
}

.detail-btn {
  padding: 8px 15px;
  font-size: 12px;
  border-radius: 4px;
  color: #fff;
  font-weight: 500;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
}

.detail-btn:hover {
  opacity: 0.9;
  transform: translateY(-1px);
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.15);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 18px;
  font-weight: bold;
}

::v-deep .alert-timeline .el-timeline-item__timestamp {
  text-align: left;
  color: #909399;
  font-size: 12px;
  padding-top: 3px;
}

::v-deep .alert-timeline .el-timeline-item__node {
  background-color: transparent;
  border: 2px solid;
}

::v-deep .alert-timeline .el-timeline-item__tail {
  border-left: 2px dashed #e4e7ed;
}
</style>
