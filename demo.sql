/*
 Navicat Premium Data Transfer

 Source Server         : zuki
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3306
 Source Schema         : demo

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 22/04/2020 23:09:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for likes
-- ----------------------------
DROP TABLE IF EXISTS `likes`;
CREATE TABLE `likes`  (
  `lid` int(10) NOT NULL AUTO_INCREMENT,
  `uid` int(10) DEFAULT NULL,
  `pid` int(10) DEFAULT NULL,
  PRIMARY KEY (`lid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3053 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of likes
-- ----------------------------
INSERT INTO `likes` VALUES (3001, 1002, 5);
INSERT INTO `likes` VALUES (3003, 1002, 1);
INSERT INTO `likes` VALUES (3022, 1003, 1);
INSERT INTO `likes` VALUES (3023, 1003, 3);
INSERT INTO `likes` VALUES (3024, 1003, 4);
INSERT INTO `likes` VALUES (3030, 1001, 1);
INSERT INTO `likes` VALUES (3034, 1004, 0);
INSERT INTO `likes` VALUES (3046, 1004, 6);

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`  (
  `pid` int(11) NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `likeCnt` int(255) DEFAULT NULL,
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of products
-- ----------------------------
INSERT INTO `products` VALUES (0, '00000', 128);
INSERT INTO `products` VALUES (1, '11111', 0);
INSERT INTO `products` VALUES (2, '22222', 322);
INSERT INTO `products` VALUES (3, '33333', 134);
INSERT INTO `products` VALUES (4, '44444', 1232);
INSERT INTO `products` VALUES (5, '55555', 0);
INSERT INTO `products` VALUES (6, '66666', 12);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `uid` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1001', '123456');
INSERT INTO `users` VALUES ('1002', '1231');
INSERT INTO `users` VALUES ('1003', '111');
INSERT INTO `users` VALUES ('1004', '1004');

SET FOREIGN_KEY_CHECKS = 1;
