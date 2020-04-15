 -- PROC_YL_COUNT_CHUNMO
DELIMITER $$

USE `iottester`$$

DROP PROCEDURE IF EXISTS `PROC_YL_COUNT_CHUNMO`$$

CREATE PROCEDURE `PROC_YL_COUNT_CHUNMO`(IN `IN_START_TIME` varchar(30),IN `IN_END_TIME` varchar(30),IN `IN_SUPPLIERS` varchar(30),OUT `OUT_RESULT` varchar(2))
BEGIN

 -- 0 将话单导入中间表
insert into iij_12_t select id,imsi,cdr_time,sum(cdr_byte),country_desc,operator_desc,supplier_code,supplier_name,status,create_time,mcc,iccid,cdr_end_time
 from cdr_record_t where instr(IN_SUPPLIERS,supplier_code) and cdr_time < IN_END_TIME and cdr_time >= IN_START_TIME group by imsi,left(cdr_time,10);

 -- 1 所有已结束订单导入中间表
insert into resolve_export_t(partner_name,iccid,order_code,package_name,actual_start_time,actual_end_time) select partner_name,iccid,order_code,package_name,actual_start_time,actual_end_time from hist_sim_order_t where order_status = '3' and 
 partner_code = 'P000258';
insert into resolve_export_t(partner_name,iccid,order_code,package_name,actual_start_time,actual_end_time) select partner_name,iccid,order_code,package_name,actual_start_time,actual_end_time from sim_order_t where order_status = '3' and 
 partner_code = 'P000258';

 -- 2 所有码号使用记录导入中间表
insert into usage_test_t(order_code,soft_iccid) select order_code,soft_iccid from sim_softsim_usage_t;
insert into usage_test_t(order_code,soft_iccid) select order_code,soft_iccid from hist_sim_softsim_usage_t;


 -- 2 合并订单和imsi
insert into resolve_export_t2(partner_name,iccid,order_code,package_name,actual_start_time,actual_end_time,local_imsi,supplier_code,supplier_name) 
 select port1.partner_name,port1.iccid,port1.order_code,port1.package_name,port1.actual_start_time,port1.actual_end_time,
 info.imsi,info.supplier_code,info.supplier_name from resolve_export_t port1 left outer join usage_test_t usaget on port1.order_code = usaget.order_code
 left outer join softsim_info_t info on usaget.soft_iccid = info.iccid;
 
 -- delete from resolve_export_t2 where not instr(IN_SUPPLIERS,supplier_code);
 delete from resolve_export_t2 where supplier_code <> '1011' and supplier_code <> '1007';


 -- 3 统计cdr
insert into resolve_export_t3(partner_name,iccid,order_code,package_name,actual_start_time,actual_end_time,local_imsi,supplier_code,supplier_name,cdr_time,cdr_byte) 
 select port2.partner_name,port2.iccid,port2.order_code,port2.package_name,port2.actual_start_time,port2.actual_end_time,
 port2.local_imsi,port2.supplier_code,port2.supplier_name,left(record.cdr_time,10),record.cdr_byte from resolve_export_t2 port2 
 left outer join iij_12_t record on port2.local_imsi = record.imsi and
 (left(record.cdr_time,10)<=left(port2.actual_end_time,10) and left(record.cdr_time,10) >= left(port2.actual_start_time,10));

 -- 4 删除其他月份无关订单(开始时间大于本月月底或者结束时间早于本月月初)
delete from resolve_export_t3 where (actual_start_time > IN_END_TIME or actual_end_time < IN_START_TIME) and cdr_byte is null;
update resolve_export_t3 set cdr_byte = '0' where cdr_byte is null;
 -- 5 导入订单表
insert into resolve_package_order_days(package_name,order_code,days,cdr_byte,channel) select package_name, order_code,count(1),sum(cdr_byte),channel from resolve_export_t3  group by order_code;


commit;
set OUT_RESULT='0';
END$$

DELIMITER ;