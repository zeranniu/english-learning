# 小学英语学习小程序

基于 unibest 框架开发的英语学习小程序，包含前端 Vue3 + 后端 Spring Boot 3 完整实现。

## 项目概述

这是一个面向小学生的英语学习小程序，提供单词学习、听力训练、阅读理解、语法练习、PK 挑战、成长中心等功能模块。前端使用 unibest + Vue3 + TypeScript + UnoCSS + wot-ui，后端使用 Spring Boot 3 + MyBatis-Flex + PostgreSQL + JWT 认证。

## 功能特性

### 学习模块

| 模块 | 功能 | API 路径 |
|------|------|----------|
| 单词学习 | 卡片式单词学习，支持发音、认识/不认识标记 | `/api/vocab/words`, `/api/vocab/learn` |
| 听力训练 | 音频播放、倍速控制、听力选择题 | `/api/listening/lessons`, `/api/listening/answer` |
| 阅读理解 | 短文阅读、生词表、理解题 | `/api/reading/passages`, `/api/reading/answer` |
| 语法练习 | 选择填空、解析说明 | `/api/grammar/questions`, `/api/grammar/answer` |
| PK 挑战 | 排行榜、PK 对战 | `/api/pk/ranking`, `/api/pk/start` |
| 成长中心 | 等级系统、经验值、成就徽章 | `/api/growth/info` |
| 错题本 | 按分类查看错题记录 | `/api/mistakes/list` |

### 用户模块

| 功能 | 描述 | API 路径 |
|------|------|----------|
| 登录 | JWT 认证，单 token 模式 | `POST /api/auth/login` |
| 注册 | 用户注册 | `POST /api/auth/register` |
| 个人信息 | 查看/更新个人资料 | `GET/PUT /api/auth/profile` |
| 消息通知 | 系统通知列表 | `/api/notifications/list` |
| 学习提醒 | 每日学习提醒设置 | `/api/reminders/list` |
| 意见反馈 | 提交反馈 | `POST /api/feedback/submit` |

## 技术栈

### 前端

| 技术 | 版本/说明 |
|------|-----------|
| 框架 | unibest 4.4.1 (uni-app + Vue3) |
| 语言 | TypeScript 5.8 |
| UI 组件 | wot-design-uni (wot-ui) |
| 样式 | UnoCSS + 内联样式 |
| 状态管理 | Pinia 2.0 + persist 持久化 |
| HTTP 客户端 | uni.request 封装 |
| 图标 | wot-ui iconfont |
| 路由 | uni-app 路由 + 自定义 tabbar |

### 后端

| 技术 | 版本/说明 |
|------|-----------|
| 框架 | Spring Boot 3.2.5 |
| ORM | MyBatis-Flex 1.9.7 |
| 数据库 | PostgreSQL |
| 认证 | Spring Security + JWT (jjwt 0.12.5) |
| 构建 | Maven |
| JDK | 17+ |

## 项目结构

