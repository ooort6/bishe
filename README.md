# 智能健康管理系统（Smart Health Manager）

## 项目介绍

智能健康管理系统是一个集成了健康数据监测、分析和管理功能的综合平台。系统包括健康数据采集、智能健康分析、健康预警以及健康建议等功能，旨在帮助用户全面了解自身健康状况并提供科学的健康管理方案。

主要功能包括：

- 健康数据实时监测与采集
- 智能健康指标分析（心率、血压、血糖、睡眠等）
- 健康异常预警和智能建议
- 健康数据可视化展示

## 系统架构

系统采用前后端分离架构：

- 前端：Vue2 + ElementUI + ECharts
- 后端：Spring Boot + MyBatis + MySQL
- 数据库：MySQL 8.0+

## 环境要求

### 前端开发环境

- Node.js v12.x 或更高版本
- npm v6.x 或更高版本或 yarn
- Vue CLI v3.x

### 后端开发环境

- JDK 1.8+
- Maven 3.5+
- MySQL 8.0+
- IDE：IntelliJ IDEA(推荐)或 Eclipse

### 运行环境

- 任何支持 Java 8 的系统（Windows/Linux/macOS）
- MySQL 8.0+
- Modern Web Browser (Chrome/Firefox/Safari/Edge)

## 安装与部署

### 数据库配置

1. 安装 MySQL 8.0+
2. 创建数据库：
   ```sql
   CREATE DATABASE healthy DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
   ```
3. 导入数据库表结构和初始数据：

   ```bash
   # 导入基础表结构
   mysql -u root -p healthy < smart_health_tables.sql

   # 导入测试数据
   mysql -u root -p healthy < complete_data_ascii.sql

   # 导入预警和建议数据
   mysql -u root -p healthy < alert_suggestion_data.sql
   ```

### 后端部署

1. 克隆代码仓库或解压项目文件：

   ```bash
   cd healthy-manager-backend
   ```

2. 修改数据库配置
   编辑 `src/main/resources/application.yml` 文件，根据你的环境配置数据库连接信息：

   ```yaml
   spring:
     datasource:
       username: root # 修改为你的数据库用户名
       password: your-password # 修改为你的数据库密码
       url: jdbc:mysql://localhost:3306/healthy?&serverTimezone=GMT%2B8&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull
   ```

3. 编译打包：

   ```bash
   mvn clean package -DskipTests
   ```

4. 运行应用：
   ```bash
   java -jar target/healthyManager-0.0.1-SNAPSHOT.jar
   ```
   或者使用 Maven 直接运行：
   ```bash
   mvn spring-boot:run
   ```

### 前端部署

1. 进入前端项目目录：

   ```bash
   cd healthy-manager-front
   ```

2. 安装依赖：

   ```bash
   npm install
   # 或者使用yarn
   yarn install
   ```

3. 开发模式运行：

   ```bash
   npm run serve
   # 或者使用yarn
   yarn serve
   ```

4. 生产环境构建：
   ```bash
   npm run build
   # 或者使用yarn
   yarn build
   ```
   构建完成后，将 `dist` 目录的内容部署到 Web 服务器（如 Nginx）即可。

## 使用说明

1. 访问系统

   - 开发环境：http://localhost:8080
   - 后端 API：http://localhost:8082

2. 登录系统

   - 默认管理员账号：admin
   - 默认密码：123456

3. 功能操作
   - 选择监测人员：在智能健康分析页面选择要监测的人员
   - 数据采集：点击"开始数据采集"按钮开始实时采集健康数据
   - 查看分析：系统会自动分析健康数据并生成图表和预警信息
   - 健康建议：系统根据健康数据自动生成个性化的健康建议

## 数据库初始化详解

项目包含多个 SQL 脚本文件，用于不同目的：

1. `smart_health_tables.sql` - 创建所有必要的表结构

   - 创建智能健康数据表 (smart_health_data)
   - 创建健康预警表 (smart_health_alert)
   - 创建健康建议表 (smart_health_suggestion)
   - 添加必要的菜单资源和权限

2. `complete_data_ascii.sql` - 导入完整的测试数据

   - 包含 24 小时健康数据记录
   - 包含一周的睡眠数据
   - 添加健康预警示例
   - 添加健康建议示例

3. `alert_suggestion_data.sql` - 特定的预警和建议数据
   - 添加更多健康预警数据
   - 添加更多健康建议数据

按照顺序依次执行这些脚本，确保数据库正确初始化：

```bash
# 执行顺序很重要
mysql -u username -p healthy < smart_health_tables.sql
mysql -u username -p healthy < complete_data_ascii.sql
mysql -u username -p healthy < alert_suggestion_data.sql
```

## 常见问题

1. 数据库连接失败

   - 检查数据库服务是否正常运行
   - 检查用户名和密码是否正确
   - 检查数据库名称是否正确
   - 确保 MySQL 支持 utf8mb4 字符集

2. 后端应用无法启动

   - 检查 JDK 版本是否为 1.8 或更高
   - 检查 Maven 依赖是否完整下载
   - 检查端口 8082 是否被占用
   - 检查 application.yml 配置是否正确

3. 前端无法连接后端 API

   - 检查后端服务是否正常运行
   - 检查 API 地址配置是否正确
   - 检查是否存在网络或防火墙限制
   - 检查浏览器控制台是否有 CORS 错误

4. 图表不显示或显示不正确
   - 确保 ECharts 库正确加载
   - 检查是否有数据返回
   - 尝试刷新页面或重新选择用户

## 开发与贡献

欢迎贡献代码或提出建议！

1. Fork 项目
2. 创建你的特性分支 (`git checkout -b feature/amazingFeature`)
3. 提交你的更改 (`git commit -m 'Add some amazingFeature'`)
4. 推送到分支 (`git push origin feature/amazingFeature`)
5. 提交合并请求

## 许可证

该项目采用 MIT License。
