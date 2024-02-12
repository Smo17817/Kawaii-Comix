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
  `immagine_prodotto` varchar(100) NOT NULL,
  `nome_prodotto` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ordine_singolo_ordini1_idx` (`ordini_id`),
  CONSTRAINT `fk_ordine_singolo_ordini1` FOREIGN KEY (`ordini_id`) REFERENCES `ordini` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordine_singolo`
--

LOCK TABLES `ordine_singolo` WRITE;
/*!40000 ALTER TABLE `ordine_singolo` DISABLE KEYS */;
INSERT INTO `ordine_singolo` VALUES (41,1,17.5,82097,'./images/berserk1.jpg','Berserk 1'),(42,1,6.45,24323,'./images/aynis2.jpg','All You Need Is Kill 2'),(43,1,106.59,24323,'./images/berserkDeluxe.jpg','Berserk Edizione Deluxe 1'),(44,1,9.99,24323,'./images/berserk4.jpg','Berserk 4'),(45,1,7.49,14426,'./images/akira4.jpg','Akira 4'),(46,1,7.49,14426,'./images/akira3.jpg','Akira 3'),(47,1,5.45,64670,'./images/bleach4.jpg','Bleach 4'),(48,1,19.99,64670,'./images/deathnotenovel.jpg','Death Note - Another Note'),(49,1,14.99,64670,'./images/deathnote1.jpg','Death Note 1'),(50,1,5.45,64670,'./images/bleach3.jpg','Bleach 3'),(51,1,5.45,64670,'./images/bm1.jpg','Bloody Monday 1');
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

-- Dump completed on 2024-02-11  1:58:28
