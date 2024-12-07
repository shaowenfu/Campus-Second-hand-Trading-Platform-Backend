## 数据库设计文档

| 序号 | 数据表名      | 中文名称       |
| ---- | ------------- | -------------- |
| 1    | marketer     | 商家（管理员）表|
| 2    | category      | 分类表         |
| 3    | thing         | 物品表         |
| 4    | user          | 用户表         |
| 5    | address_book  | 地址表         |
| 6    | shopping_cart | 购物车表       |
| 7    | orders        | 订单表         |
| 8    | order_detail  | 订单明细表     |
| 9    | news          | 消息            |

### 1. marketer

marketer表为商家（管理员）表，用于存储商家（管理员）信息。具体表结构如下：

| 字段名      | 数据类型    | 说明         | 备注        |
| ----------- | ----------- | ------------ | ----------- |
| id          | bigint      | 主键         | 自增        |
| name        | varchar(32) | 姓名         |             |
| username    | varchar(32) | 用户名       | 唯一        |
| password    | varchar(64) | 密码         |             |
| phone       | varchar(11) | 手机号       |             |
| id_number   | varchar(18) | 身份证号    |             |
| status      | int         | 权限状态     | 1管理员 0普通商家 |
| create_time | datetime    | 创建时间     |             |
| update_time | datetime    | 最后修改时间 |             |

### 2. category

category表为分类表，用于存储商品的分类信息。具体表结构如下：

| 字段名      | 数据类型    | 说明         | 备注                 |
| ----------- | ----------- | ------------ | -------------------- |
| id          | bigint      | 主键         | 自增                 |
| name        | varchar(32) | 分类名称     | 唯一                 |
| sort        | int         | 排序字段     | 用于分类数据的排序   |
| status      | int         | 状态         | 1启用 0禁用          |
| create_time | datetime    | 创建时间     |                      |
| update_time | datetime    | 最后修改时间 |                      |
| create_user | bigint      | 创建人id     |                      |
| update_user | bigint      | 最后修改人id |                      |

### 3. thing

dish表为物品表，用于存储物品的信息。具体表结构如下：

| 字段名      | 数据类型      | 说明         | 备注        |
| ----------- | ------------- | ------------ | ----------- |
| id          | bigint        | 主键         | 自增        |
| name        | varchar(32)   | 菜品名称     | 唯一        |
| category_id | bigint        | 分类id       | 逻辑外键    |
| price       | decimal(10,2) | 菜品价格     |             |
| amount      | bigint        | 余量         |             |
| image       | varchar(255)  | 图片路径     |             |
| description | varchar(255)  | 物品描述     |             |
| status      | int           | 售卖状态     | 1起售 0停售 |
| create_time | datetime      | 创建时间     |             |
| update_time | datetime      | 最后修改时间 |             |
| create_user | bigint        | 创建人id     |             |
| update_user | bigint        | 最后修改人id |             |

### 4. user

user表为用户表，用于存储C端用户的信息。具体表结构如下：

| 字段名      | 数据类型     | 说明               | 备注 |
| ----------- | ------------ | ------------------ | ---- |
| id          | bigint       | 主键               | 自增 |
| openid      | varchar(45)  | 微信用户的唯一标识 |      |
| name        | varchar(32)  | 用户姓名           |      |
| phone       | varchar(11)  | 手机号             |      |
| sex         | varchar(2)   | 性别               |      |
| id_number   | varchar(18)  | 身份证号           |      |
| avatar      | varchar(500) | 微信用户头像路径   |      |
| create_time | datetime     | 注册时间           |      |

### 5. address_book

address_book表为地址表，用于存储C端用户的收货地址信息。具体表结构如下：

| 字段名        | 数据类型     | 说明         | 备注           |
| ------------- | ------------ | ------------ | -------------- |
| id            | bigint       | 主键         | 自增           |
| user_id       | bigint       | 用户id       | 逻辑外键       |
| consignee     | varchar(50)  | 收货人       |                |
| sex           | varchar(2)   | 性别         |                |
| phone         | varchar(11)  | 手机号       |                |
| area          | varchar(12)  | 园区         |                |
| dormitories_id| varchar(32)  | 宿舍         |                |
| unit number   | varchar(32)  | 单元号       |                |
| door_code     | varchar(12)  | 门牌号       |                |
| is_default    | tinyint(1)   | 是否默认地址 | 1是 0否        |

### 6. shopping_cart

shopping_cart表为购物车表，用于存储C端用户的购物车信息。具体表结构如下：

| 字段名      | 数据类型      | 说明         | 备注     |
| ----------- | ------------- | ------------ | -------- |
| id          | bigint        | 主键         | 自增     |
| name        | varchar(32)   | 商品名称     |          |
| image       | varchar(255)  | 商品图片路径 |          |
| user_id     | bigint        | 用户id       | 逻辑外键 |
| thing_id    | bigint        | 物品id       | 逻辑外键 |
| number      | int           | 商品数量     |          |
| amount      | decimal(10,2) | 商品单价     |          |
| create_time | datetime      | 创建时间     |          |

### 7. orders

orders表为订单表，用于存储C端用户的订单数据。具体表结构如下：

| 字段名                  | 数据类型      | 说明         | 备注                                 |
| ----------------------- | ------------- | ------------ | ------------------------------------ |
| id                      | bigint        | 主键         | 自增                                 |
| number                  | varchar(50)   | 订单号       |                                      |
| status                  | int           | 订单状态     | 1待付款 2已付款 3交易中 4已完成 5已取消 |
| user_id                 | bigint        | 用户id       | 逻辑外键                              |
| address_book_id         | bigint        | 地址id       | 逻辑外键                              |
| order_time              | datetime      | 下单时间     |                                      |
| checkout_time           | datetime      | 付款时间     |                                      |
| pay_method              | int           | 支付方式     | 1微信支付 2支付宝支付                  |
| pay_status              | tinyint       | 支付状态     | 0未支付 1已支付 2退款                  |
| amount                  | decimal(10,2) | 订单金额     |                                      |
| remark                  | varchar(100)  | 备注信息     |                                      |
| phone                   | varchar(11)   | 手机号       |                                      |
| address                 | varchar(255)  | 详细地址信息 |                                      |
| user_name               | varchar(32)   | 用户姓名     |                                      |
| consignee               | varchar(32)   | 收货人       |                                      |
| cancel_reason           | varchar(255)  | 订单取消原因 |                                      |
| cancel_time             | datetime      | 订单取消时间 |                                      |

### 8. order_detail

order_detail表为订单明细表，用于存储C端用户的订单明细数据。具体表结构如下：

| 字段名      | 数据类型      | 说明         | 备注     |
| ----------- | ------------- | ------------ | -------- |
| id          | bigint        | 主键         | 自增     |
| name        | varchar(32)   | 商品名称     |          |
| image       | varchar(255)  | 商品图片路径 |          |
| order_id    | bigint        | 订单id       | 逻辑外键 |
| thing_id    | bigint        | 物品id       | 逻辑外键 |
| number      | int           | 商品数量     |          |
| amount      | decimal(10,2) | 商品单价     |          |

### 9. news
news表为新闻展示，用于存储C端的需要轮播的消息。具体表结构如下：

| 字段名      | 数据类型      | 说明         | 备注              |
| ----------- | ------------- | ------------ | ---------------- |
| id          | bigint        | 主键         | 自增             |
| detail      | varchar(32)   | 消息内容     |                  |
| image       | varchar(255)  | 相关图片路径 |                  |
| sort        | int           | 排序字段     | 用于消息展示顺序   |
| status      | int           | 状态         | 1启用 0禁用         |



