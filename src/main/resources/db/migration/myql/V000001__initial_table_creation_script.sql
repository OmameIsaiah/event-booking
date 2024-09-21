CREATE TABLE IF NOT EXISTS `users_table` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date_created` datetime(6) DEFAULT NULL,
  `last_modified` datetime(6) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `is_online` bit(1) DEFAULT NULL,
  `last_login` datetime(6) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `onboarding_stage` varchar(255) DEFAULT NULL,
  `otp_code` varchar(255) DEFAULT NULL,
  `otp_expire_time` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `user_token` varchar(255) DEFAULT NULL,
  `user_type` varchar(255) DEFAULT NULL,
  `verified` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date_created` datetime(6) DEFAULT NULL,
  `last_modified` datetime(6) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `permissions` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date_created` datetime(6) DEFAULT NULL,
  `last_modified` datetime(6) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `roleid` bigint DEFAULT NULL,
  `userrole` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKss07htsrasc17qsq2o9422nyh` (`roleid`),
  KEY `FK9vb9b0eakvmg912uiyvfg5on8` (`userrole`),
  CONSTRAINT `FK9vb9b0eakvmg912uiyvfg5on8` FOREIGN KEY (`userrole`) REFERENCES `users_table` (`id`),
  CONSTRAINT `FKss07htsrasc17qsq2o9422nyh` FOREIGN KEY (`roleid`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `event` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date_created` datetime(6) DEFAULT NULL,
  `last_modified` datetime(6) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `available_attendees_count` int NOT NULL,
  `category` varchar(255) DEFAULT NULL,
  `description` text,
  `event_date` datetime(6) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `user_event` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date_created` datetime(6) DEFAULT NULL,
  `last_modified` datetime(6) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `attendees_count` int NOT NULL,
  `eventid` bigint DEFAULT NULL,
  `userevent` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4w2e77q9ih94m2rkhhvbvrp6x` (`eventid`),
  KEY `FKf5foeefltn4x3y408gfun9py7` (`userevent`),
  CONSTRAINT `FK4w2e77q9ih94m2rkhhvbvrp6x` FOREIGN KEY (`eventid`) REFERENCES `event` (`id`),
  CONSTRAINT `FKf5foeefltn4x3y408gfun9py7` FOREIGN KEY (`userevent`) REFERENCES `users_table` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


INSERT IGNORE INTO event_db.roles(id,date_created,last_modified,uuid,description,name,permissions)
VALUES(0,'2024-06-5 10:30:10','2024-06-5 10:30:10',"18b56dc6-9813-440a-8c82-baf2eb10f264",'Role for administrators','ROLE_ADMIN','["CAN_VIEW_EVENTS","CAN_SEARCH_EVENTS","CAN_RESERVE_EVENT_TICKET","CAN_CREATE_EVENT","CAN_UPDATE_EVENT","CAN_DELETE_EVENT","CAN_VIEW_ALL_RESERVATIONS","CAN_SEND_EVENT_NOTIFICATION","CAN_VIEW_USERS","CAN_DELETE_USERS","CAN_ADD_ROLE","CAN_UPDATE_ROLE","CAN_DELETE_ROLE"]');

INSERT IGNORE INTO `event_db`.`roles`(`id`,`date_created`,`last_modified`,`uuid`,`description`,`name`,`permissions`)
VALUES(0,'2024-06-5 10:30:10','2024-06-5 10:30:10',"cae15553-1148-49c1-8e9d-f3dea70fb4d1",'Role for users','ROLE_USER','["CAN_VIEW_EVENTS","CAN_SEARCH_EVENTS","CAN_RESERVE_EVENT_TICKET"]');


