# 快速启动指南

## 方式一：使用 IDE（推荐，最简单）

### IntelliJ IDEA / Eclipse
1. 打开 IDE
2. 选择 File → Open，选择本项目文件夹
3. 等待 IDE 下载依赖（右下角进度条）
4. 找到 `src/main/java/com/meeting/MeetingApplication.java`
5. 右键 → Run 'MeetingApplication'

---

## 方式二：安装 Maven（需要网络）

1. 下载 Maven：https://maven.apache.org/download.cgi
2. 解压到 `C:\Program Files\Apache\maven`
3. 配置环境变量：
   - `MAVEN_HOME=C:\Program Files\Apache\maven`
   - `PATH=%PATH%;%MAVEN_HOME%\bin`
4. 重启命令行，然后运行：
   ```bash
   mvn spring-boot:run
   ```

---

## 方式三：使用在线构建（可选）

如果有 GitHub，可推送项目后使用 GitHub Actions 构建。

---

## 数据库连接配置

在启动项目前，先修改 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    password: 你的MySQL密码  # 修改这里
```

然后执行 `sql/init.sql` 初始化数据库。

---

## 启动成功后

访问 API 文档：http://localhost:8080/swagger-ui.html
