/*
Navicat MySQL Data Transfer

Source Server         : 本机
Source Server Version : 50553
Source Host           : localhost:3306
Source Database       : laboratory_equipment

Target Server Type    : MYSQL
Target Server Version : 50553
File Encoding         : 65001

Date: 2017-06-09 20:25:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `items`
-- ----------------------------
DROP TABLE IF EXISTS `items`;
CREATE TABLE `items` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id主键',
  `name` varchar(255) DEFAULT NULL COMMENT '设备名',
  `code` varchar(255) DEFAULT NULL COMMENT '设备编号(自动编号，需购设备中编号可为空)',
  `type` varchar(255) DEFAULT NULL COMMENT '类别',
  `modelNum` varchar(255) DEFAULT NULL COMMENT '型号',
  `norms` varchar(255) DEFAULT NULL COMMENT '规格',
  `user_login_id` int(11) DEFAULT NULL COMMENT '操作人id',
  `status` int(1) DEFAULT NULL COMMENT '状态(-1:记录删除；0：需购设备；1：新加设备；2：待修设备；3：报废设备；)',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=84 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of items
-- ----------------------------

-- ----------------------------
-- Table structure for `memu`
-- ----------------------------
DROP TABLE IF EXISTS `memu`;
CREATE TABLE `memu` (
  `id` int(10) NOT NULL COMMENT 'id',
  `parent_id` int(10) DEFAULT NULL COMMENT '父节点',
  `privilege_name` varchar(10) DEFAULT NULL COMMENT '菜单名称',
  `status` int(1) DEFAULT NULL COMMENT '状态(0/1  不可用/可用)',
  `uri` varchar(50) DEFAULT NULL COMMENT '菜单的路由',
  `s_class` varchar(30) DEFAULT NULL COMMENT '菜单样式',
  `rank` int(1) DEFAULT NULL COMMENT '菜单权限（0为超级管理员可见，1为所有管理员可见）',
  `sequence` int(11) DEFAULT NULL COMMENT '菜单显示顺序（从小到大）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of memu
-- ----------------------------
INSERT INTO `memu` VALUES ('0', '0', '设备审核', '1', 'audit', 'ace-icon fa fa-thumbs-up', '2', '1');
INSERT INTO `memu` VALUES ('1', '0', '需购设备', '1', 'need', 'ace-icon fa fa-calendar', '1', '2');
INSERT INTO `memu` VALUES ('2', '0', '新添设备', '1', 'new', 'ace-icon fa fa-plus', '1', '3');
INSERT INTO `memu` VALUES ('3', '0', '待修设备', '1', 'repair', 'ace-icon fa fa-wrench', '1', '4');
INSERT INTO `memu` VALUES ('4', '0', '报废设备', '1', 'scrap', 'ace-icon fa fa-trash', '1', '5');
INSERT INTO `memu` VALUES ('5', '0', '全部设备', '1', 'all', 'ace-icon fa fa-th-list', '1', '6');
INSERT INTO `memu` VALUES ('6', '0', '用户管理', '1', 'user', 'ace-icon fa fa-user', '0', '7');

-- ----------------------------
-- Table structure for `need_items`
-- ----------------------------
DROP TABLE IF EXISTS `need_items`;
CREATE TABLE `need_items` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id主键',
  `items_id` int(11) DEFAULT NULL COMMENT '设备id',
  `price` int(11) DEFAULT NULL COMMENT '单价',
  `number` int(11) DEFAULT NULL COMMENT '数量',
  `purchaseDate` date DEFAULT NULL COMMENT '购置日期',
  `vender` varchar(255) DEFAULT NULL COMMENT '生产厂家',
  `expirationDate` int(3) DEFAULT NULL COMMENT '保质期（年）',
  `userName` varchar(20) DEFAULT NULL COMMENT '经办人',
  `status` int(1) DEFAULT NULL COMMENT '状态(0：待审核；1：审核通过；-1：驳回；-2：已购)',
  `remark` text COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of need_items
-- ----------------------------

-- ----------------------------
-- Table structure for `new_items`
-- ----------------------------
DROP TABLE IF EXISTS `new_items`;
CREATE TABLE `new_items` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id主键',
  `items_id` int(11) DEFAULT NULL COMMENT '设备id',
  `price` int(11) DEFAULT NULL COMMENT '单价',
  `purchaseDate` date DEFAULT NULL COMMENT '购置日期',
  `vender` varchar(255) DEFAULT NULL COMMENT '生产厂家',
  `expirationDate` int(11) DEFAULT NULL COMMENT '保质期（年）',
  `userName` varchar(20) DEFAULT NULL COMMENT '经办人',
  `status` int(1) DEFAULT NULL COMMENT '状态(0：正常；-1：状态变更（去维修/已报废）)',
  `remark` text COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of new_items
-- ----------------------------

-- ----------------------------
-- Table structure for `repair_items`
-- ----------------------------
DROP TABLE IF EXISTS `repair_items`;
CREATE TABLE `repair_items` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id主键',
  `items_id` int(11) DEFAULT NULL COMMENT '设备id',
  `repairDate` date DEFAULT NULL COMMENT '修理日期',
  `vender` varchar(255) DEFAULT NULL COMMENT '修理厂家',
  `price` int(11) DEFAULT NULL COMMENT '修理费用',
  `userName` varchar(20) DEFAULT NULL COMMENT '责任人',
  `status` int(1) DEFAULT NULL COMMENT '状态（0：待维修；1：已维修；2：维修失败报废）',
  `remark` text COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of repair_items
-- ----------------------------

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL COMMENT 'id主键',
  `type` int(1) DEFAULT NULL COMMENT '类别（0：超级管理员；1：普通管理员；2领导）',
  `rank` int(1) DEFAULT NULL COMMENT '权限（0；超级管理员权限；1普通管理员权限；2领导权限）',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('0', '0', '0');
INSERT INTO `role` VALUES ('1', '1', '1');
INSERT INTO `role` VALUES ('2', '2', '2');

-- ----------------------------
-- Table structure for `scrap_items`
-- ----------------------------
DROP TABLE IF EXISTS `scrap_items`;
CREATE TABLE `scrap_items` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id主键',
  `items_id` int(11) DEFAULT NULL COMMENT '设备id',
  `scrapDate` date DEFAULT NULL COMMENT '报废日期',
  `userName` varchar(20) DEFAULT NULL COMMENT '经办人',
  `status` int(1) DEFAULT NULL COMMENT '状态(0：待审核；1：审核通过；-1：驳回)',
  `remark` text COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of scrap_items
-- ----------------------------

-- ----------------------------
-- Table structure for `user_login`
-- ----------------------------
DROP TABLE IF EXISTS `user_login`;
CREATE TABLE `user_login` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id主键',
  `name` varchar(20) DEFAULT NULL COMMENT '姓名',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱（接受验证码）',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `pwd` varchar(255) DEFAULT NULL COMMENT '密码（md5加密）',
  `status` int(1) DEFAULT NULL COMMENT '状态（1：可用；0禁用；-1：删除）',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_login TODO 需要登录的话修改为自己的邮箱
-- ----------------------------
INSERT INTO `user_login` VALUES ('1', '系统管理员', '0', 'chen_s_vip@163.com', '15666666666', 'd93a5def7511da3d0f2d171d9c344e91', '1', '2017-05-01 15:39:02', '2017-05-01 15:39:11');

