-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: homecam
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `homecam`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `homecam` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `homecam`;

--
-- Table structure for table `children`
--

DROP TABLE IF EXISTS `children`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `children` (
  `id` char(36) NOT NULL,
  `user_id` char(36) NOT NULL,
  `name` varchar(40) NOT NULL,
  `gender` enum('M','F','U') NOT NULL DEFAULT 'U',
  `birth_date` date DEFAULT NULL,
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`id`),
  KEY `ix_children_user` (`user_id`),
  CONSTRAINT `fk_children_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `children`
--

LOCK TABLES `children` WRITE;
/*!40000 ALTER TABLE `children` DISABLE KEYS */;
/*!40000 ALTER TABLE `children` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refresh_tokens`
--

DROP TABLE IF EXISTS `refresh_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refresh_tokens` (
  `id` varchar(36) NOT NULL,
  `user_id` char(36) NOT NULL,
  `token` varchar(512) NOT NULL,
  `expires_at` datetime(6) NOT NULL,
  `revoked` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_rt_token` (`token`),
  KEY `ix_rt_user` (`user_id`),
  KEY `ix_refresh_user` (`user_id`),
  CONSTRAINT `fk_rt_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refresh_tokens`
--

LOCK TABLES `refresh_tokens` WRITE;
/*!40000 ALTER TABLE `refresh_tokens` DISABLE KEYS */;
INSERT INTO `refresh_tokens` VALUES ('1a97e4f1-facc-4834-acf1-77042aa98532','1467074f-ea69-4ced-80ef-b4578304f107','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNDY3MDc0Zi1lYTY5LTRjZWQtODBlZi1iNDU3ODMwNGYxMDciLCJpYXQiOjE3NjMzODA0NjksImV4cCI6MTc2MzQ2Njg2OX0.sl9B9wv4MQzD2j3PwDvtG114oRwQ2l9GFFmGziRjKrA','2025-12-17 11:54:29.535239',1,'2025-11-17 11:54:29.535239'),('1aab68bb-6d00-44bd-9e69-0f53fc3d69a4','1467074f-ea69-4ced-80ef-b4578304f107','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNDY3MDc0Zi1lYTY5LTRjZWQtODBlZi1iNDU3ODMwNGYxMDciLCJpYXQiOjE3NjMzODA4MDgsImV4cCI6MTc2MzQ2NzIwOH0.1y7y8QxA9FfAXMdAgu1MbUFCG-yHUvl4uwL1s-dzmSU','2025-12-17 12:00:08.825219',1,'2025-11-17 12:00:08.825219'),('1d91473b-264f-4c6d-a88d-866a33f6b87b','961eb813-98b2-4e5a-bd9f-43bfd7dcfac6','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5NjFlYjgxMy05OGIyLTRlNWEtYmQ5Zi00M2JmZDdkY2ZhYzYiLCJpYXQiOjE3NjE5NzkzMTAsImV4cCI6MTc2MzE4ODkxMH0.cXMgROXRTLbYQpVaipQbMnrhQCuQ620Vz_QWOcIUiKg','2025-12-01 06:41:50.790058',0,'2025-11-01 06:41:50.790058'),('4b957c5c-d33b-4376-96b0-9e2279b9f35f','1467074f-ea69-4ced-80ef-b4578304f107','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNDY3MDc0Zi1lYTY5LTRjZWQtODBlZi1iNDU3ODMwNGYxMDciLCJpYXQiOjE3NjMzODEwNjUsImV4cCI6MTc2MzQ2NzQ2NX0.-16OIEinZYQMw0fbqcDfrTkk4c2tQGFgs_-PgKDYwJA','2025-12-17 12:04:25.681104',1,'2025-11-17 12:04:25.681104'),('6411fdd9-27dc-4e70-ae9c-3a7ae8e23373','1467074f-ea69-4ced-80ef-b4578304f107','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNDY3MDc0Zi1lYTY5LTRjZWQtODBlZi1iNDU3ODMwNGYxMDciLCJpYXQiOjE3NjMzODA3NTQsImV4cCI6MTc2MzQ2NzE1NH0.ObUHlOxglYBQFrZ4CmcwpJobCKsGqjV_yiiv2EKgGyc','2025-12-17 11:59:14.750770',1,'2025-11-17 11:59:14.750770'),('b0b959f8-b544-41ea-8fe2-91a2e37a4e0c','1467074f-ea69-4ced-80ef-b4578304f107','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNDY3MDc0Zi1lYTY5LTRjZWQtODBlZi1iNDU3ODMwNGYxMDciLCJpYXQiOjE3NjMzODA2MTgsImV4cCI6MTc2MzQ2NzAxOH0.WDzvihBLGgDkF8zJyVw6Nakgua8WR39aEsEzcXr-4WA','2025-12-17 11:56:58.527011',0,'2025-11-17 11:56:58.527011'),('e7b8b746-df96-4d0c-b6e6-d995c6b03331','1467074f-ea69-4ced-80ef-b4578304f107','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNDY3MDc0Zi1lYTY5LTRjZWQtODBlZi1iNDU3ODMwNGYxMDciLCJpYXQiOjE3NjMzODA4NzAsImV4cCI6MTc2MzQ2NzI3MH0.04zk9MYGpazAQTNNXd3HzA4VEB7XZ4lJ3MiAJOOwY4M','2025-12-17 12:01:10.324333',0,'2025-11-17 12:01:10.324333');
/*!40000 ALTER TABLE `refresh_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` char(36) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password_hash` varchar(60) NOT NULL,
  `name` varchar(60) DEFAULT NULL,
  `is_email_verified` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `last_login_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_users_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('0076d05a-6a84-4f2a-bb74-884bee11e3cd','test1@example.com','$2a$10$.WVcpP0Oe3SocwQNruJdv.3SS1uehu2r4jnhb6MxvzEUMzEy5.t7C',NULL,0,'2025-11-01 05:37:48.344235','2025-11-01 05:37:48.344235',NULL),('1467074f-ea69-4ced-80ef-b4578304f107','eee@email.com','$2a$10$CfOwFPvUbeUmYdrZJZYuf.R6f.Oq2u51nga0J89rPeqO6MqIF6WaK','이름',0,'2025-11-17 11:54:13.271211','2025-11-17 12:04:25.681104','2025-11-17 12:04:25.681104'),('2faeeafb-0765-4714-a311-bcbef162f56f','hgjwilly@example.com','$2a$10$5h4yqwqrnFklmb.AsanqLuq1lTJr1KON8kxOERBcwMNmyQ7o1WJx2','hgjwilly',0,'2025-11-01 07:29:09.800375','2025-11-01 07:29:09.800375',NULL),('961eb813-98b2-4e5a-bd9f-43bfd7dcfac6','test3@example.com','$2a$10$qh6CI0qijPKw1wz4OkJpp.mRVUfqIMyzXIOBhcK9J9RdxOse7sXAO','tests',0,'2025-11-01 05:50:38.307158','2025-11-01 06:41:50.834937','2025-11-01 06:41:50.684040'),('dbfb4e00-f34c-44a5-8b12-4ff3804333d0','eeee@maver.com','$2a$10$yykjEbQSngdJHjX4brP6oOj45gkc66uEiEYG5CzTlbgFQJ09DueA6','ddd',0,'2025-11-19 03:57:29.425510','2025-11-19 03:57:29.425510',NULL),('e3b90c64-b1e2-4b2f-be4f-ac6e3047cf91','test2@example.com','$2a$10$FBotGyvd7EKPbRQH8mc6IezFahJyapAjy8mfVC/wTnu3ih/VgkgS2','tests',0,'2025-11-01 05:42:42.416130','2025-11-01 05:42:42.416130',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'homecam'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-22 21:25:15
