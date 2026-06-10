-- MySQL 种子数据

DELETE FROM order_item;
DELETE FROM `order`;
DELETE FROM cart;
DELETE FROM product;
DELETE FROM `user`;

INSERT INTO `user` (user_id, username, password, phone, address)
VALUES (1, 'testuser', 'password', '13800138000', '北京市朝阳区测试路100号');

INSERT INTO product (product_id, name, category, promo_type, promo_end_time, price, stock, image) VALUES
-- 数码（原有12件）
(1, '数码 华为 MateBook X Pro', '数码', 'seckill',       '2026-12-31 23:59:59', 8999.00, 100, '/img/p1.webp'),
(2, '数码 iPhone 15 Pro Max', '数码', 'fullreduction', '2026-12-31 23:59:59', 9999.00, 50,  '/img/p2.webp'),
(3, '数码 索尼 WH-1000XM5 耳机', '数码', 'new',          '2026-12-31 23:59:59', 2499.00, 200, '/img/p3.webp'),
(4, '数码 MacBook Air 15', '数码', 'hot',          '2026-12-31 23:59:59', 10499.00, 80, '/img/p4.webp'),
(5, '数码 iPad Pro M4', '数码', 'seckill',       '2026-12-31 23:59:59', 6799.00, 60, '/img/p5.webp'),
(6, '数码 AirPods Pro 2', '数码', 'fullreduction', '2026-12-31 23:59:59', 1899.00, 150, '/img/p6.webp'),
(7, '数码 华为 Watch GT 4', '数码', 'new',          '2026-12-31 23:59:59', 1488.00, 120, '/img/p7.webp'),
(8, '数码 小米 14 Ultra', '数码', 'seckill',       '2026-12-31 23:59:59', 5999.00, 90, '/img/p8.webp'),
(9, '数码 戴尔 U2723QE 显示器', '数码', 'fullreduction', '2026-12-31 23:59:59', 3999.00, 40, '/img/p9.webp'),
(10, '数码 罗技 MX Master 3S', '数码', 'new',          '2026-12-31 23:59:59', 699.00, 300, '/img/p10.webp'),
(11, '数码 三星 T7 2TB 移动硬盘', '数码', 'hot',          '2026-12-31 23:59:59', 1099.00, 150, '/img/p11.webp'),
(12, '数码 佳能 EOS R6 Mark II', '数码', 'hot',          '2026-12-31 23:59:59', 16499.00, 30, '/img/p12.webp'),
-- 服装（新增3件）
(13, '服装 纯棉圆领T恤', '服装', 'new',          '2026-12-31 23:59:59', 99.00, 500, '/img/p13.webp'),
(14, '服装 直筒牛仔裤', '服装', 'fullreduction', '2026-12-31 23:59:59', 299.00, 300, '/img/p14.webp'),
(15, '服装 轻量跑步鞋', '服装', 'hot',          '2026-12-31 23:59:59', 499.00, 200, '/img/p15.webp'),
-- 食品（新增3件）
(16, '食品 东北有机大米 5kg', '食品', 'new',          '2026-12-31 23:59:59', 79.00, 400, '/img/p16.webp'),
(17, '食品 进口特级初榨橄榄油', '食品', 'fullreduction', '2026-12-31 23:59:59', 128.00, 250, '/img/p17.webp'),
(18, '食品 每日坚果礼盒 30包', '食品', 'hot',          '2026-12-31 23:59:59', 149.00, 150, '/img/p18.webp'),
-- 家居（新增3件）
(19, '家居 天然乳胶护颈枕', '家居', 'new',          '2026-12-31 23:59:59', 259.00, 180, '/img/p19.webp'),
(20, '家居 可折叠收纳箱 60L', '家居', 'seckill',       '2026-12-31 23:59:59', 69.00, 300, '/img/p20.webp'),
(21, '家居 LED护眼台灯', '家居', 'seckill',       '2026-12-31 23:59:59', 199.00, 120, '/img/p21.webp');
