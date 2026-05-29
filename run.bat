@echo off
REM Maven Wrapper 启动脚本
echo.
echo =======================================================
echo  正在启动 Maven Wrapper...
echo =======================================================
echo.

REM 检查是否有 Java
if "%JAVA_HOME%" == "" (
    echo [警告] JAVA_HOME 未设置，尝试使用系统 PATH 中的 Java...
)

REM 尝试使用 mvnw（如果已下载）
if exist ".mvnw\mvnw.cmd" (
    echo [信息] 使用已存在的 Maven Wrapper...
    call .mvnw\mvnw.cmd %*
    goto end
)

echo.
echo =======================================================
echo  Maven 未安装！
echo =======================================================
echo.
echo  请选择以下方式之一运行项目：
echo.
echo  1. 安装 Maven：https://maven.apache.org/download.cgi
echo  2. 使用 IDE（推荐）：IntelliJ IDEA 或 Eclipse
echo  3. 手动编译（不推荐）
echo.
echo  快捷方式：直接在 IntelliJ IDEA 中打开此项目，
echo           然后右键运行 MeetingApplication.java
echo.
:end