```
english-learning/
├── frontend/                    # 前端项目
│   ├── src/
│   │   ├── pages/               # 页面 (16个)
│   │   │   ├── index/index.vue  # 首页 (tabbar)
│   │   │   ├── study/study.vue  # 学习中心 (tabbar)
│   │   │   ├── growth/growth.vue # 成长中心 (tabbar)
│   │   │   ├── profile/profile.vue # 我的 (tabbar)
│   │   │   ├── login/index.vue  # 登录页 (首页)
│   │   │   ├── vocab/vocab.vue  # 单词学习
│   │   │   ├── listening/listening.vue # 听力训练
│   │   │   ├── reading/reading.vue # 阅读理解
│   │   │   ├── grammar/grammar.vue # 语法练习
│   │   │   ├── pk/pk.vue        # PK 挑战
│   │   │   ├── mistakes/mistakes.vue # 错题本
│   │   │   ├── ai-teacher/ai-teacher.vue # AI 老师
│   │   │   ├── notifications/notifications.vue # 消息通知
│   │   │   ├── reminders/reminders.vue # 学习提醒
│   │   │   ├── privacy/privacy.vue # 隐私设置
│   │   │   └── feedback/feedback.vue # 意见反馈
│   │   ├── api/                 # API 服务层
│   │   ├── config/              # 全局配置 (DATA_MODE 开关)
│   │   ├── http/                # HTTP 请求封装
│   │   ├── router/              # 路由守卫
│   │   ├── store/               # Pinia 状态管理
│   │   ├── tabbar/              # 自定义 tabbar
│   │   └── utils/               # 工具函数
│   ├── env/                     # 环境变量配置
│   ├── uno.config.ts            # UnoCSS 配置
│   └── pages.json               # 页面配置
│
├── backend/                     # 后端项目
│   ├── src/main/java/com/englishlearning/
│   │   ├── Application.java     # 启动类
│   │   ├── common/              # 公共类 (R, JwtUtil, 异常处理)
│   │   ├── config/              # 配置类 (Security, Cors, JwtFilter)
│   │   ├── controller/          # 控制器 (15个)
│   │   ├── dto/                 # 数据传输对象
│   │   ├── entity/              # 实体类 (16个)
│   │   ── mapper/              # MyBatis-Flex Mapper
│   └── src/main/resources/
│       ├── application.yml      # 应用配置
│       └── db/                  # 数据库脚本
│           ├── schema.sql       # 建表脚本 (15张表)
│           └── data.sql         # 初始数据
│
└── 原型.html                    # UI 原型设计
```

## 快速开始

### 环境要求

- Node.js 20+
- pnpm 9+
- JDK 17+
- Maven 3.8+
- PostgreSQL 14+

### 1. 数据库准备

```bash
# 创建数据库
createdb english_learning

# 数据库连接配置 (application.yml)
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/english_learning
    username: postgres
    password: postgres
```

后端启动时会自动执行 `schema.sql` 建表和 `data.sql` 插入初始数据。

### 2. 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端运行在 `http://localhost:8080/api`

**默认账号：**
- 用户名：`xiaoming`
- 密码：`123456`

### 3. 启动前端

```bash
cd frontend
pnpm install
pnpm dev:h5
```

前端运行在 `http://localhost:9000`

### 4. 数据模式切换

编辑 `frontend/src/config/index.ts`：

```typescript
export const APP_CONFIG = {
  DATA_MODE: 0 as 0 | 1,  // 0 = 静态模拟数据, 1 = 真实后端数据
}
```

- `DATA_MODE: 0` — 使用前端内置的静态模拟数据，无需后端
- `DATA_MODE: 1` — 调用后端真实 API，需先启动后端服务

## API 接口文档

### 认证接口

#### 登录
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "xiaoming",
  "password": "123456"
}

Response:
{
  "code": 0,
  "msg": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "expiresIn": 86400,
    "userId": 1,
    "nickname": "小明同学"
  }
}
```

#### 注册
```
POST /api/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "password": "password123",
  "nickname": "新用户"
}
```

### 学习接口

所有需要认证的接口需在请求头携带：
```
Authorization: Bearer <token>
```

#### 获取单词列表
```
GET /api/vocab/words
```

#### 学习单词
```
POST /api/vocab/learn
{
  "wordId": 1,
  "known": true
}
```

#### 获取听力课程
```
GET /api/listening/lessons
```

#### 获取阅读文章详情
```
GET /api/reading/passage/1
```

#### 获取语法题目
```
GET /api/grammar/questions
```

#### 获取 PK 排行榜
```
GET /api/pk/ranking
```

#### 获取成长信息
```
GET /api/growth/info
```

#### 获取错题列表
```
GET /api/mistakes/list?category=vocab
```

## 数据库设计

### 核心表结构

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| `users` | 用户表 | id, username, password, nickname, grade, level, exp, total_score |
| `vocab_word` | 单词表 | id, word, phonetic, translation, icon, icon_color |
| `user_vocab_progress` | 单词学习记录 | id, user_id, word_id, known, learned_at |
| `listening_lesson` | 听力课程 | id, title, duration, audio_url |
| `listening_question` | 听力题目 | id, lesson_id, question_text, option_a/b/c, correct_option |
| `reading_passage` | 阅读文章 | id, title, content, difficulty |
| `reading_vocab` | 阅读生词 | id, passage_id, word, meaning |
| `reading_question` | 阅读题目 | id, passage_id, question_text, option_a/b/c, correct_option |
| `grammar_question` | 语法题目 | id, question_text, option_a/b/c/d, correct_option, explanation |
| `mistake_record` | 错题记录 | id, user_id, category, title, error_info, tag |
| `pk_ranking` | PK 排行榜 | id, user_id, score |
| `notification` | 通知消息 | id, user_id, title, description, icon, is_read |
| `study_reminder` | 学习提醒 | id, user_id, label, reminder_time, enabled |
| `feedback` | 意见反馈 | id, user_id, feedback_type, content, contact |
| `daily_study_log` | 每日学习记录 | id, user_id, study_date, study_minutes, tasks_completed |
| `user_badge` | 用户徽章 | id, user_id, badge_name, unlocked |

## 安全设计

### JWT 认证流程

```
用户登录 → 后端验证 → 生成 JWT Token → 返回前端
                                            ↓
