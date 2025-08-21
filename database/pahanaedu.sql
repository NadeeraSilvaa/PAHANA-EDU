-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 20, 2025 at 06:35 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pahanaedu`
--

-- --------------------------------------------------------

--
-- Table structure for table `bills`
--

CREATE TABLE `bills` (
  `id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `bill_amount` decimal(10,2) NOT NULL,
  `bill_date` date NOT NULL,
  `status` enum('PENDING','PAID','CANCELLED') DEFAULT 'PENDING'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bills`
--

INSERT INTO `bills` (`id`, `customer_id`, `bill_amount`, `bill_date`, `status`) VALUES
(10, 7, 2071.25, '2025-08-20', 'PENDING'),
(15, 12, 307.59, '2025-08-20', 'PENDING'),
(16, 13, 26750.00, '2025-08-20', 'PENDING'),
(17, 13, 2000.00, '2025-08-20', 'PENDING');

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `account_number` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `address` varchar(200) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `units` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`account_number`, `name`, `address`, `phone`, `units`) VALUES
(7, 'John Smith', '123 Maple St, Springfield, IL', '555-0123', 7),
(9, 'Michael Brown', '789 Pine Rd, Boston, MA', '555-3456', 0),
(10, 'Sarah Davis', '321 Elm St, Austin, TX', '555-4567', 0),
(11, 'David Wilsonk', '654 Cedar Ln, Seattle, WA', '555-5678', 0),
(12, 'Laura Martinezhj', '987 Birch Dr, Miami, FL', '555-6789', 13),
(13, 'James Taylor', '147 Walnut St, Denver, CO', '555-7890', 13),
(14, 'Emily Anderson', '258 Spruce Ct, Portland, OR', '555-8901', 0),
(15, 'Robert Thomas', '369 Willow Way, Atlanta, GA', '555-9012', 0),
(16, 'Olivia Lee', '741 Chestnut Blvd, San Diego, CA', '555-1234', 0),
(17, 'Pasindu Dilshan Herath', 'Akash Saloon, kudawewa junction', '0761030267', 0),
(18, 'Test Customer', 'Test Address', '1234567890', 0);

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE `items` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `author` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `items`
--

INSERT INTO `items` (`id`, `name`, `price`, `author`, `image_url`, `category`) VALUES
(7, 'Harry Potter', 1000.00, 'J K Rowling', 'uploads/HarryPotter.jpg', 'Fiction'),
(8, 'The Silent Forest', 12.99, 'Alice Harper', 'uploads/silentforest.jpg', 'Fiction'),
(9, 'Quantum Theory', 4500.00, 'Robert Kline', 'uploads/quantamtheory.jpg', 'Science Fiction'),
(10, 'Unveiled Truth', 1600.00, 'Emma Stone', 'uploads/uneviledtruth.jpg', 'Non-Fiction'),
(11, 'Mystery of the Lake', 3750.00, 'James Carter', 'uploads/mystrylake.jpg', 'Mystery'),
(13, 'The Code Breaker', 29.95, 'David Walsh', 'uploads/codebreaker.jpg', 'Thriller'),
(14, 'Wandering The Earth', 1890.00, 'Sarah Mitchell', 'uploads/WanderingTheEarth.jpg', 'History'),
(15, 'Starlit Adventures', 1345.00, 'Michael Reed', 'uploads/starlit.jpg', 'Fantasy'),
(16, 'Cooking with Love', 1500.00, 'Olivia Grant', 'uploads/book9.jpg', 'Cooking'),
(17, 'The Art of Code', 39.99, 'Emily Chen', 'uploads/book10.jpg', 'Technology');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `role` varchar(20) NOT NULL CHECK (`role` in ('Admin','Cashier'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `role`) VALUES
(2, 'Cashier', 'pass123', 'Cashier'),
(6, 'admin', 'pass123', 'Admin');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bills`
--
ALTER TABLE `bills`
  ADD PRIMARY KEY (`id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`account_number`);

--
-- Indexes for table `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bills`
--
ALTER TABLE `bills`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `account_number` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `items`
--
ALTER TABLE `items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bills`
--
ALTER TABLE `bills`
  ADD CONSTRAINT `bills_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`account_number`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
