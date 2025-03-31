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
      <el-form label-width="120px" :model="dataSourceConfig">
        <el-form-item label="同步频率">
          <el-select
            v-model="dataSourceConfig.frequency"
            placeholder="请选择同步频率"
          >
            <el-option label="实时" value="realtime"></el-option>
            <el-option label="每小时" value="hourly"></el-option>
            <el-option label="每天" value="daily"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="监测人员">
          <el-select
            v-model="dataSourceConfig.userId"
            filterable
            placeholder="请选择监测人员"
          >
            <el-option
              v-for="user in userList"
              :key="user.id"
              :label="user.name"
              :value="user.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
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
      <div slot="header" class="clearfix">
        <span>健康预警</span>
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
      <el-timeline v-else>
        <el-timeline-item
          v-for="(alert, index) in alerts"
          :key="index"
          :timestamp="alert.time"
          :type="alert.type"
          :color="alert.color"
          :icon="alert.icon"
        >
          {{ alert.message }}
          <el-button size="mini" type="primary" @click="showAlertDetail(alert)"
            >查看详情</el-button
          >
        </el-timeline-item>
      </el-timeline>
    </el-card>

    <el-card class="suggestion-card">
      <div slot="header" class="clearfix">
        <span>智能建议</span>
      </div>
      <div class="suggestion-content">
        <div
          v-for="(suggestion, index) in suggestions"
          :key="index"
          class="suggestion-item"
        >
          <div class="suggestion-icon">
            <i :class="suggestion.icon"></i>
          </div>
          <div class="suggestion-text">
            <h4>{{ suggestion.title }}</h4>
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
      if (!userId) return;

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
        })
        .catch((err) => {
          console.error("获取健康数据失败:", err);
          this.$message.error("获取健康数据失败");
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
        })
        .catch((err) => {
          console.error("获取图表数据失败:", err);
          this.$message.error("获取图表数据失败");
        });

      // 获取健康预警
      this.$http
        .get(`/smart-health/alerts/${userId}`)
        .then((res) => {
          if (res.status === 200) {
            this.alerts = res.data.map((alert) => {
              // 格式化时间
              const time = new Date(alert.alertTime);
              const formattedTime = `${time.getFullYear()}-${
                time.getMonth() + 1
              }-${time.getDate()} ${time.getHours()}:${time.getMinutes()}`;

              return {
                id: alert.id,
                type: alert.type,
                color: alert.color,
                icon: alert.icon,
                time: formattedTime,
                message: alert.message,
                detail: alert.detail,
                isRead: alert.isRead,
              };
            });
          }
        })
        .catch((err) => {
          console.error("获取健康预警失败:", err);
          this.$message.error("获取健康预警失败");
        });

      // 获取健康建议
      this.$http
        .get(`/smart-health/suggestions/${userId}`)
        .then((res) => {
          if (res.status === 200) {
            this.suggestions = res.data.map((suggestion) => {
              return {
                id: suggestion.id,
                icon: suggestion.icon,
                title: suggestion.title,
                content: suggestion.content,
                isRead: suggestion.isRead,
              };
            });
          }
        })
        .catch((err) => {
          console.error("获取健康建议失败:", err);
          this.$message.error("获取健康建议失败");
        });
    },
    showAlertDetail(alert) {
      this.$alert(alert.detail, alert.message, {
        confirmButtonText: "我知道了",
        callback: () => {
          // 如果未读，标记为已读
          if (!alert.isRead && alert.id) {
            this.$http
              .put(`/smart-health/alert/read/${alert.id}`)
              .then((res) => {
                if (res.status === 200 && res.data) {
                  alert.isRead = true;
                }
              })
              .catch((err) => {
                console.error("标记预警为已读失败:", err);
              });
          }
        },
      });
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
        this.dataSourceConfig.userId = currentUser.id;
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
        // 心率图表
        const heartRateChart = echarts.init(
          document.getElementById("heartRateChart")
        );
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
        const sleepChart = echarts.init(document.getElementById("sleepChart"));
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
        const bloodPressureChart = echarts.init(
          document.getElementById("bloodPressureChart")
        );
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
        const bloodSugarChart = echarts.init(
          document.getElementById("bloodSugarChart")
        );
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
      });
    },
  },
  mounted() {
    this.loadUserList();
    // 初始化后立即获取数据
    if (this.dataSourceConfig.userId) {
      this.fetchData();
    }
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
}

.data-card,
.chart-card,
.alert-card,
.suggestion-card,
.data-source-card {
  margin-bottom: 20px;
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

.no-alerts {
  text-align: center;
  color: #909399;
  margin: 20px 0;
  font-size: 14px;
}

.suggestion-content {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.suggestion-item {
  display: flex;
  padding: 10px;
  border-radius: 4px;
  background-color: #f9f9f9;
}

.suggestion-icon {
  font-size: 24px;
  margin-right: 15px;
  color: #409eff;
  display: flex;
  align-items: center;
}

.suggestion-text h4 {
  margin: 0 0 5px 0;
  font-size: 16px;
}

.suggestion-text p {
  margin: 0;
  font-size: 14px;
  color: #606266;
}

.action-buttons {
  display: flex;
  gap: 10px;
}
</style>
