-- ----------------------------
--  Table structure for `ym_wechat_account`
-- ----------------------------
DROP TABLE IF EXISTS `ym_wechat_account`;
CREATE TABLE `ym_wechat_account` (
  `id` varchar(32) NOT NULL,
  `name` varchar(50) NOT NULL,
  `account_id` varchar(32) NOT NULL,
  `app_id` varchar(50) NOT NULL,
  `app_secret` varchar(100) NOT NULL,
  `app_aes_key` varchar(50) DEFAULT NULL,
  `last_app_aes_key` varchar(50) DEFAULT NULL,
  `redirect_uri` varchar(255) DEFAULT NULL,
  `token` varchar(32) NOT NULL,
  `type` smallint(2) unsigned DEFAULT '0',
  `is_verified` smallint(1) unsigned DEFAULT '0',
  `is_deleted` smallint(1) unsigned DEFAULT '0',
  `is_default` smallint(1) unsigned DEFAULT '0',
  `is_msg_encrypted` smallint(1) unsigned DEFAULT '0',
  `status` smallint(2) unsigned DEFAULT '0',
  `site_id` varchar(32) NOT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `create_time` bigint(13) NOT NULL,
  `last_modify_time` bigint(13) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `ym_wechat_account_attribute`
-- ----------------------------
DROP TABLE IF EXISTS `ym_wechat_account_attribute`;
CREATE TABLE `ym_wechat_account_attribute` (
  `id` varchar(32) NOT NULL,
  `account_id` varchar(32) NOT NULL,
  `attr_key` varchar(255) NOT NULL,
  `attr_value` text,
  `type` smallint(2) unsigned DEFAULT '0',
  `owner` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `ym_wechat_location`
-- ----------------------------
DROP TABLE IF EXISTS `ym_wechat_location`;
CREATE TABLE `ym_wechat_location` (
  `id` varchar(32) NOT NULL,
  `account_id` varchar(32) NOT NULL,
  `wx_uid` varchar(32) NOT NULL,
  `location_lon` bigint(20) NOT NULL,
  `location_lat` bigint(20) NOT NULL,
  `precision` bigint(20) NOT NULL,
  `label` varchar(100) DEFAULT NULL,
  `create_time` bigint(13) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `ym_wechat_message`
-- ----------------------------
DROP TABLE IF EXISTS `ym_wechat_message`;
CREATE TABLE `ym_wechat_message` (
  `id` varchar(32) NOT NULL,
  `from_uid` varchar(32) NOT NULL,
  `to_uid` varchar(32) NOT NULL,
  `content` mediumtext NOT NULL,
  `is_star` smallint(1) unsigned DEFAULT '0',
  `is_replied` smallint(1) unsigned DEFAULT '0',
  `is_autoreply` smallint(1) unsigned DEFAULT '0',
  `is_read` smallint(1) unsigned DEFAULT '0',
  `is_deleted` smallint(1) unsigned DEFAULT '0',
  `session_flag` varchar(32) DEFAULT NULL,
  `type` smallint(2) unsigned DEFAULT '0',
  `status` smallint(2) unsigned DEFAULT '0',
  `idx_status` smallint(2) unsigned DEFAULT '0',
  `create_time` bigint(13) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `ym_wechat_scan`
-- ----------------------------
DROP TABLE IF EXISTS `ym_wechat_scan`;
CREATE TABLE `ym_wechat_scan` (
  `id` varchar(32) NOT NULL,
  `account_id` varchar(32) NOT NULL,
  `wx_uid` varchar(256) NOT NULL,
  `scene` varchar(64) NOT NULL,
  `ticket` varchar(256) DEFAULT NULL,
  `hits` bigint(20) DEFAULT '0',
  `create_time` bigint(13) NOT NULL,
  `last_modify_time` bigint(13) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `ym_wechat_user`
-- ----------------------------
DROP TABLE IF EXISTS `ym_wechat_user`;
CREATE TABLE `ym_wechat_user` (
  `id` varchar(32) NOT NULL,
  `account_id` varchar(32) NOT NULL,
  `open_id` varchar(256) NOT NULL,
  `union_id` varchar(32) DEFAULT NULL,
  `nick_name` varchar(32) DEFAULT NULL,
  `gender` smallint(1) unsigned DEFAULT '0',
  `head_img_url` varchar(255) DEFAULT NULL,
  `signature` varchar(100) DEFAULT NULL,
  `country` varchar(32) DEFAULT NULL,
  `province` varchar(32) DEFAULT NULL,
  `city` varchar(32) DEFAULT NULL,
  `language` varchar(10) DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL,
  `is_subscribe` smallint(1) unsigned DEFAULT '0',
  `subscribe_time` bigint(13) DEFAULT '0',
  `unsubscribe_time` bigint(13) DEFAULT '0',
  `tag_id_list` varchar(32) DEFAULT NULL,
  `status` smallint(2) unsigned DEFAULT '0',
  `create_time` bigint(13) NOT NULL,
  `last_modify_time` bigint(13) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `ym_wechat_user_attribute`
-- ----------------------------
DROP TABLE IF EXISTS `ym_wechat_user_attribute`;
CREATE TABLE `ym_wechat_user_attribute` (
  `id` varchar(32) NOT NULL,
  `wx_id` varchar(32) NOT NULL,
  `attr_key` varchar(255) NOT NULL,
  `attr_value` text,
  `type` smallint(2) unsigned DEFAULT '0',
  `owner` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;