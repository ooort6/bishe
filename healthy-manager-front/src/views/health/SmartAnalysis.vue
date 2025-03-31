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
        calories: 37,
        caloriesTarget: 400,
        steps: 788,
        stepsTarget: 6000,
        activeMinutes: 0,
        activeMinutesTarget: 30,
        weight: 87.8,
        lastWeight: 86.5,
        weightDate: "9月18日 18:45",
        heartRate: 75,
        bloodPressure: {
          systolic: 120,
          diastolic: 80,
        },
        bloodSugar: 5.6,
        sleep: {
          deepSleep: 2.5,
          lightSleep: 4.3,
          remSleep: 1.2,
          awake: 0.5,
        },
      },
      alerts: [
        {
          type: "warning",
          color: "#E6A23C",
          icon: "el-icon-warning",
          time: "2023-09-18 14:30",
          message: "您的血压略高于正常值，建议注意休息",
          detail:
            "收缩压135mmHg，舒张压90mmHg，略高于标准值。请注意降低盐分摄入，增加有氧运动。",
        },
      ],
      suggestions: [
        {
          icon: "el-icon-heavy-rain",
          title: "增加运动时间",
          content:
            "根据您近期的数据，建议每天增加30分钟的中等强度运动，如快走或慢跑。",
        },
        {
          icon: "el-icon-food",
          title: "调整饮食结构",
          content:
            "您的体重有上升趋势，建议减少碳水化合物摄入，增加蛋白质和蔬菜比例。",
        },
        {
          icon: "el-icon-cold-drink",
          title: "增加水分摄入",
          content: "您的活动量增加，但水分摄入不足，建议每天饮水量达到2000ml。",
        },
      ],
      progressColors: [
        { color: "#f56c6c", percentage: 20 },
        { color: "#e6a23c", percentage: 40 },
        { color: "#5cb87a", percentage: 60 },
        { color: "#1989fa", percentage: 80 },
        { color: "#6f7ad3", percentage: 100 },
      ],
      chartData: {
        heartRate: {
          times: [
            "00:00",
            "04:00",
            "08:00",
            "12:00",
            "16:00",
            "20:00",
            "24:00",
          ],
          values: [65, 62, 70, 85, 80, 75, 68],
        },
        bloodPressure: {
          times: [
            "00:00",
            "04:00",
            "08:00",
            "12:00",
            "16:00",
            "20:00",
            "24:00",
          ],
          systolic: [115, 110, 120, 135, 130, 125, 118],
          diastolic: [75, 72, 78, 90, 85, 82, 76],
        },
        bloodSugar: {
          times: [
            "00:00",
            "04:00",
            "08:00",
            "12:00",
            "16:00",
            "20:00",
            "24:00",
          ],
          values: [5.2, 5.0, 5.8, 6.5, 6.0, 5.7, 5.4],
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
        this.mockDataUpdate();
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

      this.mockDataUpdate();
      this.initCharts();
    },
    calculatePercentage(value, target) {
      return Math.min(Math.round((value / target) * 100), 100);
    },
    mockDataUpdate() {
      // 模拟数据更新
      this.healthData.calories = Math.floor(
        this.healthData.calories + Math.random() * 5
      );
      this.healthData.steps = Math.floor(
        this.healthData.steps + Math.random() * 50
      );
      this.healthData.activeMinutes = Math.min(
        30,
        Math.floor(this.healthData.activeMinutes + Math.random() * 2)
      );

      // 随机生成健康预警
      if (Math.random() > 0.8 && this.alerts.length < 3) {
        const alertTypes = [
          {
            type: "warning",
            color: "#E6A23C",
            icon: "el-icon-warning",
            message: "您的心率略高，建议放松休息",
            detail:
              "心率达到95bpm，高于您的平均水平。可能是由于运动或压力导致。",
          },
          {
            type: "info",
            color: "#909399",
            icon: "el-icon-info",
            message: "今日步数较少，建议增加活动",
            detail: "当前步数低于您的日均水平30%。适当增加活动有助于维持健康。",
          },
        ];

        const randomAlert =
          alertTypes[Math.floor(Math.random() * alertTypes.length)];
        randomAlert.time = new Date().toLocaleString();
        this.alerts.unshift(randomAlert);

        if (randomAlert.type === "warning") {
          this.$notify({
            title: "健康预警",
            message: randomAlert.message,
            type: "warning",
            duration: 5000,
          });
        }
      }

      // 更新图表数据
      this.updateChartData();
    },
    showAlertDetail(alert) {
      this.$alert(alert.detail, alert.message, {
        confirmButtonText: "确定",
        type: alert.type,
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
    updateChartData() {
      if (!this.collecting) return;

      // 更新心率数据
      const lastHeartRate =
        this.chartData.heartRate.values[
          this.chartData.heartRate.values.length - 1
        ];
      const newHeartRate = Math.max(
        55,
        Math.min(100, lastHeartRate + (Math.random() * 10 - 5))
      );
      this.chartData.heartRate.values.shift();
      this.chartData.heartRate.values.push(Math.round(newHeartRate));

      // 更新血压数据
      const lastSystolic =
        this.chartData.bloodPressure.systolic[
          this.chartData.bloodPressure.systolic.length - 1
        ];
      const newSystolic = Math.max(
        100,
        Math.min(140, lastSystolic + (Math.random() * 8 - 4))
      );
      this.chartData.bloodPressure.systolic.shift();
      this.chartData.bloodPressure.systolic.push(Math.round(newSystolic));

      const lastDiastolic =
        this.chartData.bloodPressure.diastolic[
          this.chartData.bloodPressure.diastolic.length - 1
        ];
      const newDiastolic = Math.max(
        65,
        Math.min(95, lastDiastolic + (Math.random() * 6 - 3))
      );
      this.chartData.bloodPressure.diastolic.shift();
      this.chartData.bloodPressure.diastolic.push(Math.round(newDiastolic));

      // 更新血糖数据
      const lastBloodSugar =
        this.chartData.bloodSugar.values[
          this.chartData.bloodSugar.values.length - 1
        ];
      const newBloodSugar = Math.max(
        4.5,
        Math.min(7.0, lastBloodSugar + (Math.random() * 0.4 - 0.2))
      );
      this.chartData.bloodSugar.values.shift();
      this.chartData.bloodSugar.values.push(
        parseFloat(newBloodSugar.toFixed(1))
      );

      this.initCharts();
    },
  },
  mounted() {
    this.loadUserList();
    this.initCharts();
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
