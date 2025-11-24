-- phpMyAdmin SQL Dump
-- version 4.0.4.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 24, 2025 at 01:49 AM
-- Server version: 5.6.13
-- PHP Version: 5.4.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `smartship`
--
CREATE DATABASE IF NOT EXISTS `smartship` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `smartship`;

-- --------------------------------------------------------

--
-- Table structure for table `assignment`
--

CREATE TABLE IF NOT EXISTS `assignment` (
  `assignmentID` int(11) NOT NULL AUTO_INCREMENT,
  `plateNumber` varchar(25) NOT NULL,
  `trackingNumber` int(11) NOT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `startTime` time DEFAULT NULL,
  `endTime` time DEFAULT NULL,
  `overseerID` int(11) DEFAULT NULL,
  `driverID` int(11) NOT NULL,
  PRIMARY KEY (`assignmentID`,`plateNumber`,`trackingNumber`),
  KEY `assignmentFK1` (`plateNumber`),
  KEY `assignmentFK2` (`trackingNumber`),
  KEY `overseerID` (`overseerID`),
  KEY `driverID` (`driverID`),
  KEY `overseerID_2` (`overseerID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `assignment`
--

INSERT INTO `assignment` (`assignmentID`, `plateNumber`, `trackingNumber`, `startDate`, `endDate`, `startTime`, `endTime`, `overseerID`, `driverID`) VALUES
(1, 'ABC2004', 2, '2025-11-23', '2025-12-31', '00:10:44', '00:15:13', 6, 8),
(7, 'CAD9999', 4, '2025-11-23', NULL, '00:26:20', NULL, 7, 8),
(8, 'ABC2004', 1, NULL, NULL, NULL, NULL, 6, 13);

-- --------------------------------------------------------

--
-- Table structure for table `clerk`
--

CREATE TABLE IF NOT EXISTS `clerk` (
  `clerkID` int(11) NOT NULL,
  `salary` double NOT NULL,
  `yearEmployed` int(11) NOT NULL,
  PRIMARY KEY (`clerkID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `clerk`
--

INSERT INTO `clerk` (`clerkID`, `salary`, `yearEmployed`) VALUES
(6, 728882, 2000);

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE IF NOT EXISTS `customer` (
  `custID` int(25) NOT NULL,
  `address` varchar(250) NOT NULL,
  `phone` varchar(25) NOT NULL,
  PRIMARY KEY (`custID`),
  UNIQUE KEY `custID` (`custID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`custID`, `address`, `phone`) VALUES
(1, '1 Baldplate,Kingston,Ja', '8502495'),
(3, '23 Fern Gully Kingston 45', '8769734657'),
(4, 'Helshire', '83782927482'),
(9, '123 Main,,Kingston,Jamaica', '555-0199');

-- --------------------------------------------------------

--
-- Table structure for table `driver`
--

CREATE TABLE IF NOT EXISTS `driver` (
  `driverID` int(11) NOT NULL,
  `salary` double NOT NULL,
  `yearEmployed` int(11) NOT NULL,
  PRIMARY KEY (`driverID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `driver`
--

INSERT INTO `driver` (`driverID`, `salary`, `yearEmployed`) VALUES
(8, 99000, 2009),
(13, 50000, 2008);

-- --------------------------------------------------------

--
-- Table structure for table `invoice`
--

CREATE TABLE IF NOT EXISTS `invoice` (
  `invoiceID` int(11) NOT NULL AUTO_INCREMENT,
  `trackingNumber` int(11) NOT NULL,
  `method` varchar(25) NOT NULL,
  `status` varchar(25) NOT NULL,
  `discount` double NOT NULL,
  `cost` double NOT NULL,
  `balance` double NOT NULL,
  `custID` int(11) NOT NULL,
  PRIMARY KEY (`invoiceID`,`trackingNumber`),
  KEY `invoiceFK` (`trackingNumber`),
  KEY `custID` (`custID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `invoice`
--

INSERT INTO `invoice` (`invoiceID`, `trackingNumber`, `method`, `status`, `discount`, `cost`, `balance`, `custID`) VALUES
(1, 1, 'Card', 'Paid', 52050, 468450, 0, 1),
(2, 2, 'Cash', 'Paid', 2150, 19350, 0, 3),
(3, 3, 'Card', 'Partially Paid', 2300, 20700, 20000, 4),
(4, 4, 'Cash', 'Paid', 0, 2500, 0, 1),
(5, 5002, 'Cash', 'Unpaid', 29650, 266850, 266850, 1);

-- --------------------------------------------------------

--
-- Table structure for table `manager`
--

CREATE TABLE IF NOT EXISTS `manager` (
  `managerID` int(11) NOT NULL,
  `salary` double NOT NULL,
  `yearEmployed` int(11) NOT NULL,
  PRIMARY KEY (`managerID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `manager`
--

INSERT INTO `manager` (`managerID`, `salary`, `yearEmployed`) VALUES
(7, 7, 1999),
(11, 50000, 2023);

-- --------------------------------------------------------

--
-- Table structure for table `shipment`
--

CREATE TABLE IF NOT EXISTS `shipment` (
  `trackingNumber` int(11) NOT NULL AUTO_INCREMENT,
  `recipientName` varchar(25) NOT NULL,
  `height` float NOT NULL,
  `length` float NOT NULL,
  `width` float NOT NULL,
  `destinationAddress` varchar(250) NOT NULL,
  `zone` int(11) NOT NULL,
  `type` varchar(25) NOT NULL,
  `cost` double NOT NULL,
  `weight` float NOT NULL,
  `status` varchar(25) NOT NULL,
  `custID` int(11) NOT NULL,
  PRIMARY KEY (`trackingNumber`),
  KEY `custID` (`custID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5003 ;

--
-- Dumping data for table `shipment`
--

INSERT INTO `shipment` (`trackingNumber`, `recipientName`, `height`, `length`, `width`, `destinationAddress`, `zone`, `type`, `cost`, `weight`, `status`, `custID`) VALUES
(1, 'Jane Doe', 10, 30, 20, 'Kingston, JA', 4, 'Express', 468450, 50, 'Assigned', 1),
(2, 'John Doe', 3, 3, 3, 'Spanish Town', 1, 'Standard', 19350, 3, 'Delivered', 3),
(3, 'Diwani Walters', 1, 1, 1, 'New York', 1, 'Standard', 20700, 1, 'Pending', 4),
(4, 'Jordan Thomas', 1, 1, 1, 'St. Thomas', 1, 'Standard', 2500, 1, 'In Transit', 1),
(5002, 'Close Relative', 8, 9, 8, 'Kingston', 4, 'Standard', 266850, 6, 'Pending', 1);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `userID` int(4) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(25) NOT NULL,
  `lastName` varchar(25) NOT NULL,
  `hashedPassword` varchar(250) NOT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `userID` (`userID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1001 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`userID`, `firstName`, `lastName`, `hashedPassword`) VALUES
(1, 'Duwani', 'Walters', 'c685a2c9bab235ccdd2ab0ea92281a521c8aaf37895493d080070ea00fc7f5d7'),
(3, 'Olivia', 'McFarlane', 'c06b0cfe0cc5e900c57784484094331f095bf441995c3c31ea6c75691c786c35'),
(4, 'Javone', 'Gordon', '6c4c36ad42fd8a537787cfd4a441f1c8f537a4a49a63dee74c42c08ecb42c36d'),
(6, 'Jacob', 'Edwards', 'd74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1'),
(7, 'Jane', 'Jane', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8'),
(8, 'Bartolemew', 'Allen', '1e1f43c22de38ba1d3ed9139993ca0aec46fa334df3eddface2cad255ece251e'),
(9, 'John', 'Doe', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c'),
(11, 'Sarah', 'Smith', '57777d28b11a44a474e1c1dbc080e10f43587ad7b4745184474cf3c75c9e471f'),
(13, 'Mike', 'Taller', 'dba36bffa5cab0f922d087a3aeb179f9d4e745df40b323e1b1471402848c8a3e');

-- --------------------------------------------------------

--
-- Table structure for table `vehicle`
--

CREATE TABLE IF NOT EXISTS `vehicle` (
  `plateNumber` varchar(25) NOT NULL,
  `model` varchar(25) NOT NULL,
  `weightCapacity` float NOT NULL,
  `quantityCapacity` float NOT NULL,
  `availableWeight` float NOT NULL DEFAULT '0',
  `availableQuantity` float NOT NULL DEFAULT '0',
  `status` varchar(25) NOT NULL DEFAULT 'inactive',
  PRIMARY KEY (`plateNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `vehicle`
--

INSERT INTO `vehicle` (`plateNumber`, `model`, `weightCapacity`, `quantityCapacity`, `availableWeight`, `availableQuantity`, `status`) VALUES
('ABC2004', 'sedan', 400, 25, 350, 24, 'inactive'),
('CAD9999', 'Toyota', 10, 500, 9, 499, 'active');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `assignment`
--
ALTER TABLE `assignment`
  ADD CONSTRAINT `assignmentFK4` FOREIGN KEY (`driverID`) REFERENCES `driver` (`driverID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `assignmentFK1` FOREIGN KEY (`plateNumber`) REFERENCES `vehicle` (`plateNumber`) ON UPDATE CASCADE,
  ADD CONSTRAINT `assignmentFK2` FOREIGN KEY (`trackingNumber`) REFERENCES `shipment` (`trackingNumber`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `assignmentFK3` FOREIGN KEY (`overseerID`) REFERENCES `user` (`userID`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `clerk`
--
ALTER TABLE `clerk`
  ADD CONSTRAINT `clerkFK` FOREIGN KEY (`clerkID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `customer`
--
ALTER TABLE `customer`
  ADD CONSTRAINT `custFK` FOREIGN KEY (`custID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `driver`
--
ALTER TABLE `driver`
  ADD CONSTRAINT `driverFK` FOREIGN KEY (`driverID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `invoice`
--
ALTER TABLE `invoice`
  ADD CONSTRAINT `invoiceFK1` FOREIGN KEY (`custID`) REFERENCES `customer` (`custID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `invoiceFK` FOREIGN KEY (`trackingNumber`) REFERENCES `shipment` (`trackingNumber`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `manager`
--
ALTER TABLE `manager`
  ADD CONSTRAINT `manFK` FOREIGN KEY (`managerID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `shipment`
--
ALTER TABLE `shipment`
  ADD CONSTRAINT `shipFK` FOREIGN KEY (`custID`) REFERENCES `customer` (`custID`) ON DELETE NO ACTION ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
