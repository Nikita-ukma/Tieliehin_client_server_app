-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1
-- Час створення: Чрв 28 2024 р., 13:34
-- Версія сервера: 10.4.32-MariaDB
-- Версія PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База даних: `storedb`
--

-- --------------------------------------------------------

--
-- Структура таблиці `product`
--

CREATE TABLE `product` (
  `id_product` int(11) NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `manufacturer` varchar(255) DEFAULT NULL,
  `amount` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `group_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп даних таблиці `product`
--

INSERT INTO `product` (`id_product`, `product_name`, `description`, `manufacturer`, `amount`, `price`, `group_id`) VALUES
(1, 'Prazhskiy cake', 'tasty', 'Roshen', 90, 239.00, 3),
(4, 'Beef', 'beef composition: fats, herbs, carbohydrates', 'Cargill', 100, 59.99, 2),
(14, 'Smartphone', 'Latest model with advanced features', 'Samsung', 50, 699.99, 14),
(15, 'Laptop', 'High-performance laptop with 16GB RAM', 'Apple', 30, 1299.99, 14),
(16, 'LED TV', '55-inch 4K Ultra HD Smart TV', 'Sony', 40, 799.99, 14),
(17, 'Bluetooth Speaker', 'Portable speaker with high-quality sound', 'LG', 100, 149.99, 14),
(18, 'Sofa', 'Comfortable 3-seater sofa', 'IKEA', 20, 499.99, 15),
(19, 'Dining Table', 'Wooden dining table for 6 people', 'Ashley Furniture', 15, 599.99, 15),
(20, 'Office Chair', 'Ergonomic office chair with lumbar support', 'Steelcase', 50, 299.99, 15),
(21, 'T-Shirt', 'Cotton t-shirt available in various sizes', 'Nike', 200, 29.99, 16),
(22, 'Sneakers', 'Comfortable running shoes', 'Adidas', 150, 89.99, 16),
(23, 'Jeans', 'Slim fit jeans with stretch', 'Zara', 100, 59.99, 16),
(24, 'Running Shoes', 'Lightweight running shoes with cushioned sole', 'Nike', 120, 99.99, 17),
(25, 'Soccer Cleats', 'High-performance cleats for soccer', 'Adidas', 80, 129.99, 17),
(26, 'Basketball Shoes', 'Durable shoes for basketball players', 'Puma', 90, 119.99, 17),
(27, 'Doll', 'Fashion doll with accessories', 'Mattel', 150, 19.99, 18),
(28, 'Action Figure', 'Superhero action figure with moveable parts', 'Hasbro', 200, 24.99, 18),
(29, 'Building Blocks', 'Construction set with 500 pieces', 'LEGO', 100, 59.99, 18),
(30, 'Novel', 'Bestselling novel by renowned author', 'Penguin Random House', 100, 14.99, 19),
(31, 'Cookbook', 'Collection of recipes from famous chef', 'HarperCollins', 80, 29.99, 19),
(32, 'Children\'s Book', 'Illustrated book for children', 'Simon & Schuster', 120, 12.99, 19),
(33, 'Pens', 'Pack of 10 ballpoint pens', 'Staples', 300, 5.99, 20),
(34, 'Notebook', 'Spiral notebook with 200 pages', 'Office Depot', 200, 4.99, 20),
(35, 'Sticky Notes', 'Pack of 5 colored sticky note pads', '3M', 250, 7.99, 20),
(36, 'Disinfectant Wipes', 'Pack of 75 disinfectant wipes', 'Procter & Gamble', 150, 6.99, 21),
(37, 'All-Purpose Cleaner', 'Bottle of all-purpose cleaner', 'SC Johnson', 180, 3.99, 21),
(38, 'Bleach', 'Gallon of bleach', 'Clorox', 100, 2.99, 21),
(39, 'Shampoo', 'Bottle of shampoo for all hair types', 'L\'Oreal', 200, 8.99, 22),
(40, 'Lipstick', 'Long-lasting lipstick in various shades', 'Estée Lauder', 150, 24.99, 22),
(41, 'Moisturizer', 'Daily moisturizer with SPF', 'Procter & Gamble', 100, 15.99, 22),
(42, 'Car Tires', 'Set of 4 all-season car tires', 'Toyota', 50, 399.99, 23),
(43, 'Engine Oil', '5-quart bottle of engine oil', 'Ford', 100, 29.99, 23),
(44, 'Car Battery', '12V car battery with 600 CCA', 'General Motors', 40, 99.99, 23),
(45, 'Lawn Mower', 'Electric lawn mower with 21-inch deck', 'Scotts Miracle-Gro', 30, 249.99, 24),
(46, 'Garden Shears', 'Heavy-duty garden shears', 'Fiskars', 70, 19.99, 24),
(47, 'Beef Premium', 'Premium cuts of beef', 'Tyson', 50, 12.99, 2),
(48, 'Chicken', 'Fresh chicken breasts', 'Cargill', 80, 8.99, 2),
(49, 'Pork', 'Tender pork loin', 'National Beef', 60, 10.99, 2),
(50, 'Milk', 'Fresh whole milk', 'Nestlé', 100, 3.99, 3),
(51, 'Yogurt', 'Assorted yogurt flavors', 'Danone', 120, 1.99, 3),
(52, 'Cheese', 'Variety of cheese types', 'Lactalis', 80, 5.99, 3),
(100, 'Beef Royal', 'Premium cuts of beef', 'Tyson', 50, 12.99, 2),
(125, 'Milk', 'Fresh whole milk', 'Nestlé', 100, 3.99, 3),
(126, 'Cheese', 'Variety of cheese types', 'Lactalis', 80, 5.99, 3),
(127, 'Yogurt', 'Assorted yogurt flavors', 'Danone', 120, 1.99, 3);

-- --------------------------------------------------------

--
-- Структура таблиці `productgroup`
--

CREATE TABLE `productgroup` (
  `group_id` int(11) NOT NULL,
  `group_name` varchar(255) NOT NULL,
  `description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп даних таблиці `productgroup`
--

INSERT INTO `productgroup` (`group_id`, `group_name`, `description`) VALUES
(2, 'Meat', 'Include products of this companies:\r\nTyson, Cargill, and Brazil-based National Beef and JB'),
(3, 'Sweets', 'The best product group'),
(5, 'Bakery', 'Include products of this companies:\r\nGrupo Bimbo, Mondelez International, and Yamazaki Baking'),
(6, 'Fruits', 'Include products of this companies:\r\nDole Food Company, Chiquita Brands International, and Del Monte Foods'),
(7, 'Vegetables', 'Include products of this companies:\r\nGreen Giant, Birds Eye, and Bonduelle'),
(8, 'Seafood', 'Include products of this companies:\r\nMarine Harvest, Thai Union Group, and Nippon Suisan Kaisha'),
(9, 'Snacks', 'Include products of this companies:\r\nPepsiCo, Mondelez International, and Kellogg Company'),
(10, 'Confectionery', 'Include products of this companies:\r\nMars, Mondelez International, and Nestlé'),
(11, 'Frozen Foods', 'Include products of this companies:\r\nNestlé, Conagra Brands, and McCain Foods'),
(12, 'Beverages - Alcoholic', 'Include products of this companies:\r\nAnheuser-Busch InBev, Heineken, and Diageo'),
(13, 'Pet Foods', 'Include products of this companies:\r\nMars Petcare, Nestlé Purina PetCare, and Hill\'s Pet Nutrition'),
(14, 'Electronics', 'Include products of this companies:\r\nSamsung, Apple, Sony, and LG'),
(15, 'Furniture', 'Include products of this companies:\r\nIKEA, Ashley Furniture, and Steelcase'),
(16, 'Clothing', 'Include products of this companies:\r\nNike, Adidas, Zara, and H&M'),
(17, 'Footwear', 'Include products of this companies:\r\nNike, Adidas, Puma, and Skechers'),
(18, 'Toys', 'Include products of this companies:\r\nMattel, Hasbro, and LEGO'),
(19, 'Books', 'Include products of this companies:\r\nPenguin Random House, HarperCollins, and Simon & Schuster'),
(20, 'Office Supplies', 'Include products of this companies:\r\nStaples, Office Depot, and 3M'),
(21, 'Cleaning Supplies', 'Include products of this companies:\r\nProcter & Gamble, SC Johnson, and Clorox'),
(22, 'Beauty Products', 'Include products of this companies:\r\nL\'Oreal, Estée Lauder, and Procter & Gamble'),
(23, 'Automotive', 'Include products of this companies:\r\nToyota, Ford, and General Motors'),
(24, 'Gardening', 'Include products of this companies:\r\nScotts Miracle-Gro, Fiskars, and Husqvarna'),
(25, 'Sporting Goods', 'Include products of this companies:\r\nNike, Adidas, and Under Armour');

--
-- Індекси збережених таблиць
--

--
-- Індекси таблиці `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id_product`),
  ADD KEY `productGroupId` (`group_id`);

--
-- Індекси таблиці `productgroup`
--
ALTER TABLE `productgroup`
  ADD PRIMARY KEY (`group_id`);

--
-- AUTO_INCREMENT для збережених таблиць
--

--
-- AUTO_INCREMENT для таблиці `product`
--
ALTER TABLE `product`
  MODIFY `id_product` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2043141858;

--
-- AUTO_INCREMENT для таблиці `productgroup`
--
ALTER TABLE `productgroup`
  MODIFY `group_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- Обмеження зовнішнього ключа збережених таблиць
--

--
-- Обмеження зовнішнього ключа таблиці `product`
--
ALTER TABLE `product`
  ADD CONSTRAINT `product_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `productgroup` (`group_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
