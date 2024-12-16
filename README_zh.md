# 校园二手交易平台后端项目

<div align="center">

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MyBatis](https://img.shields.io/badge/MyBatis-Latest-blue.svg)](https://mybatis.org)
[![JWT](https://img.shields.io/badge/JWT-Latest-yellow.svg)](https://jwt.io)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](./LICENSE)

[English](./README.md) | [简体中文](./README_zh.md)

</div>

## 📝 目录
- [项目概述](#项目概述)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [数据库设计](#数据库设计)
- [接口文档](#接口文档)
- [快速开始](#快速开始)
- [注意事项](#注意事项)

## 🎯 项目概述 <a name = "项目概述"></a>
本项目是一个基于Spring Boot的校园二手交易平台后端系统，为校园内的二手商品交易提供支持。系统包含用户端、商家端和管理员端，支持商品浏览、购物车、订单管理、支付等功能。

## 🛠️ 技术栈 <a name = "技术栈"></a>

| 类别 | 技术 |
|------|------|
| 核心框架 | Spring Boot 2.7.3 |
| ORM框架 | MyBatis |
| 数据库连接池 | Druid |
| 分页插件 | PageHelper |
| 接口文档 | Knife4j |
| 安全认证 | JWT |
| 文件存储 | 阿里云OSS |
| 支付集成 | 微信支付 |
| 其他工具 | Lombok、Fastjson、Apache POI |

## 📁 项目结构 <a name = "项目结构"></a>
```
campus-used-trading/
├── campus-used-trading-common/  # 公共模块
├── campus-used-trading-pojo/    # 实体类模块
├── campus-used-trading-server/  # 主服务模块
├── pom.xml                      # Maven配置文件
├── second_trade.sql             # 数据库脚本
├── 接口文档.txt                  # 接口文档
└── 接口文档（不含评论）.json      # 接口文档（JSON格式）
```

## 💾 数据库设计 <a name = "数据库设计"></a>

<details>
<summary>点击展开数据库表结构</summary>

| 表名 | 描述 |
|------|------|
| marketer | 商家信息表 |
| category | 商品分类表 |
| thing | 商品信息表 |
| user | 用户信息表 |
| address_book | 用户地址表 |
| shopping_cart | 购物车表 |
| orders | 订单表 |
| order_detail | 订单详情表 |
| news | 新闻公告表 |
| remark | 评论表 |

</details>

## 📚 接口文档 <a name = "接口文档"></a>

### 主要接口

| 模块 | 接口路径 | 功能描述 |
|------|----------|----------|
| 管理端 | `/admin` | 订单管理、商品管理、商家管理、分类管理 |
| 用户端 | `/user` | 用户登录/注册、商品浏览、购物车管理、订单管理、地址管理 |
| 商家端 | `/marketer` | 商品管理、订单管理、数据统计 |

详细接口说明请参考项目中的接口文档文件。

## 🚀 快速开始 <a name = "快速开始"></a>

1. 导入数据库：
```bash
mysql -u username -p database_name < second_trade.sql
```

2. 配置数据库连接：修改`application-dev.yml`中的数据库配置
3. 启动项目：运行`SkyApplication.java`
4. 访问接口文档：http://localhost:8080/doc.html

## 📌 注意事项 <a name = "注意事项"></a>

- 微信支付和阿里云OSS需要配置相关参数
- 生产环境请修改`application.yml`中的安全配置
- 建议使用Nginx进行反向代理和负载均衡

## 📄 开源协议

本项目采用 MIT 协议 - 查看 [LICENSE](./LICENSE) 文件了解详情

## 🤝 贡献

欢迎提交问题和功能需求，也欢迎提交代码贡献！