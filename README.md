# Campus Second-hand Trading Platform Backend

<div align="center">

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MyBatis](https://img.shields.io/badge/MyBatis-Latest-blue.svg)](https://mybatis.org)
[![JWT](https://img.shields.io/badge/JWT-Latest-yellow.svg)](https://jwt.io)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](./LICENSE)

[English](./README.md) | [简体中文](./README_zh.md)

</div>

## 📝 Table of Contents
- [About](#about)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Database Design](#database-design)
- [API Documentation](#api-documentation)
- [Quick Start](#quick-start)
- [Notes](#notes)

## 🎯 About <a name = "about"></a>
A Spring Boot-based campus second-hand trading platform backend system that supports second-hand goods trading within campus. The system includes user, merchant, and admin interfaces, supporting features like product browsing, shopping cart, order management, and payment.

## 🛠️ Tech Stack <a name = "tech-stack"></a>

| Category | Technology |
|----------|------------|
| Core Framework | Spring Boot 2.7.3 |
| ORM Framework | MyBatis |
| Connection Pool | Druid |
| Pagination | PageHelper |
| API Documentation | Knife4j |
| Authentication | JWT |
| File Storage | Aliyun OSS |
| Payment | WeChat Pay |
| Utils | Lombok, Fastjson, Apache POI |

## 📁 Project Structure <a name = "project-structure"></a>
```
campus-used-trading/
├── campus-used-trading-common/  # Common module
├── campus-used-trading-pojo/    # Entity classes
├── campus-used-trading-server/  # Main service
├── pom.xml                      # Maven configuration
├── second_trade.sql             # Database script
├── api-doc.txt                  # API documentation
└── api-doc-no-comments.json     # API documentation (JSON)
```

## 💾 Database Design <a name = "database-design"></a>

<details>
<summary>Click to expand database tables</summary>

| Table Name | Description |
|------------|-------------|
| marketer | Merchant information |
| category | Product categories |
| thing | Product information |
| user | User information |
| address_book | User addresses |
| shopping_cart | Shopping cart |
| orders | Orders |
| order_detail | Order details |
| news | News and announcements |
| remark | Comments |

</details>

## 📚 API Documentation <a name = "api-documentation"></a>

### Main Endpoints

| Module | Endpoint | Description |
|--------|----------|-------------|
| Admin | `/admin` | Order, product, merchant, and category management |
| User | `/user` | Login/register, browsing, cart, orders, addresses |
| Merchant | `/marketer` | Product management, order management, statistics |

Detailed API documentation can be found in the project's documentation files.

## 🚀 Quick Start <a name = "quick-start"></a>

1. Import database:
```bash
mysql -u username -p database_name < second_trade.sql
```

2. Configure database connection in `application-dev.yml`
3. Run `SkyApplication.java`
4. Access API documentation at `http://localhost:8080/doc.html`

## 📌 Notes <a name = "notes"></a>

- WeChat Pay and Aliyun OSS parameters need to be configured
- Modify security settings in `application.yml` for production
- Nginx is recommended for reverse proxy and load balancing

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.

## 🤝 Contributing

Contributions, issues and feature requests are welcome! 