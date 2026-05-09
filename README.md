# Eleven 的摄影笔记

> 用光影写故事，用构图说心情 —— 一个关于摄影学习与分享的个人博客

## 技术栈

| 层 | 技术 |
|---|---|
| 前端 | Vue 3 + TypeScript + Vite 8 + Tailwind CSS 4 |
| 状态管理 | Pinia |
| 路由 | Vue Router 4 |
| Markdown 渲染 | markdown-it + highlight.js |
| 后端 | Spring Boot 3.2 + MyBatis-Plus 3.5 |
| 安全 | Spring Security + JWT |
| 数据库 | MySQL 8 + Redis |

## 功能特性

- **博客首页** — 精选文章、分类导航、读者留言展示
- **文章系统** — Markdown 编写，代码高亮，分类标签
- **作品集** — 摄影作品画廊，Lightbox 大图浏览
- **文章归档** — 按时间线浏览所有文章
- **评论系统** — 读者留言，嵌套回复，管理员审核
- **管理后台** — 文章 CRUD、评论审核、分类标签管理
- **主题切换** — 亮色/暗色漫画风格主题
- **音乐播放器** — 磁带风格的小型音乐播放组件

## 快速开始

### 环境要求

- Node.js 18+
- Java 17+
- MySQL 8.0+
- Redis 7+
- Maven（或使用项目自带的 Maven Wrapper）

### 1. 数据库初始化

```sql
CREATE DATABASE blog DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

启动 MySQL 和 Redis 后，项目会在首次启动时自动建表。

### 2. 启动后端

```bash
cd blog-backend
./mvnw spring-boot:run      # Linux / macOS
mvnw.cmd spring-boot:run     # Windows
# 或直接运行 start-backend.bat
```

后端默认运行在 `http://localhost:8080`。

修改 `application.yml` 中的数据库连接信息和 JWT 密钥：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/blog?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: 你的密码

jwt:
  secret: 你的JWT密钥  # 生产环境务必修改
```

### 3. 启动前端

```bash
cd blog-frontend
npm install
npm run dev
# 或直接运行 start-frontend.bat
```

前端运行在 `http://localhost:5173`，API 请求自动代理至后端。

### 4. 生产构建

```bash
cd blog-frontend
npm run build     # 构建到 dist/ 目录
npm run preview   # 预览构建结果
```

将 `dist/` 目录部署到 Nginx 即可。项目根目录已提供 `nginx.conf` 配置参考。

## 项目结构

```
eleven-photo-blog/
├── blog-frontend/          # Vue 3 前端
│   ├── src/
│   │   ├── api/            # API 请求封装
│   │   ├── components/     # 可复用组件
│   │   │   ├── comment/    # 评论组件
│   │   │   ├── layout/     # 布局组件（导航、页脚）
│   │   │   ├── music/      # 音乐播放器
│   │   │   ├── post/       # 文章组件
│   │   │   └── ui/         # 通用 UI 组件
│   │   ├── router/         # 路由配置
│   │   ├── stores/         # Pinia 状态管理
│   │   ├── views/          # 页面视图
│   │   │   └── admin/      # 管理后台页面
│   │   └── style.css       # 全局样式（漫画主题）
│   └── public/
│       ├── img/            # 网站图片资源
│       └── YouName/        # 作品集照片（15张）
├── blog-backend/           # Spring Boot 后端
│   └── src/main/java/com/blog/
│       ├── controller/     # REST 控制器
│       ├── service/        # 业务逻辑层
│       ├── mapper/         # MyBatis 数据访问层
│       ├── entity/         # 数据库实体
│       ├── dto/            # 数据传输对象
│       ├── config/         # 安全、跨域等配置
│       └── interceptor/    # JWT 拦截器
├── nginx.conf              # Nginx 配置参考
├── start-backend.bat       # 后端启动脚本
└── start-frontend.bat      # 前端启动脚本
```

## 管理后台

访问 `/admin/login` 进入管理后台：

- 默认账号：`admin`
- 默认密码：`admin123`（可在 `application.yml` 中修改）

功能：
- **仪表盘** — 文章、评论统计概览
- **文章管理** — 新建/编辑/删除文章，Markdown 编辑器
- **评论管理** — 审核通过/拒绝/删除评论
- **分类管理** — 创建与编辑文章分类
- **标签管理** — 创建与编辑文章标签

## 页面路由

| 路径 | 说明 |
|---|---|
| `/` | 首页 |
| `/post/:slug` | 文章详情 |
| `/archive` | 文章归档 |
| `/portfolio` | 摄影作品集 |
| `/category/:slug` | 分类文章列表 |
| `/tag/:slug` | 标签文章列表 |
| `/about` | 关于本站 |
| `/admin/login` | 管理后台登录 |
| `/admin` | 管理仪表盘 |

## 许可

MIT License

---

📷 每一张好照片，都是按下快门时的心跳。
