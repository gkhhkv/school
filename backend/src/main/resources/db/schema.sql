-- 电商购物平台数据库建表脚本 (MySQL)
-- 执行前请先创建数据库: CREATE DATABASE ecommerce DEFAULT CHARACTER SET utf8mb4;

-- 按依赖逆序删除旧表，保证 schema 变更生效
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS `user`;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    user_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    phone       VARCHAR(20)  DEFAULT NULL,
    address     VARCHAR(255) DEFAULT NULL,
    created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 商品表
CREATE TABLE IF NOT EXISTS product (
    product_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(200)   NOT NULL,
    category    VARCHAR(50)    NOT NULL DEFAULT '',
    promo_type  VARCHAR(20)    DEFAULT NULL,
    promo_end_time DATETIME    DEFAULT NULL,
    price       DECIMAL(10,2)  NOT NULL,
    stock       INT            NOT NULL DEFAULT 0,
    image       VARCHAR(500)   DEFAULT NULL,
    created_at  DATETIME       DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 订单主表
CREATE TABLE IF NOT EXISTS `order` (
    order_id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no         VARCHAR(32)    NOT NULL UNIQUE,
    user_id          BIGINT         NOT NULL,
    total_amount     DECIMAL(12,2)  NOT NULL,
    status           VARCHAR(20)    NOT NULL DEFAULT 'PENDING_PAYMENT',
    paid_at          DATETIME       DEFAULT NULL,
    shipped_at       DATETIME       DEFAULT NULL,
    shipping_address VARCHAR(255)   NOT NULL,
    contact_name     VARCHAR(50)    NOT NULL,
    contact_phone    VARCHAR(20)    NOT NULL,
    created_at       DATETIME       DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES `user`(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 订单明细表
CREATE TABLE IF NOT EXISTS order_item (
    item_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT         NOT NULL,
    product_id BIGINT         NOT NULL,
    quantity   INT            NOT NULL,
    unit_price DECIMAL(10,2)  NOT NULL,
    subtotal   DECIMAL(12,2)  NOT NULL,
    FOREIGN KEY (order_id)   REFERENCES `order`(order_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 购物车表
CREATE TABLE IF NOT EXISTS cart (
    cart_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT   NOT NULL,
    product_id BIGINT   NOT NULL,
    quantity   INT      NOT NULL DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)    REFERENCES `user`(user_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
