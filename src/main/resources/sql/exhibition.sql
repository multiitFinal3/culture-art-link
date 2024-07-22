"CREATE TABLE `exhibition_analyze` (
  `id` int NOT NULL AUTO_INCREMENT,
  `exhibition_id` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `artwork` varchar(100) NOT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `date` datetime NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `exhibition_id` (`exhibition_id`),
  CONSTRAINT `exhibition_analyze_ibfk_1` FOREIGN KEY (`exhibition_id`) REFERENCES `exhibition` (`id`)
);"
"CREATE TABLE `exhibition_comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `exhibition_id` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `stars` double NOT NULL,
  `date` datetime NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `exhibition_id` (`exhibition_id`),
  CONSTRAINT `exhibition_comment_ibfk_1` FOREIGN KEY (`exhibition_id`) REFERENCES `exhibition` (`id`)
);"
"CREATE TABLE `exhibition_comment_keyword` (
  `id` int NOT NULL AUTO_INCREMENT,
  `exhibition_id` int NOT NULL,
  `keyword` varchar(100) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `genre` enum('festival','cultural_properties','exhibition','performance','event') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `exhibition_id` (`exhibition_id`),
  CONSTRAINT `exhibition_comment_keyword_ibfk_1` FOREIGN KEY (`exhibition_id`) REFERENCES `exhibition` (`id`)
);"
"CREATE TABLE `exhibition_interested` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `exhibition_id` int NOT NULL,
  `state` enum('interested','not_interested') NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `exhibition_id` (`exhibition_id`),
  CONSTRAINT `exhibition_interested_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `exhibition_interested_ibfk_2` FOREIGN KEY (`exhibition_id`) REFERENCES `exhibition` (`id`)
);"
"CREATE TABLE `exhibition` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `artist` varchar(255) DEFAULT NULL,
  `museum` varchar(100) NOT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `price` varchar(100) DEFAULT NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `sub_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);"
"CREATE TABLE `exhibition_interested_keyword` (
  `id` int NOT NULL AUTO_INCREMENT,
  `exhibition_id` int NOT NULL,
  `keyword` varchar(100) NOT NULL,
  `count` int NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `genre` enum('festival','cultural_properties','exhibition','performance','event') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `exhibition_id` (`exhibition_id`),
  CONSTRAINT `exhibition_interested_keyword_ibfk_1` FOREIGN KEY (`exhibition_id`) REFERENCES `exhibition` (`id`)
);"
"CREATE TABLE `board` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `title` varchar(100) NOT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `date` datetime NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `genre` enum('festival','cultural_properties','exhibition','performance','event') NOT NULL,
  PRIMARY KEY (`id`)
);"
"CREATE TABLE `board_comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_id` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `date` datetime NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `board_id` (`board_id`),
  CONSTRAINT `board_comment_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`)
);"