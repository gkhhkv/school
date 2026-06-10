-- H2 兼容版建表脚本 (MySQL 模式)

CREATE TABLE IF NOT EXISTS `user` (
    user_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    phone       VARCHAR(20)  DEFAULT NULL,
    address     VARCHAR(255) DEFAULT NULL,
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS product (
    product_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(200)   NOT NULL,
    category    VARCHAR(50)    NOT NULL DEFAULT '',
    promo_type  VARCHAR(20)    DEFAULT NULL,
    promo_end_time TIMESTAMP   DEFAULT NULL,
    price       DECIMAL(10,2)  NOT NULL,
    stock       INT            NOT NULL DEFAULT 0,
    image       VARCHAR(500)   DEFAULT NULL,
    created_at  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `order` (
    order_id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no         VARCHAR(32)    NOT NULL UNIQUE,
    user_id          BIGINT         NOT NULL,
    total_amount     DECIMAL(12,2)  NOT NULL,
    status           VARCHAR(20)    NOT NULL DEFAULT 'PENDING_PAYMENT',
    paid_at          TIMESTAMP      DEFAULT NULL,
    shipped_at       TIMESTAMP      DEFAULT NULL,
    shipping_address VARCHAR(255)   NOT NULL,
    contact_name     VARCHAR(50)    NOT NULL,
    contact_phone    VARCHAR(20)    NOT NULL,
    created_at       TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES `user`(user_id)
);

CREATE TABLE IF NOT EXISTS order_item (
    item_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT         NOT NULL,
    product_id BIGINT         NOT NULL,
    quantity   INT            NOT NULL,
    unit_price DECIMAL(10,2)  NOT NULL,
    subtotal   DECIMAL(12,2)  NOT NULL,
    FOREIGN KEY (order_id)   REFERENCES `order`(order_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

CREATE TABLE IF NOT EXISTS cart (
    cart_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT   NOT NULL,
    product_id BIGINT   NOT NULL,
    quantity   INT      NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)    REFERENCES `user`(user_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- 测试种子数据
INSERT INTO `user` (user_id, username, password, phone, address)
VALUES (1, 'testuser', 'password', '13800138000', '北京市朝阳区测试路100号');

INSERT INTO product (product_id, name, category, promo_type, promo_end_time, price, stock, image)
VALUES (1, '测试商品1', '数码', 'seckill', '2026-12-31 23:59:59', 99.00, 100, '/img/p1.jpg');

INSERT INTO product (product_id, name, category, promo_type, promo_end_time, price, stock, image)
VALUES (2, '测试商品2', '服装', NULL, NULL, 199.00, 50, '/img/p2.jpg');

INSERT INTO product (product_id, name, category, promo_type, promo_end_time, price, stock, image)
VALUES (3, '测试商品3', '食品', 'hot', '2026-12-31 23:59:59', 299.00, 0, '/img/p3.jpg');
