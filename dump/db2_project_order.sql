-- MariaDB dump 10.19  Distrib 10.6.5-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: db2_project
-- ------------------------------------------------------
-- Server version	10.6.5-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) NOT NULL,
  `id_offer` int(11) NOT NULL,
  `creation_date` datetime NOT NULL DEFAULT current_timestamp(),
  `start_date` datetime NOT NULL,
  `total_monthly_fee` double NOT NULL,
  `status` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_1_idx` (`id_offer`),
  KEY `fk_order_2_idx` (`id_user`),
  CONSTRAINT `fk_order_1` FOREIGN KEY (`id_offer`) REFERENCES `offer` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fk_order_2` FOREIGN KEY (`id_user`) REFERENCES `customer` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES (33,15,1,'2021-12-19 21:22:43','2022-01-07 00:00:00',100,2),(34,15,1,'2021-12-19 21:22:43','2022-01-07 00:00:00',100,2),(35,15,2,'2021-12-19 21:26:10','2022-01-08 00:00:00',200,2),(36,15,1,'2021-12-19 21:31:48','2022-01-08 00:00:00',100,2),(37,13,3,'2021-12-19 21:32:56','2022-01-01 00:00:00',1234,1),(38,13,3,'2021-12-19 21:32:56','2022-01-01 00:00:00',1234,1),(39,13,3,'2021-12-19 21:32:56','2022-01-01 00:00:00',1234,1),(40,13,1,'2021-12-19 21:58:57','2021-12-31 00:00:00',100,1),(41,13,1,'2021-12-19 21:58:57','2021-12-31 00:00:00',100,1),(42,13,2,'2021-12-19 21:59:59','2022-01-01 00:00:00',200,1),(43,14,1,'2021-12-19 22:31:16','2022-01-08 00:00:00',100,2),(44,14,1,'2021-12-19 22:31:16','2022-01-08 00:00:00',100,2),(45,14,3,'2021-12-19 22:47:24','2022-01-05 00:00:00',1234,2),(46,13,1,'2021-12-19 23:10:40','2021-12-30 00:00:00',100,2),(47,13,1,'2021-12-19 23:10:40','2021-12-30 00:00:00',100,1);
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-12-20  8:49:24
