-- MySQL 种子数据（INSERT IGNORE 确保重复执行不报错）

INSERT IGNORE INTO `user` (user_id, username, password, phone, address)
VALUES (1, 'testuser', 'password', '13800138000', '北京市朝阳区测试路100号');

INSERT IGNORE INTO product (product_id, name, price, stock, image) VALUES
(1, '华为 MateBook X Pro', 8999.00, 100, '/img/p1.jpg'),
(2, 'iPhone 15 Pro Max', 9999.00, 50,  '/img/p2.jpg'),
(3, '索尼 WH-1000XM5 耳机', 2499.00, 200, '/img/p3.jpg'),
(4, 'MacBook Air 15', 10499.00, 80, '/img/p4.jpg'),
(5, 'iPad Pro M4', 6799.00, 60, '/img/p5.jpg'),
(6, 'AirPods Pro 2', 1899.00, 150, '/img/p6.jpg'),
(7, '华为 Watch GT 4', 1488.00, 120, '/img/p7.jpg'),
(8, '小米 14 Ultra', 5999.00, 90, '/img/p8.jpg'),
(9, '戴尔 U2723QE 显示器', 3999.00, 40, '/img/p9.jpg'),
(10, '罗技 MX Master 3S', 699.00, 300, '/img/p10.jpg'),
(11, '三星 T7 2TB 移动硬盘', 1099.00, 150, '/img/p11.jpg'),
(12, '佳能 EOS R6 Mark II', 16499.00, 30, '/img/p12.jpg');