前端存储 Token (Pinia persist) → 后续请求携带 Authorization 头
                                            ↓
后端 JwtAuthFilter 验证 Token → 提取 userId → 设置 SecurityContext
```

### 安全配置

- **SecurityConfig**: `/auth/login`, `/auth/register` 放行，其他接口需认证
- **JwtAuthFilter**: 从 `Authorization: Bearer <token>` 提取用户信息
- **JwtUtil**: HS256 签名，24 小时有效期
- **密码加密**: BCrypt 加密存储

## 开发注意事项

### 前端

1. **图标使用**: 全部使用 wot-ui 的 `<wd-icon>` 组件，不使用 emoji 或 UnoCSS carbon 图标
2. **样式方案**: UnoCSS 原子类 + 内联样式，适配 uni-app 多端
3. **路由守卫**: 登录页设为首页，在 `onShow` 中检查登录状态
4. **数据模式**: 通过 `APP_CONFIG.DATA_MODE` 切换静态/真实数据

### 后端

1. **统一响应**: 所有接口返回 `R<T>` 格式 `{ code: 0, msg: "success", data: T }`
2. **异常处理**: `GlobalExceptionHandler` 统一处理业务异常和系统异常
3. **跨域配置**: `CorsConfig` 允许所有来源（开发环境）
4. **数据库初始化**: `spring.sql.init.mode=always` 自动执行 SQL 脚本

## 常见问题

### Q: 前端访问显示空白页

检查 `pages.json` 中登录页是否为 `"type": "home"`，确保登录页是第一个页面。

### Q: 登录后无法跳转首页

确保使用 `uni.switchTab` 跳转到 tabbar 页面，而非 `uni.reLaunch` 或 `uni.navigateTo`。

### Q: 后端接口返回 403

检查请求头是否携带 `Authorization: Bearer <token>`，token 是否过期。

### Q: 图标不显示

确保使用的是 wot-ui 支持的图标名称，参考 `wd-icon` 组件文档。

## 部署建议

### 生产环境

1. **后端**:
   - 修改 `application.yml` 中的数据库连接信息
   - 更换 JWT 密钥 (`jwt.secret`)
   - 关闭 SQL 自动初始化 (`spring.sql.init.mode=never`)
   - 使用 HTTPS

2. **前端**:
   - 修改 `.env.production` 中的 `VITE_SERVER_BASEURL`
   - 关闭代理 (`VITE_APP_PROXY_ENABLE=false`)
   - 执行 `pnpm build:h5` 生成生产包

### Docker 部署 (可选)

```dockerfile
# 后端 Dockerfile
FROM openjdk:17-jdk-slim
COPY target/english-learning-backend.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```dockerfile
# 前端 Dockerfile (Nginx)
FROM node:20-alpine AS build
WORKDIR /app
COPY . .
RUN pnpm install && pnpm build:h5

FROM nginx:alpine
COPY --from=build /dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
```

## 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 许可证

MIT License

## 联系方式

项目维护者 - [波子]

项目链接：[https://github.com/zeranniu/english-learning](https://github.com/zeranniu/english-learning)

---

**最后更新**: 2026-06-11
