# 电商购物平台

基于 Spring Boot + Vue 3 的全栈电商系统。

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Spring Boot 3, MyBatis-Plus, MySQL |
| 前端 | Vite, Vue 3 (Composition API), Pinia, Vue Router, Axios |
| 测试 | JUnit 5, Mockito, Playwright |
| 文档 | SpringDoc OpenAPI (Swagger) |

## 项目结构

```
├── backend/          # Spring Boot 后端
│   ├── src/main/java/com/example/ecommerce/
│   │   ├── controller/    # 控制器层
│   │   ├── service/       # 服务层
│   │   ├── repository/    # 数据访问层
│   │   ├── domain/        # 领域模型
│   │   ├── dto/           # 数据传输对象
│   │   ├── config/        # 配置类
│   │   ├── common/        # 通用类（Result、ResultCode）
│   │   └── exception/     # 异常处理
│   └── src/test/          # 测试代码
├── frontend/         # Vue 3 前端
│   └── src/
│       ├── components/    # 组件（PromoLabel、Countdown、ProductDetail、ProductList）
│       ├── views/         # 页面视图（ShoppingCart、OrderSuccess、NotFound）
│       ├── router/        # 路由配置
│       ├── stores/        # Pinia 状态管理（购物车）
│       └── utils/         # 工具函数（Axios 封装）
└── harness-collab/   # 项目文档
    ├── 01-product-specs/  # 需求规格文档
    ├── 02-design-docs/    # 技术设计文档
    └── 04-api-docs/       # API 文档
```

## 快速启动

### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8.0+

### 后端
```bash
cd backend
# 创建数据库
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS ecommerce DEFAULT CHARACTER SET utf8mb4;"

# 修改 src/main/resources/application.yml 中的数据库账号密码

mvn spring-boot:run
# 启动于 http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```

### 前端
```bash
cd frontend
npm install
npm run dev
# 启动于 http://localhost:5173
```

### 测试
```bash
# 后端测试
cd backend
mvn clean verify -Pharness-new

# 前端测试
cd frontend
npx playwright test
```
