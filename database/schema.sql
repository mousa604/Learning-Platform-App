-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: learning_platform
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Backend Development','Server-side programming and APIs','2026-10-14 12:05:13','2026-10-14 12:05:13'),(2,'Frontend Development','Building user interfaces for the web','2026-10-14 12:05:13','2026-10-14 12:05:13'),(3,'Mobile Development','Building mobile applications','2026-10-14 12:05:13','2026-10-14 12:05:13'),(4,'Artificial Intelligence','AI concepts and applications','2026-10-14 12:05:13','2026-10-14 12:05:13'),(5,'Data Science','Data analysis and scientific computing','2026-10-14 12:05:13','2026-10-14 12:05:13'),(6,'Database','Database design and management','2026-10-14 12:05:13','2026-10-14 12:05:13'),(7,'Cybersecurity','Security fundamentals and best practices','2026-10-14 12:05:13','2026-10-14 12:05:13'),(8,'Cloud Computing','Cloud infrastructure and services','2026-10-14 12:05:13','2026-10-14 12:05:13'),(9,'DevOps','Development and operations practices','2026-10-14 12:05:13','2026-10-14 12:05:13'),(10,'Software Engineering','Software design and engineering','2026-10-14 12:05:13','2026-10-14 12:05:13'),(11,'Computer Networks','Networking fundamentals','2026-10-14 12:05:13','2026-10-14 12:05:13'),(12,'Operating Systems','Operating systems concepts','2026-10-14 12:05:13','2026-10-14 12:05:13'),(13,'Algorithms','Algorithms and problem solving','2026-10-14 12:05:13','2026-10-14 12:05:13'),(14,'Game Development','Game programming and design','2026-10-14 12:05:13','2026-10-14 12:05:13'),(15,'UI UX Design','User interface and experience design','2026-10-14 12:05:13','2026-10-14 12:05:13'),(16,'Python Programming','Python programming fundamentals','2026-10-14 12:05:13','2026-10-14 12:05:13'),(17,'Java Programming','Java programming fundamentals','2026-10-14 12:05:13','2026-10-14 12:05:13'),(18,'C++ Programming','C++ programming and OOP','2026-10-14 12:05:13','2026-10-14 12:05:13'),(19,'Machine Learning','Machine learning algorithms and models','2026-10-14 12:05:13','2026-10-14 12:05:13'),(20,'Web Development','Full-stack web development','2026-10-14 12:05:13','2026-10-14 12:05:13');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `certificates`
--

DROP TABLE IF EXISTS `certificates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `certificates` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `course_id` bigint NOT NULL,
  `certificate_number` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `issued_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `file_path` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_certificate_number` (`certificate_number`),
  UNIQUE KEY `uk_student_course_certificate` (`student_id`,`course_id`),
  KEY `fk_certificate_course` (`course_id`),
  CONSTRAINT `fk_certificate_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `fk_certificate_student` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `certificates`
--

