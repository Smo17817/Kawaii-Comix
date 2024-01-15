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
-- Table structure for table `ordine_singolo`
--

DROP TABLE IF EXISTS `ordine_singolo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordine_singolo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `quantità` int NOT NULL,
  `totale_parziale` double NOT NULL,
  `ordini_id` int NOT NULL,
  `prodotti_isbn` char(17) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ordine_singolo_ordini1_idx` (`ordini_id`),
  KEY `fk_prodotti_isbn_idx` (`prodotti_isbn`),
  CONSTRAINT `fk_ordine_singolo_ordini1` FOREIGN KEY (`ordini_id`) REFERENCES `ordini` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_prodotti_isbn` FOREIGN KEY (`prodotti_isbn`) REFERENCES `prodotti` (`isbn`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordine_singolo`
--

LOCK TABLES `ordine_singolo` WRITE;
/*!40000 ALTER TABLE `ordine_singolo` DISABLE KEYS */;
INSERT INTO `ordine_singolo` VALUES (14,1,5.45,88569,'10000000000000001'),(15,1,5.45,67996,'10000000000000005'),(16,1,7.49,67996,'10000000000000099'),(17,1,7.49,67996,'10000000000000100'),(18,1,7.49,22067,'10000000000000099'),(19,1,7.49,22067,'10000000000000100'),(20,1,7.49,22067,'10000000000000102'),(21,1,7.49,88607,'10000000000000099'),(22,1,6.45,46771,'10000000000000082'),(23,1,6.45,46771,'10000000000000083'),(24,1,19.99,46771,'10000000000000056'),(25,1,4.99,18182,'10000000000000051'),(26,1,5.45,18182,'10000000000000087'),(27,1,5.45,79733,'10000000000000105'),(28,1,5.45,79733,'10000000000000069'),(29,3,22.47,37619,'10000000000000099'),(30,1,7.49,37619,'10000000000000100'),(31,3,16.35,37619,'10000000000000068'),(32,1,106.59,33335,'10000000000000025'),(33,1,5.45,18285,'10000000000000045'),(34,1,5.45,18285,'10000000000000046'),(35,1,19.99,51943,'10000000000000106'),(36,10,74.9,40358,'10000000000000100'),(37,5,39.95,40358,'10000000000000034'),(38,7,55.93,40358,'10000000000000090'),(39,25,136.25,93813,'10000000000000005');
/*!40000 ALTER TABLE `ordine_singolo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-10 10:46:58