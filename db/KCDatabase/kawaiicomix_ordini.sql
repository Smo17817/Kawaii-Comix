-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: kawaiicomix
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ordini`
--

DROP TABLE IF EXISTS `ordini`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordini` (
  `id` int NOT NULL AUTO_INCREMENT,
  `data` date NOT NULL,
  `totale` double NOT NULL,
  `site_user_id` int NOT NULL,
  `stato_ordine_id` int NOT NULL,
  `metodo_spedizione_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ordini_site_user1_idx` (`site_user_id`),
  KEY `fk_ordini_stato_ordine1_idx` (`stato_ordine_id`),
  KEY `fk_ordini_metodo_spedizione1_idx` (`metodo_spedizione_id`),
  CONSTRAINT `fk_ordini_metodo_spedizione1` FOREIGN KEY (`metodo_spedizione_id`) REFERENCES `metodo_spedizione` (`id`),
  CONSTRAINT `fk_ordini_site_user1` FOREIGN KEY (`site_user_id`) REFERENCES `site_user` (`id`),
  CONSTRAINT `fk_ordini_stato_ordine1` FOREIGN KEY (`stato_ordine_id`) REFERENCES `stato_ordine` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=97214 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordini`
--

LOCK TABLES `ordini` WRITE;
/*!40000 ALTER TABLE `ordini` DISABLE KEYS */;
INSERT INTO `ordini` VALUES (18182,'2023-07-03',20.44,2,1,1),(18285,'2023-07-05',20.9,1,1,1),(22067,'2023-07-01',32.47,1,2,1),(33335,'2023-07-04',116.59,1,1,1),(37619,'2023-07-04',56.31,1,2,1),(40358,'2023-07-07',180.78,4,2,1),(46771,'2023-07-02',42.89,1,1,1),(51943,'2023-07-05',29.99,3,2,1),(67996,'2023-07-01',30.43,1,1,1),(79733,'2023-07-03',20.9,2,2,1),(88569,'2023-07-01',15.45,1,2,1),(88607,'2023-07-02',17.49,1,1,1),(93813,'2023-07-08',146.25,1,2,1);
/*!40000 ALTER TABLE `ordini` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-15 18:53:21