LOCK TABLES `certificates` WRITE;
/*!40000 ALTER TABLE `certificates` DISABLE KEYS */;
INSERT INTO `certificates` VALUES (8,7,1,'0d3c9d8d-305d-4d52-933d-f772fcc8afa2','2026-10-27 17:25:01','2026-10-27 17:25:01','2026-10-27 17:25:01','uploads\\certificates\\973d5784-ccdd-4af5-927a-9ead67856c42.pdf');
/*!40000 ALTER TABLE `certificates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_tags`
--

DROP TABLE IF EXISTS `course_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_tags` (
  `course_id` bigint NOT NULL,
  `tag_id` bigint NOT NULL,
  PRIMARY KEY (`course_id`,`tag_id`),
  KEY `fk_course_tags_tag` (`tag_id`),
  CONSTRAINT `fk_course_tags_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_course_tags_tag` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_tags`
--

LOCK TABLES `course_tags` WRITE;
/*!40000 ALTER TABLE `course_tags` DISABLE KEYS */;
/*!40000 ALTER TABLE `course_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `price` decimal(10,2) NOT NULL DEFAULT '0.00',
  `level` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DRAFT',
  `instructor_id` bigint NOT NULL,
  `category_id` bigint NOT NULL,
  `thumbnail` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `published_at` timestamp NULL DEFAULT NULL,
  `rejection_reason` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_courses_instructor` (`instructor_id`),
  KEY `fk_courses_category` (`category_id`),
  CONSTRAINT `fk_courses_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  CONSTRAINT `fk_courses_instructor` FOREIGN KEY (`instructor_id`) REFERENCES `users` (`id`),
  CONSTRAINT `chk_course_level` CHECK ((`level` in (_utf8mb4'BEGINNER',_utf8mb4'INTERMEDIATE',_utf8mb4'ADVANCED'))),
  CONSTRAINT `chk_course_price` CHECK ((`price` >= 0)),
  CONSTRAINT `chk_course_status` CHECK ((`status` in (_utf8mb4'DRAFT',_utf8mb4'PENDING_REVIEW',_utf8mb4'PUBLISHED',_utf8mb4'REJECTED',_utf8mb4'ARCHIVED')))
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (1,'Spring Boot REST API','Build REST APIs using Spring Boot and JPA',500.00,'INTERMEDIATE','PUBLISHED',2,1,'spring-boot.jpg','2026-10-14 12:06:08','2026-10-15 15:41:22','2026-10-14 12:06:08',NULL),(2,'Java Programming Fundamentals','Learn Java syntax, OOP and core concepts',300.00,'BEGINNER','PUBLISHED',2,17,'java-fundamentals.jpg','2026-10-14 12:06:08','2026-10-14 12:06:08','2026-10-14 12:06:08',NULL),(3,'Advanced Java OOP','Master inheritance, polymorphism and abstraction',450.00,'INTERMEDIATE','PUBLISHED',2,17,'java-oop.jpg','2026-10-14 12:06:08','2026-10-14 12:06:08','2026-10-14 12:06:08',NULL),(4,'HTML CSS and JavaScript','Create interactive websites from scratch',350.00,'BEGINNER','PUBLISHED',3,2,'html-css-js.jpg','2026-10-14 12:06:08','2026-10-14 12:06:08','2026-10-14 12:06:08',NULL),(5,'React Frontend Development','Build reusable interfaces using React',600.00,'INTERMEDIATE','PUBLISHED',3,2,'react.jpg','2026-10-14 12:06:08','2026-10-14 12:06:08','2026-10-14 12:06:08',NULL),(6,'Android App Development','Build Android applications step by step',550.00,'INTERMEDIATE','PUBLISHED',20,3,'android.jpg','2026-10-14 12:06:08','2026-10-14 12:06:08','2026-10-14 12:06:08',NULL),(7,'Python for Beginners','Learn programming using Python',250.00,'BEGINNER','PUBLISHED',4,16,'python.jpg','2026-10-14 12:06:08','2026-10-14 12:06:08','2026-10-14 12:06:08',NULL),(8,'Machine Learning Fundamentals','Understand machine learning concepts',700.00,'INTERMEDIATE','PUBLISHED',4,19,'machine-learning.jpg','2026-10-14 12:06:08','2026-10-14 12:06:08','2026-10-14 12:06:08',NULL),(9,'Artificial Intelligence Basics','Explore AI concepts and practical examples',650.00,'BEGINNER','PUBLISHED',4,4,'ai-basics.jpg','2026-10-14 12:06:08','2026-10-14 12:06:08','2026-10-14 12:06:08',NULL),(10,'SQL and Database Design','Design relational databases and write SQL',400.00,'BEGINNER','PUBLISHED',2,6,'sql-database.jpg','2026-10-14 12:06:08','2026-10-14 12:06:08','2026-10-14 12:06:08',NULL),(11,'Data Science with Python','Analyze data using Python libraries',750.00,'ADVANCED','PUBLISHED',4,5,'data-science.jpg','2026-10-14 12:06:08','2026-10-14 12:06:08','2026-10-14 12:06:08',NULL),(12,'Cybersecurity Essentials','Learn security principles and common threats',500.00,'BEGINNER','PUBLISHED',20,7,'cybersecurity.jpg','2026-10-14 12:06:08','2026-10-14 12:06:08','2026-10-14 12:06:08',NULL),(13,'Docker and DevOps','Learn containers and DevOps fundamentals',800.00,'ADVANCED','PUBLISHED',20,9,'docker-devops.jpg','2026-10-14 12:06:08','2026-10-14 12:06:08','2026-10-14 12:06:08',NULL),(14,'Data Structures and Algorithms','Practice data structures and algorithms',450.00,'INTERMEDIATE','PUBLISHED',2,13,'dsa.jpg','2026-10-14 12:06:08','2026-10-14 12:06:08','2026-10-14 12:06:08',NULL),(15,'Full Stack Web Development','Build complete web applications',900.00,'ADVANCED','PUBLISHED',20,20,'full-stack.jpg','2026-10-14 12:06:08','2026-10-14 12:06:08','2026-10-14 12:06:08',NULL),(16,'Kubernetes Fundamentals','Introduction to container orchestration',850.00,'ADVANCED','REJECTED',20,9,'kubernetes.jpg','2026-10-14 12:06:08','2026-10-19 07:50:50',NULL,'The course is missing important learning objectives.'),(17,'Spring Security','Authentication and authorization with Spring',650.00,'ADVANCED','DRAFT',2,1,'spring-security.jpg','2026-10-14 12:06:08','2026-10-14 12:06:08',NULL,NULL),(18,'Cloud Computing Essentials','Introduction to cloud architecture',700.00,'INTERMEDIATE','PUBLISHED',20,8,'cloud.jpg','2026-10-14 12:06:08','2026-10-19 07:46:53','2026-10-19 07:46:53',NULL),(19,'Legacy Java Course','An archived Java programming course',200.00,'BEGINNER','ARCHIVED',2,17,'legacy-java.jpg','2026-10-14 12:06:08','2026-10-14 12:06:08',NULL,NULL),(20,'C++ Programming','Learn C++ and object-oriented programming',350.00,'BEGINNER','REJECTED',2,18,'cpp.jpg','2026-10-14 12:06:08','2026-10-14 12:06:08',NULL,NULL),(21,'Spring Boot REST API','Learn how to build REST APIs using Spring Boot, Spring Data JPA, and MySQL.',499.99,'BEGINNER','DRAFT',1,1,'https://example.com/images/spring-boot.jpg','2026-10-25 15:42:02','2026-10-25 15:42:02',NULL,NULL);
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enrollments`
--

DROP TABLE IF EXISTS `enrollments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enrollments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `course_id` bigint NOT NULL,
  `enrolled_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `completed_at` timestamp NULL DEFAULT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_student_course` (`student_id`,`course_id`),
  KEY `fk_enrollments_course` (`course_id`),
  CONSTRAINT `fk_enrollments_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `fk_enrollments_student` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`),
  CONSTRAINT `chk_enrollment_status` CHECK ((`status` in (_utf8mb4'ACTIVE',_utf8mb4'COMPLETED',_utf8mb4'CANCELLED')))
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enrollments`
--

LOCK TABLES `enrollments` WRITE;
/*!40000 ALTER TABLE `enrollments` DISABLE KEYS */;
INSERT INTO `enrollments` VALUES (1,1,1,'2026-10-14 12:07:03',NULL,'ACTIVE'),(2,6,1,'2026-10-14 12:07:03',NULL,'ACTIVE'),(3,7,1,'2026-10-14 12:07:03',NULL,'ACTIVE'),(4,8,2,'2026-10-14 12:07:03',NULL,'ACTIVE'),(5,9,2,'2026-10-14 12:07:03',NULL,'ACTIVE'),(6,10,3,'2026-10-14 12:07:03','2026-10-14 12:07:03','COMPLETED'),(7,11,4,'2026-10-14 12:07:03',NULL,'ACTIVE'),(8,12,4,'2026-10-14 12:07:03','2026-10-14 12:07:03','COMPLETED'),(9,13,5,'2026-10-14 12:07:03',NULL,'ACTIVE'),(10,14,5,'2026-10-14 12:07:03',NULL,'ACTIVE'),(11,15,6,'2026-10-14 12:07:03',NULL,'ACTIVE'),(12,16,6,'2026-10-14 12:07:03','2026-10-14 12:07:03','COMPLETED'),(13,17,7,'2026-10-14 12:07:03',NULL,'ACTIVE'),(14,18,8,'2026-10-14 12:07:03',NULL,'ACTIVE'),(15,19,8,'2026-10-14 12:07:03',NULL,'ACTIVE'),(16,1,10,'2026-10-14 12:07:03','2026-10-14 12:07:03','COMPLETED'),(17,6,10,'2026-10-14 12:07:03',NULL,'ACTIVE'),(18,7,11,'2026-10-14 12:07:03',NULL,'ACTIVE'),(19,9,14,'2026-10-14 12:07:03',NULL,'ACTIVE'),(20,12,15,'2026-10-14 12:07:03',NULL,'ACTIVE'),(21,12,1,'2026-10-15 16:22:42',NULL,'CANCELLED'),(22,15,1,'2026-10-18 17:01:17',NULL,'ACTIVE'),(23,21,1,'2026-10-26 09:15:21',NULL,'CANCELLED'),(24,7,2,'2026-10-30 13:18:57',NULL,'ACTIVE');
/*!40000 ALTER TABLE `enrollments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lesson_progress`
--

DROP TABLE IF EXISTS `lesson_progress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lesson_progress` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `lesson_id` bigint NOT NULL,
  `completed` tinyint(1) NOT NULL DEFAULT '0',
  `completed_at` datetime DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_lesson_progress_user_lesson` (`user_id`,`lesson_id`),
  KEY `fk_lesson_progress_lesson` (`lesson_id`),
  CONSTRAINT `fk_lesson_progress_lesson` FOREIGN KEY (`lesson_id`) REFERENCES `lessons` (`id`),
  CONSTRAINT `fk_lesson_progress_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lesson_progress`
--

LOCK TABLES `lesson_progress` WRITE;
/*!40000 ALTER TABLE `lesson_progress` DISABLE KEYS */;
INSERT INTO `lesson_progress` VALUES (1,1,1,1,'2026-10-27 16:53:13','2026-10-27 13:53:13','2026-10-27 13:53:13'),(2,1,2,1,'2026-10-27 16:53:21','2026-10-27 13:53:21','2026-10-27 13:53:21'),(3,1,41,1,'2026-10-27 17:45:33','2026-10-27 14:45:33','2026-10-27 14:45:33'),(4,7,1,1,'2026-10-27 19:56:47','2026-10-27 16:56:47','2026-10-27 16:56:47'),(5,7,2,1,'2026-10-27 19:56:56','2026-10-27 16:56:56','2026-10-27 16:56:56'),(6,7,41,1,'2026-10-27 19:57:05','2026-10-27 16:57:05','2026-10-27 16:57:05');
/*!40000 ALTER TABLE `lesson_progress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lessons`
--

DROP TABLE IF EXISTS `lessons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lessons` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `video_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `duration` int DEFAULT NULL,
  `lesson_order` int NOT NULL,
  `course_id` bigint NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_course_lesson_order` (`course_id`,`lesson_order`),
  CONSTRAINT `fk_lessons_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `chk_lesson_duration` CHECK (((`duration` is null) or (`duration` > 0))),
  CONSTRAINT `chk_lesson_order` CHECK ((`lesson_order` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lessons`
--

LOCK TABLES `lessons` WRITE;
/*!40000 ALTER TABLE `lessons` DISABLE KEYS */;
INSERT INTO `lessons` VALUES (1,'Introduction to Spring Boot','Spring Boot overview','https://example.com/videos/1',20,1,1,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(2,'Creating REST Controllers','Build your first REST endpoint','https://example.com/videos/2',35,2,1,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(3,'Java Syntax','Variables and data types','https://example.com/videos/3',25,1,2,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(4,'Control Flow','Conditions and loops','https://example.com/videos/4',30,2,2,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(5,'Classes and Objects','Introduction to Java classes','https://example.com/videos/5',30,1,3,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(6,'Inheritance and Polymorphism','Advanced OOP concepts','https://example.com/videos/6',40,2,3,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(7,'HTML Structure','Create your first webpage','https://example.com/videos/7',25,1,4,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(8,'CSS Styling','Style webpages with CSS','https://example.com/videos/8',35,2,4,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(9,'React Components','Understand React components','https://example.com/videos/9',30,1,5,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(10,'React State','Manage component state','https://example.com/videos/10',40,2,5,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(11,'Android Studio Setup','Prepare the development environment','https://example.com/videos/11',20,1,6,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(12,'Building Your First App','Create a simple mobile app','https://example.com/videos/12',45,2,6,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(13,'Python Variables','Variables and basic types','https://example.com/videos/13',20,1,7,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(14,'Python Functions','Write reusable functions','https://example.com/videos/14',30,2,7,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(15,'What Is Machine Learning','ML concepts and terminology','https://example.com/videos/15',25,1,8,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(16,'Training Your First Model','Build a simple ML model','https://example.com/videos/16',45,2,8,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(17,'Introduction to AI','AI concepts and use cases','https://example.com/videos/17',25,1,9,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(18,'AI Applications','Examples of practical AI','https://example.com/videos/18',35,2,9,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(19,'Relational Databases','Tables and relationships','https://example.com/videos/19',30,1,10,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(20,'SQL Queries','SELECT, WHERE and JOIN','https://example.com/videos/20',40,2,10,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(21,'Data Cleaning','Prepare datasets for analysis','https://example.com/videos/21',35,1,11,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(22,'Data Visualization','Visualize data insights','https://example.com/videos/22',45,2,11,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(23,'Security Fundamentals','Core cybersecurity principles','https://example.com/videos/23',25,1,12,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(24,'Common Web Threats','Understand common security risks','https://example.com/videos/24',35,2,12,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(25,'Docker Images','Understand container images','https://example.com/videos/25',30,1,13,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(26,'Docker Containers','Run and manage containers','https://example.com/videos/26',40,2,13,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(27,'Arrays and Linked Lists','Introduction to data structures','https://example.com/videos/27',35,1,14,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(28,'Searching and Sorting','Common algorithms','https://example.com/videos/28',45,2,14,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(29,'Backend API Design','Design REST endpoints','https://example.com/videos/29',35,1,15,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(30,'Frontend Integration','Connect frontend and backend','https://example.com/videos/30',45,2,15,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(31,'Kubernetes Concepts','Pods and clusters','https://example.com/videos/31',30,1,16,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(32,'Deploying Containers','Basic deployment concepts','https://example.com/videos/32',40,2,16,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(33,'Authentication Basics','Authentication concepts','https://example.com/videos/33',30,1,17,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(34,'Authorization Rules','Roles and endpoint access','https://example.com/videos/34',45,2,17,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(35,'Cloud Architecture','Introduction to cloud systems','https://example.com/videos/35',30,1,18,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(36,'Cloud Deployment','Deploying an application','https://example.com/videos/36',45,2,18,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(37,'Legacy Java Syntax','Review older Java concepts','https://example.com/videos/37',20,1,19,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(38,'Legacy OOP','Review object-oriented programming','https://example.com/videos/38',30,2,19,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(39,'C++ Variables','C++ types and variables','https://example.com/videos/39',25,1,20,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(40,'C++ Classes','Introduction to C++ OOP','https://example.com/videos/40',40,2,20,'2026-10-14 12:06:42','2026-10-14 12:06:42'),(41,'Introduction to Spring Boot','Learn the basics of Spring Boot','https://example.com/lesson-1',80,3,1,'2026-10-15 15:13:25','2026-10-15 15:26:54');
/*!40000 ALTER TABLE `lessons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `title` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `message` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_read` tinyint(1) NOT NULL DEFAULT '0',
  `read_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_notifications_user` (`user_id`),
  CONSTRAINT `fk_notifications_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (1,7,'Course Enrollment Successful','You have successfully enrolled in the course: Java Programming Fundamentals','COURSE_ENROLLED',0,NULL,'2026-10-30 15:18:57','2026-10-30 15:18:57');
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `course_id` bigint NOT NULL,
  `rating` int NOT NULL,
  `comment` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_student_course_review` (`student_id`,`course_id`),
  KEY `fk_reviews_course` (`course_id`),
  CONSTRAINT `fk_reviews_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `fk_reviews_student` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`),
  CONSTRAINT `chk_review_rating` CHECK ((`rating` between 1 and 5))
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (1,1,1,4,'Good course, with useful practical examples.','2026-10-14 12:07:23','2026-10-18 17:03:37'),(2,6,1,4,'Very useful and easy to follow.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(3,7,1,5,'Helped me understand REST APIs.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(4,8,2,4,'Good introduction to Java.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(5,9,2,5,'Clear explanations and examples.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(6,10,3,5,'Great explanation of OOP.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(7,11,4,4,'Useful HTML and CSS lessons.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(8,12,4,5,'The examples were easy to understand.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(9,13,5,4,'Good React introduction.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(10,14,5,5,'Practical and well organized.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(11,15,6,4,'Helpful mobile development content.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(12,16,6,5,'I completed the course successfully.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(13,17,7,4,'Python basics explained clearly.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(14,18,8,5,'Good introduction to machine learning.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(15,19,8,4,'Useful ML examples.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(16,1,10,5,'The SQL examples were very helpful.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(17,6,10,4,'Good database fundamentals.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(18,7,11,5,'Great data science introduction.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(19,9,14,4,'Good algorithm practice.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(20,12,15,5,'Useful full-stack concepts.','2026-10-14 12:07:23','2026-10-14 12:07:23'),(21,15,1,5,'Excellent course. The explanations are clear.','2026-10-18 17:01:39','2026-10-18 17:01:39');
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'STUDENT','2026-10-14 12:04:00','2026-10-14 12:04:00'),(2,'INSTRUCTOR','2026-10-14 12:04:00','2026-10-14 12:04:00'),(3,'ADMIN','2026-10-14 12:04:00','2026-10-14 12:04:00');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tags` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tags_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `fk_user_roles_role` (`role_id`),
  CONSTRAINT `fk_user_roles_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `fk_user_roles_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (1,1),(2,1),(6,1),(7,1),(8,1),(9,1),(10,1),(11,1),(12,1),(13,1),(14,1),(15,1),(16,1),(17,1),(18,1),(19,1),(21,1),(22,1),(1,2),(2,2),(3,2),(4,2),(6,2),(20,2),(21,2),(5,3);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bio` text COLLATE utf8mb4_unicode_ci,
  `profile_image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Mohamed','Mousa','mohamed@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01000000000','Java Backend Developer','https://example.com/profile.jpg',1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(2,'Ahmed','Ali','ahmed@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000002','Backend instructor',NULL,0,'2026-10-14 12:04:23','2026-10-26 08:41:02'),(3,'Sara','Hassan','sara@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000003','Frontend instructor',NULL,1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(4,'Omar','Khaled','omar@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000004','AI instructor',NULL,1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(5,'Admin','User','admin@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000005','Platform administrator',NULL,1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(6,'Youssef','Mahmoud','youssef@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000006','Interested in backend development',NULL,1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(7,'Mariam','Samir','mariam@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000007','Interested in web development',NULL,1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(8,'Ali','Mostafa','ali@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000008','Mobile developer learner',NULL,1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(9,'Nour','Ibrahim','nour@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000009','Data science learner',NULL,1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(10,'Salma','Adel','salma@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000010','AI learner',NULL,1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(11,'Hassan','Fathy','hassan@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000011','Database learner',NULL,1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(12,'Farah','Tarek','farah@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000012','Software engineering learner',NULL,1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(13,'Ziad','Ashraf','ziad@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000013','Java developer learner',NULL,1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(14,'Malak','Ayman','malak@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000014','Frontend learner',NULL,1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(15,'Karim','Wael','karim@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000015','Backend learner',NULL,1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(16,'Jana','Hany','jana@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000016','Mobile development learner',NULL,1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(17,'Adam','Sherif','adam@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000017','AI and machine learning learner',NULL,1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(18,'Laila','Emad','laila@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000018','Data analytics learner',NULL,1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(19,'Amr','Reda','amr@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000019','Software development learner',NULL,1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(20,'Mina','George','mina@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01010000020','Full-stack instructor',NULL,1,'2026-10-14 12:04:23','2026-10-25 15:30:58'),(21,'Mohamed','Emad','mohamed.new@test.com','$2a$10$D9WK7IFNo2b5HBf5Blex1e3TyZUAMkXDW6vyvkgcZ0j/IvUMm54QK','01012345678','Java Backend Developer',NULL,1,'2026-10-23 13:47:46','2026-10-25 15:34:19'),(22,'Nour','Test','nour.test@example.com','$2a$10$OfiyDI3hSSZTPOe1L3TaAOS7/7dxmOasGWVhIZfY04AomRQy3/On.',NULL,NULL,NULL,1,'2026-10-26 10:06:58','2026-10-26 10:06:58');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-30 18:37:22
