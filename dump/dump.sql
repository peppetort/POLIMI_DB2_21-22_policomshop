CREATE TABLE `audit_customer` (
  `id_user` int(11) NOT NULL,
  `amount` double NOT NULL,
  `last_rejection` datetime NOT NULL DEFAULT current_timestamp(),
  UNIQUE KEY `id_user_UNIQUE` (`id_user`),
  CONSTRAINT `fk_audit_customer_1` FOREIGN KEY (`id_user`) REFERENCES `customer` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  `n_payment_attemps` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `employee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `fixed_internet` (
  `id_service` int(11) NOT NULL,
  `n_gigabytes` int(11) NOT NULL,
  `fee_gigabytes` double NOT NULL,
  KEY `fk_fixed_internet_1_idx` (`id_service`),
  CONSTRAINT `fk_fixed_internet_1` FOREIGN KEY (`id_service`) REFERENCES `service` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `fixed_phone` (
  `id_service` int(11) NOT NULL,
  `n_gigabytes` int(11) NOT NULL,
  `fee_gigabytes` double NOT NULL,
  KEY `fk_fixed_phone_1_idx` (`id_service`),
  CONSTRAINT `fk_fixed_phone_1` FOREIGN KEY (`id_service`) REFERENCES `service` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `mobile_internet` (
  `id_service` int(11) NOT NULL,
  `n_gigabytes` int(11) NOT NULL,
  `fee_gigabytes` double NOT NULL,
  KEY `fk_mobile_internet_1_idx` (`id_service`),
  CONSTRAINT `fk_mobile_internet_1` FOREIGN KEY (`id_service`) REFERENCES `service` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `mobile_phone` (
  `id_service` int(11) NOT NULL,
  `n_minutes` int(11) NOT NULL,
  `fee_minutes` double NOT NULL,
  `n_sms` int(11) NOT NULL,
  `fee_sms` double NOT NULL,
  KEY `fk_mobile_phone_1_idx` (`id_service`),
  CONSTRAINT `fk_mobile_phone_1` FOREIGN KEY (`id_service`) REFERENCES `service` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `offer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_package` int(11) NOT NULL,
  `validity_period` int(11) NOT NULL,
  `monthly_fee` double NOT NULL DEFAULT 0,
  `is_active` tinyint(4) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `fk_offer_1_idx` (`id_package`),
  CONSTRAINT `fk_offer_1` FOREIGN KEY (`id_package`) REFERENCES `service_package` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `optional_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `monthly_fee` double NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `order_to_optional_product` (
  `id_order` int(11) NOT NULL,
  `id_optional_product` int(11) NOT NULL,
  KEY `fk_order_to_optional_product_1_idx` (`id_order`),
  KEY `fk_order_to_optional_product_2_idx` (`id_optional_product`),
  CONSTRAINT `fk_order_to_optional_product_1` FOREIGN KEY (`id_order`) REFERENCES `order` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_order_to_optional_product_2` FOREIGN KEY (`id_optional_product`) REFERENCES `optional_product` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `service_package` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `service_package_to_optional_product` (
  `id_service_package` int(11) NOT NULL,
  `id_optional_product` int(11) DEFAULT NULL,
  KEY `fk_service_package_to_optional_product_1_idx` (`id_service_package`),
  KEY `fk_service_package_to_optional_product_2_idx` (`id_optional_product`),
  CONSTRAINT `fk_service_package_to_optional_product_1` FOREIGN KEY (`id_service_package`) REFERENCES `service_package` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fk_service_package_to_optional_product_2` FOREIGN KEY (`id_optional_product`) REFERENCES `optional_product` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `service_package_to_service` (
  `id_package` int(11) NOT NULL,
  `id_service` int(11) NOT NULL,
  KEY `fk_service_package_to_service_1_idx` (`id_package`),
  KEY `fk_service_package_to_service_2_idx` (`id_service`),
  CONSTRAINT `fk_service_package_to_service_1` FOREIGN KEY (`id_package`) REFERENCES `service_package` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_service_package_to_service_2` FOREIGN KEY (`id_service`) REFERENCES `service` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

