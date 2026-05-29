# 使用 IntelliJ IDEA 启动项目（初学者专用）

## 准备工作

### 1. 确保已安装
- ✅ JDK 1.8+（已确认：您已安装）
- ✅ MySQL 5.5+（需要您确认）
- ✅ IntelliJ IDEA（免费版也可以）

---

## 步骤一：数据库初始化

### 1. 打开 MySQL 命令行
```bash
mysql -u root -p
```

### 2. 执行初始化脚本
将以下命令复制粘贴到 MySQL 命令行中：
```sql
source C:/Users/DK/Desktop/Meeting-Mangement/sql/init.sql
```

或者直接在 MySQL Workbench 中打开 `sql/init.sql` 文件并执行。

---

## 步骤二：打开项目

1. 打开 IntelliJ IDEA
2. 点击 **File** → **Open**
3. 选择文件夹：`C:\Users\DK\Desktop\Meeting-Mangement`
4. 点击 **OK**

![](https://example.com/open-project.png)

---

## 步骤三：配置数据库连接

1. 在 IDEA 左侧项目树中找到文件：
   ```
   src/main/resources/application.yml
   ```
2. 双击打开
3. 修改数据库密码：
   ```yaml
   spring:
     datasource:
       password: 你的MySQL密码  # ← 这里改成你的密码
   ```

**注意：** 如果你的 MySQL 用户名不是 root，还需要修改 `username` 字段。

---

## 步骤四：等待依赖下载

1. IDEA 右下角会显示进度条
2. 等待 "Maven: importing changes" 完成
3. 如果左下角有 "Maven needs to be imported"，点击 "Import Changes"

**第一次可能需要几分钟，请耐心等待！**

---

## 步骤五：运行项目

### 方法 A：右键菜单（最简单）
1. 在左侧项目树找到：
   ```
   src/main/java/com/meeting/MeetingApplication.java
   ```
2. 右键点击该文件
3. 选择 **Run 'MeetingApplication'**

### 方法 B：直接点击运行按钮
1. 打开 `MeetingApplication.java` 文件
2. 查看类定义旁边的绿色小箭头 ▶️
3. 点击箭头，选择 **Run**

---

## 步骤六：验证启动成功

### 查看控制台输出
当看到类似以下输出时，说明启动成功：
```
Started MeetingApplication in 5.234 seconds
```

### 访问 API 文档
打开浏览器，访问：
```
http://localhost:8080/swagger-ui.html
```

---

## 常见问题

### Q1: 启动报错 "Communications link failure"
- 检查 MySQL 服务是否已启动
- 检查 `application.yml` 中的数据库地址、用户名、密码

### Q2: 提示 "Access denied for user"
- 检查用户名密码是否正确
- 确认该用户有权访问 `meeting_db` 数据库

### Q3: Maven 依赖下载很慢
- 可配置阿里云镜像（参考网上教程）
- 或者耐心等待（第一次较慢）

### Q4: 想重启项目
- 点击控制台上方的红色方块 ■ 停止
- 再点击绿色箭头 ▶️ 重新启动

---

## 下一步

启动成功后，打开 [DEMO_GUIDE.md](DEMO_GUIDE.md) 查看功能演示！
