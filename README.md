# 学生选课系统 SCSS

一个基于 Spring Boot 3 + MySQL 8 + Vue 3 + Element Plus 构建的学生选课系统。

项目覆盖学生、教师、管理员三类角色，包含课程管理、选课退课、授课查看、用户管理、JWT 鉴权与前后端分离界面，适合作为课程设计、毕业设计或 Spring Boot + Vue 全栈练手项目。

## 项目介绍

本项目围绕高校选课业务的核心流程设计：

- 学生可以注册账号、浏览课程、搜索课程、选课、退课、查看已选课程
- 教师可以查看自己的授课课程以及对应的选课学生名单
- 管理员可以维护用户、课程，并完成教师分配与课程容量控制
- 后端使用 JWT 做登录鉴权，前端根据角色动态显示菜单与页面能力

除了基础业务功能，项目还实现了较完整的前后端工程结构，便于继续扩展成绩管理、排课、公告通知、课表展示等模块。

## 功能亮点

- 三角色权限体系：管理员、教师、学生
- JWT 登录认证与接口鉴权
- 课程容量限制与重复选课校验
- 管理员课程管理、用户管理
- 教师授课视图与学生名单查看
- 学生选课、退课、我的课程展示
- Vue 3 + Element Plus 前端界面
- 前后端分离，Vite 代理 `/api` 请求

## 技术栈

### 后端

- Java 21
- Spring Boot 3.2.5
- Spring Web
- Spring Data JPA
- Spring Security
- JWT (`jjwt`)
- MySQL 8
- Maven

### 前端

- Vue 3
- Vite
- Vue Router 4
- Pinia
- Axios
- Element Plus

## 项目结构

```text
scss
├─ src/main/java/com/example/scss
│  ├─ config        # 安全配置、异常处理、初始化数据
│  ├─ controller    # 接口层
│  ├─ dto           # 请求/响应对象
│  ├─ entity        # JPA 实体
│  ├─ repository    # 数据访问层
│  ├─ security      # JWT 过滤器与工具类
│  └─ service       # 业务逻辑层
├─ src/main/resources
│  ├─ application.yaml
│  └─ schema.sql
├─ frontend
│  ├─ src/router    # 路由配置
│  ├─ src/stores    # Pinia 状态管理
│  ├─ src/utils     # Axios 封装
│  └─ src/views     # 页面视图
└─ pom.xml
```

## 运行环境

- JDK 21
- Maven 3.9+（也可直接使用 Maven Wrapper）
- Node.js 18+
- MySQL 8

## 快速开始

### 1. 创建数据库

```sql
CREATE DATABASE IF NOT EXISTS scss DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 配置数据库与 JWT

后端配置文件位于 [src/main/resources/application.yaml](src/main/resources/application.yaml)。

项目已改为环境变量优先，默认值如下：

- `DB_URL=jdbc:mysql://localhost:3306/scss?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true`
- `DB_USERNAME=root`
- `DB_PASSWORD=123456`
- `JWT_SECRET=ChangeThisJwtSecretBeforeProductionUse1234567890`
- `JWT_EXPIRATION=86400000`

### 3. 启动后端

在项目根目录执行：

```powershell
cd c:\Users\Administrator\IdeaProjects\scss
.\mvnw.cmd spring-boot:run
```

后端默认地址：`http://localhost:8080`

### 4. 启动前端

在前端目录执行：

```powershell
cd c:\Users\Administrator\IdeaProjects\scss\frontend
npm install
npm run dev
```

前端默认地址：`http://localhost:5173`

## 默认测试账号

系统启动后会自动初始化以下账号：

| 角色 | 用户名 | 密码 |
| --- | --- | --- |
| 管理员 | admin | admin123 |
| 教师 | teacher1 | teacher123 |
| 学生 | student1 | student123 |

## 主要接口

### 认证接口

- `POST /api/auth/login`：登录
- `POST /api/auth/register`：注册

### 课程接口

- `GET /api/courses`：获取课程列表
- `GET /api/courses/search?name=xxx`：按名称搜索课程
- `POST /api/courses`：管理员新增课程
- `PUT /api/courses/{id}`：管理员修改课程
- `DELETE /api/courses/{id}`：管理员删除课程

### 选课接口

- `POST /api/selections/{courseId}`：学生选课
- `DELETE /api/selections/{courseId}`：学生退课
- `GET /api/selections/my`：查看我的选课

### 管理接口

- `GET /api/admin/users`：管理员查看全部用户
- `POST /api/admin/users`：管理员新增用户
- `PUT /api/admin/users/{id}`：管理员修改用户
- `DELETE /api/admin/users/{id}`：管理员删除用户

## 前端页面说明

- 登录/注册页
- 仪表盘首页
- 课程列表页
- 我的选课页
- 教师授课页
- 管理员课程管理页
- 管理员用户管理页

## 适合继续扩展的方向

- 学生成绩管理
- 教室与时间冲突检测
- 公告通知模块
- 课表可视化展示
- 文件上传与头像管理
- Docker 部署与 CI/CD

## 说明

- 当前仓库适合本地开发、课程设计和演示用途
- 若用于生产环境，请务必修改数据库密码、JWT 密钥、CORS 配置与部署参数

## License

仅供学习与交流使用。