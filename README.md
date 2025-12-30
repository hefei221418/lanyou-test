# Spring Boot 3.1 + MyBatis + PostgreSQL Demo Project

这是一个基于Spring Boot 3.1、MyBatis和PostgreSQL的基础项目模板，包含了完整的CRUD操作示例。

## 项目特性

- Spring Boot 3.1.0
- Java 11
- MyBatis 3.0.2
- PostgreSQL数据库
- Lombok简化代码
- RESTful API
- 完整的CRUD操作

## 项目结构

```
springboot-mybatis-demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── controller/     # REST控制器
│   │   │   ├── entity/         # 实体类
│   │   │   ├── mapper/         # MyBatis Mapper接口
│   │   │   ├── service/        # 业务逻辑层
│   │   │   └── DemoApplication.java  # 主启动类
│   │   └── resources/
│   │       ├── mapper/         # MyBatis XML映射文件
│   │       ├── db/             # 数据库初始化脚本
│   │       └── application.yml  # 配置文件
│   └── test/                   # 测试类
├── pom.xml                     # Maven配置文件
└── README.md                   # 项目说明文档
```

## API端点

### 用户管理API

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/users` | 获取所有用户 |
| GET | `/api/users/get` | 根据ID获取用户 |
| GET | `/api/users/getByEmail` | 根据邮箱获取用户 |
| GET | `/api/users/search` | 根据姓名搜索用户 |
| POST | `/api/users/create` | 创建新用户 |
| POST | `/api/users/update` | 更新用户信息 |
| POST | `/api/users/delete` | 删除用户 |

### 算法API

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/algorithms/binarySearch` | 二分查找算法 |
| GET | `/api/algorithms/quickSort` | 快速排序算法 |
| GET | `/api/algorithms/bubbleSort` | 冒泡排序算法 |
| GET | `/api/algorithms/fibonacci` | 斐波那契数列 |
| GET | `/api/algorithms/primeNumbers` | 素数生成 |
| GET | `/api/algorithms/factorial` | 阶乘计算 |

## 环境要求

- Java 11+
- PostgreSQL 12+
- Maven 3.6+

## 数据库配置

1. 安装PostgreSQL数据库
2. 创建数据库：
```sql
CREATE DATABASE demo_db;
```

3. 修改application.yml中的数据库连接配置（如果需要）：
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/demo_db
    username: postgres
    password: postgres
```

## 快速开始

### 1. 运行项目

```bash
mvn spring-boot:run
```

### 2. 测试API

使用curl或Postman测试API：

```bash
# 创建用户
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"张三","email":"zhangsan@example.com"}'

# 获取所有用户
curl http://localhost:8080/api/users

# 根据ID获取用户
curl "http://localhost:8080/api/users/get?id=1"

# 更新用户
curl -X POST http://localhost:8080/api/users/update?id=1 \
  -H "Content-Type: application/json" \
  -d '{"name":"李四","email":"lisi@example.com"}'

# 删除用户
curl -X POST http://localhost:8080/api/users/delete?id=1
```

### 3. 测试算法API

```bash
# 二分查找
curl "http://localhost:8080/api/algorithms/binarySearch?array=1,3,5,7,9,11&target=7"

# 快速排序
curl "http://localhost:8080/api/algorithms/quickSort?array=64,34,25,12,22,11,90"

# 冒泡排序
curl "http://localhost:8080/api/algorithms/bubbleSort?array=64,34,25,12,22,11,90"

# 斐波那契数列
curl "http://localhost:8080/api/algorithms/fibonacci?n=10"

# 素数生成
curl "http://localhost:8080/api/algorithms/primeNumbers?limit=30"

# 阶乘计算
curl "http://localhost:8080/api/algorithms/factorial?n=5"

# 根据姓名搜索用户
curl "http://localhost:8080/api/users/search?name=张"
```

## 配置说明

### 应用配置

- 端口：8080
- 应用名称：springboot-mybatis-demo

### 数据库配置

- 数据库：PostgreSQL
- 驱动：org.postgresql.Driver
- 自动执行初始化脚本：schema.sql

### MyBatis配置

- 映射文件位置：classpath:mapper/*.xml
- 实体类别名包：com.example.demo.entity
- 开启驼峰命名转换：map-underscore-to-camel-case: true
- SQL日志输出：开启

### 日志配置

- 根日志级别：INFO
- 应用日志级别：DEBUG
- MyBatis SQL日志：DEBUG

## 技术栈

- **Spring Boot 3.1.0** - 应用框架
- **MyBatis 3.0.2** - 数据持久化框架
- **PostgreSQL** - 关系型数据库
- **Lombok** - 代码简化
- **Maven** - 构建工具
- **JUnit 5** - 测试框架

## 数据库表结构

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 开发建议

1. 生产环境请使用连接池（如HikariCP）优化数据库连接
2. 添加全局异常处理
3. 实现数据验证（如Hibernate Validator）
4. 添加API文档（如Swagger）
5. 实现安全认证（如Spring Security）
6. 考虑添加缓存（如Redis）提高性能
7. 实现分页查询功能
8. 添加单元测试和集成测试

## 注意事项

- 确保PostgreSQL服务已启动并可连接
- 数据库连接信息在application.yml中配置
- 项目启动时会自动执行schema.sql创建表结构
- MyBatis的SQL语句可以在XML文件中查看和修改

## 许可证

MIT License