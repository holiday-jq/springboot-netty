/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50728
Source Host           : localhost:3306
Source Database       : wechat

Target Server Type    : MYSQL
Target Server Version : 50728
File Encoding         : 65001

Date: 2020-12-10 16:20:28
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_friend
-- ----------------------------
DROP TABLE IF EXISTS `t_friend`;
CREATE TABLE `t_friend` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) NOT NULL,
  `friend_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_friend
-- ----------------------------
INSERT INTO `t_friend` VALUES ('1', '04150901', '04150902');
INSERT INTO `t_friend` VALUES ('2', '04150901', '04150903');
INSERT INTO `t_friend` VALUES ('3', '04150901', '04150904');
INSERT INTO `t_friend` VALUES ('4', '04150901', '04150905');
INSERT INTO `t_friend` VALUES ('5', '04150901', '04150906');
INSERT INTO `t_friend` VALUES ('6', '04150902', '04150901');
INSERT INTO `t_friend` VALUES ('7', '04150902', '04150903');
INSERT INTO `t_friend` VALUES ('8', '04150902', '04150904');
INSERT INTO `t_friend` VALUES ('9', '04150902', '04150905');
INSERT INTO `t_friend` VALUES ('10', '04150902', '04150906');
INSERT INTO `t_friend` VALUES ('11', '04150903', '04150901');
INSERT INTO `t_friend` VALUES ('12', '04150903', '04150902');
INSERT INTO `t_friend` VALUES ('13', '04150903', '04150904');
INSERT INTO `t_friend` VALUES ('14', '04150903', '04150905');
INSERT INTO `t_friend` VALUES ('15', '04150903', '04150906');
INSERT INTO `t_friend` VALUES ('16', '04150904', '04150901');
INSERT INTO `t_friend` VALUES ('17', '04150904', '04150902');
INSERT INTO `t_friend` VALUES ('18', '04150904', '04150903');
INSERT INTO `t_friend` VALUES ('19', '04150904', '04150905');
INSERT INTO `t_friend` VALUES ('20', '04150904', '04150906');
INSERT INTO `t_friend` VALUES ('21', '04150905', '04150901');
INSERT INTO `t_friend` VALUES ('22', '04150905', '04150902');
INSERT INTO `t_friend` VALUES ('23', '04150905', '04150903');
INSERT INTO `t_friend` VALUES ('24', '04150905', '04150904');
INSERT INTO `t_friend` VALUES ('25', '04150905', '04150906');
INSERT INTO `t_friend` VALUES ('26', '04150906', '04150901');
INSERT INTO `t_friend` VALUES ('27', '04150906', '04150902');
INSERT INTO `t_friend` VALUES ('28', '04150906', '04150903');
INSERT INTO `t_friend` VALUES ('29', '04150906', '04150904');
INSERT INTO `t_friend` VALUES ('30', '04150906', '04150905');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `userId` varchar(255) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `sex` varchar(10) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `showName` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('04150901', 'user1', '123456', '男', 'headImg.jpg', '小黄');
INSERT INTO `t_user` VALUES ('04150902', 'user2', '123456', '女', 'user2.jpg', '小刘');
INSERT INTO `t_user` VALUES ('04150903', 'user3', '123456', '男', 'user3.jpg', '小强');
INSERT INTO `t_user` VALUES ('04150904', 'user4', '123456', '男', 'user4.jpg', '铁憨憨');
INSERT INTO `t_user` VALUES ('04150905', 'user5', '123456', '男', 'user5.jpg', '小明');
INSERT INTO `t_user` VALUES ('04150906', 'user6', '123456', '女', 'user6.jpg', '小红');
