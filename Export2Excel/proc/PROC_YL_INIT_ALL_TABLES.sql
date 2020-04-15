 -- PROC_YL_INIT_ALL_TABLES
DELIMITER $$

USE `iottester`$$

DROP PROCEDURE IF EXISTS `PROC_YL_INIT_ALL_TABLES`$$

CREATE PROCEDURE `PROC_YL_INIT_ALL_TABLES`(IN `IN_IMSI` varchar(30),OUT `OUT_RESULT` varchar(2))
BEGIN


drop table if exists  resolve_export_t;
drop table if exists  resolve_export_t2;
drop table if exists  resolve_export_t3;
drop table if exists  resolve_package_order_days;
drop table if exists  iij_12_t;
drop table if exists  usage_test_t;


drop table if exists  resolve_export_t;
CREATE TABLE `resolve_export_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `partner_name` varchar(50) DEFAULT NULL COMMENT '归属的合作伙伴名称',
  `iccid` varchar(30) NOT NULL COMMENT '主号ICCID',
  `order_code` varchar(50) NOT NULL COMMENT '订单编码',
  `package_name` varchar(50) NOT NULL COMMENT '资费套餐名称（中文）',
  `actual_start_time` varchar(30) DEFAULT NULL COMMENT '订单实际开始时间',
  `actual_end_time` varchar(30) DEFAULT NULL COMMENT '订单实际结束时间',
  local_imsi varchar(30) DEFAULT NULL COMMENT '副号imsi',
  supplier_code varchar(30) DEFAULT NULL COMMENT '副号imsi',
  supplier_name varchar(30) DEFAULT NULL COMMENT '副号imsi',
 `cdr_time` varchar(30) DEFAULT NULL COMMENT 'cdr时间，精确到天',
 `cdr_byte` varchar(30) DEFAULT NULL COMMENT '每天流量，单位字节',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

drop table if exists  resolve_export_t2;
CREATE TABLE `resolve_export_t2` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `partner_name` varchar(50) DEFAULT NULL COMMENT '归属的合作伙伴名称',
  `iccid` varchar(30) NOT NULL COMMENT '主号ICCID',
  `order_code` varchar(50) NOT NULL COMMENT '订单编码',
  `package_name` varchar(50) NOT NULL COMMENT '资费套餐名称（中文）',
  `actual_start_time` varchar(30) DEFAULT NULL COMMENT '订单实际开始时间',
  `actual_end_time` varchar(30) DEFAULT NULL COMMENT '订单实际结束时间',
  local_imsi varchar(30) DEFAULT NULL COMMENT '副号imsi',
  supplier_code varchar(30) DEFAULT NULL COMMENT '副号imsi',
  supplier_name varchar(30) DEFAULT NULL COMMENT '副号imsi',
 `cdr_time` varchar(30) DEFAULT NULL COMMENT 'cdr时间，精确到天',
 `cdr_byte` varchar(30) DEFAULT NULL COMMENT '每天流量，单位字节',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

drop table if exists  resolve_export_t3;
CREATE TABLE `resolve_export_t3` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `partner_name` varchar(50) DEFAULT NULL COMMENT '归属的合作伙伴名称',
  `iccid` varchar(30) NOT NULL COMMENT '主号ICCID',
  `order_code` varchar(50) NOT NULL COMMENT '订单编码',
  `package_name` varchar(50) NOT NULL COMMENT '资费套餐名称（中文）',
  `actual_start_time` varchar(30) DEFAULT NULL COMMENT '订单实际开始时间',
  `actual_end_time` varchar(30) DEFAULT NULL COMMENT '订单实际结束时间',
  local_imsi varchar(30) DEFAULT NULL COMMENT '副号imsi',
  supplier_code varchar(30) DEFAULT NULL COMMENT '副号imsi',
  supplier_name varchar(30) DEFAULT NULL COMMENT '副号imsi',
 `cdr_time` varchar(30) DEFAULT NULL COMMENT 'cdr时间，精确到天',
 `cdr_byte` varchar(30) DEFAULT NULL COMMENT '每天流量，单位字节',
 `channel` varchar(30) DEFAULT "0" COMMENT '渠道:1惠州 2 苏州 3 港台',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

 -- 任务2_统计订单数量
drop table if exists  resolve_package_order_days;
CREATE TABLE `resolve_package_order_days` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `package_name` varchar(50) NOT NULL COMMENT '资费套餐名称（中文）',
  `order_code` varchar(50) NOT NULL COMMENT '订单编码',
  `days` varchar(30) DEFAULT NULL COMMENT '使用天数',
  `cdr_byte` varchar(30) DEFAULT NULL COMMENT '流量，单位字节',
  `channel` varchar(30) DEFAULT "0" COMMENT '渠道:1惠州 2 苏州 3 港台',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

 -- 任务3_验证12月份cdr总量和iij提供的是否一致
drop table if exists  iij_12_t;
CREATE TABLE `iij_12_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `imsi` varchar(20) DEFAULT NULL,
  `cdr_time` datetime NOT NULL COMMENT '流量产生时间',
  `cdr_byte` bigint(20) NOT NULL COMMENT '数据流量字节数',
  `country_desc` varchar(50) DEFAULT NULL COMMENT '国家名称描述',
  `operator_desc` varchar(50) DEFAULT NULL COMMENT '登网的运营商名称',
  `supplier_code` varchar(4) NOT NULL COMMENT '供应商编码',
  `supplier_name` varchar(50) NOT NULL COMMENT '供应商名称',
  `status` varchar(1) DEFAULT '0' COMMENT '0：待处理；1：处理中；2：已处理',
  `create_time` varchar(30) NOT NULL COMMENT '入库时间',
  `mcc` varchar(50) DEFAULT NULL COMMENT '运营商网络码',
  `iccid` varchar(20) DEFAULT NULL COMMENT 'iccid',
  `cdr_end_time` datetime DEFAULT NULL COMMENT '流量结束时间，如果cdr中无结束时间，使用流量产生时间',
  PRIMARY KEY (`id`),
  KEY `index_cdr_record_imsi` (`imsi`),
  KEY `index_cdr_record_time` (`cdr_time`),
  KEY `cdr_record_mcc` (`mcc`),
  KEY `index_cdr_record_iccid` (`iccid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

drop table if exists  usage_test_t;
CREATE TABLE `usage_test_t` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `order_code` varchar(50) NOT NULL COMMENT '订单编码',
  `soft_iccid` varchar(30) DEFAULT NULL COMMENT '副号imsi',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

commit;
set OUT_RESULT='0';
END$$

DELIMITER ;