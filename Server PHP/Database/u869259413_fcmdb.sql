
-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Dec 04, 2016 at 03:33 PM
-- Server version: 10.0.20-MariaDB
-- PHP Version: 5.2.17

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `u869259413_fcmdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `status`
--

CREATE TABLE IF NOT EXISTS `status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `sstatus` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Hey there! I am using Firebase Messenger☺',
  `stime` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=9 ;

--
-- Dumping data for table `status`
--

INSERT INTO `status` (`id`, `uid`, `sstatus`, `stime`) VALUES
(2, 39, ':-p', '2016-12-04 00:41:00'),
(6, 43, 'Hey there! I am using Firebase Messenger', '2016-12-03 00:00:00'),
(8, 45, 'yayyyyyyyyyyyy', '2016-12-04 00:39:44');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `token` longtext COLLATE utf8_unicode_ci NOT NULL,
  `joindate` text COLLATE utf8_unicode_ci NOT NULL,
  `lastseen` varchar(300) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'online',
  `profpic` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'default.png',
  PRIMARY KEY (`name`),
  KEY `id` (`id`),
  KEY `id_2` (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=46 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `token`, `joindate`, `lastseen`, `profpic`) VALUES
(43, 'Sonu', 'eXmmpMl3un4:APA91bE9Qt7b3SEeQBOilmfjUqqtRAtsOG30ydMtPZ0s3PbU9L3P85hoq_YHVRKB4ke3FcXmM03HUaG56SodBzwUKVz_ifV0UlI3YYL7YJ1y-_P0OFK20X0kIkok2SV6zBDLQa0aBbzo', '2016-12-03', '2016-12-03 13:22:21', 'default.png'),
(45, 'Anam', 'fKaq3oFOIc4:APA91bHsxXYg4fOV6lkQye9vrvDLorCYTmpLiBCdar4GTFMblkxWFCxS3HuP-6sA_-UHmLbjTh9JLk-4-DnajzpLnMaFemvXX4j_aP5X_hWuypIIpmCo60YXljox65q5imkpqr3fYraa', '2016-12-03', '2016-12-04 15:36:31', 'default.png'),
(39, 'Akash', 'fFQiPP1zh3M:APA91bHMlaVBg3wUtqfWnNmwNzAonqxrY-wlY4y_1TawS-Y4hRkkJ9PZPdUCd1fwQ-jTxvL7xxbe-mxvMkPOYT6sf_c2t-CwO_9DwRyEeHshGZSMsivjPe1j7v-LuHTl12afr8xiYcNs', '2016-12-02', '2016-12-04 19:06:49', '1480845528_39.png');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
