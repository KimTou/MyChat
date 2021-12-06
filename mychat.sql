/*
Navicat MySQL Data Transfer

Source Server         : 本地mysql
Source Server Version : 50727
Source Host           : localhost:3306
Source Database       : mychat

Target Server Type    : MYSQL
Target Server Version : 50727
File Encoding         : 65001

Date: 2021-07-07 15:09:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for friend
-- ----------------------------
DROP TABLE IF EXISTS `friend`;
CREATE TABLE `friend` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `friend_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of friend
-- ----------------------------
INSERT INTO `friend` VALUES ('1', '1', '2');
INSERT INTO `friend` VALUES ('2', '3', '1');

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of group
-- ----------------------------
INSERT INTO `group` VALUES ('1', '小分队');
INSERT INTO `group` VALUES ('4', '冲冲冲群');

-- ----------------------------
-- Table structure for group_message
-- ----------------------------
DROP TABLE IF EXISTS `group_message`;
CREATE TABLE `group_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `sender` int(255) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `gmt_create` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of group_message
-- ----------------------------
INSERT INTO `group_message` VALUES ('16', '1', '1', '你们好呀\n', '2021-06-21 13:25:07');
INSERT INTO `group_message` VALUES ('17', '1', '3', '你好', '2021-06-21 13:25:18');
INSERT INTO `group_message` VALUES ('18', '1', '4', '大家都好', '2021-06-21 13:25:29');
INSERT INTO `group_message` VALUES ('19', '1', '1', '嘿嘿', '2021-06-21 21:14:02');
INSERT INTO `group_message` VALUES ('20', '1', '1', '666', '2021-06-21 21:17:55');
INSERT INTO `group_message` VALUES ('21', '1', '3', '冲冲冲', '2021-06-21 21:18:11');
INSERT INTO `group_message` VALUES ('22', '4', '1', '我创建了群聊', '2021-06-21 23:20:33');
INSERT INTO `group_message` VALUES ('23', '4', '4', '好的', '2021-06-21 23:20:54');
INSERT INTO `group_message` VALUES ('24', '1', '1', '大家好', '2021-07-04 14:42:59');

-- ----------------------------
-- Table structure for group_user
-- ----------------------------
DROP TABLE IF EXISTS `group_user`;
CREATE TABLE `group_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of group_user
-- ----------------------------
INSERT INTO `group_user` VALUES ('1', '1', '1');
INSERT INTO `group_user` VALUES ('2', '1', '4');
INSERT INTO `group_user` VALUES ('3', '1', '3');
INSERT INTO `group_user` VALUES ('5', '4', '1');
INSERT INTO `group_user` VALUES ('6', '4', '4');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` int(11) DEFAULT NULL,
  `receiver` int(11) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `gmt_create` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES ('7', '1', '3', '打个招呼，嘿嘿', '2021-06-21 13:26:13');
INSERT INTO `message` VALUES ('8', '1', '3', 'www', '2021-06-21 21:22:25');
INSERT INTO `message` VALUES ('9', '1', '3', '顶顶顶顶', '2021-06-21 21:25:25');
INSERT INTO `message` VALUES ('10', '3', '1', 'hello', '2021-06-21 21:35:55');
INSERT INTO `message` VALUES ('11', '1', '3', 'hhh\n哈哈哈', '2021-06-21 21:36:09');
INSERT INTO `message` VALUES ('12', '1', '3', '一对一聊天测试', '2021-07-04 00:08:42');
INSERT INTO `message` VALUES ('13', '1', '3', '你好', '2021-07-04 14:42:30');
INSERT INTO `message` VALUES ('14', '3', '1', 'nihao\n', '2021-07-04 14:42:43');

-- ----------------------------
-- Table structure for request
-- ----------------------------
DROP TABLE IF EXISTS `request`;
CREATE TABLE `request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '添加者id',
  `add_id` int(11) DEFAULT NULL COMMENT '被添加者id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of request
-- ----------------------------
INSERT INTO `request` VALUES ('1', '1', '5');
INSERT INTO `request` VALUES ('3', '1', '4');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'Kim', '123456', 'photo.jpg');
INSERT INTO `user` VALUES ('2', 'Tao', '123456', 'flower.jpg');
INSERT INTO `user` VALUES ('3', '小伙子', '123', 'th3.jpg');
INSERT INTO `user` VALUES ('4', '小明', '123', 'car.jpg');
INSERT INTO `user` VALUES ('5', '大成', '123', '1.jpg');
