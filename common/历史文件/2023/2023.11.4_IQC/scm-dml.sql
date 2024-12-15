insert into `cn_scm`.`qc_order`(`qc_order_id`, `qc_order_no`, `warehouse_code`, `receive_order_no`, `process_order_no`,
                                `qc_type`, `qc_amount`, `qc_state`, `qc_result`, `hand_over_time`, `task_finish_time`,
                                `audit_time`, `hand_over_user`, `operator`, `operator_name`, `auditor`, `sku_dev_type`,
                                `create_user`, `create_username`, `update_user`, `update_username`, `create_time`,
                                `update_time`, `version`, `del_timestamp`)
select `qc_order_id`,
       `qc_order_no`,
       `warehouse_code`,
       `receive_order_no`,
       `process_order_no`,
       `qc_type`,
       `qc_amount`,
       `qc_state`,
       `qc_result`,
       `hand_over_time`,
       `task_finish_time`,
       `audit_time`,
       `hand_over_user`,
       `operator`,
       `operator_name`,
       `auditor`,
       `sku_dev_type`,
       `create_user`,
       `create_username`,
       `update_user`,
       `update_username`,
       `create_time`,
       `update_time`,
       `version`,
       `del_timestamp`
from `cn_wms`.`qc_order`;

insert into `cn_scm`.`qc_detail`(`qc_detail_id`, `qc_order_no`, `container_code`, `batch_code`, `spu`, `sku_code`,
                                 `amount`, `wait_amount`, `pass_amount`, `not_pass_amount`, `qc_result`,
                                 `qc_not_passed_reason`, `remark`, `picture_ids`, `weight`, `relation_qc_detail_id`,
                                 `create_user`, `create_username`, `update_user`, `update_username`, `create_time`,
                                 `update_time`, `version`, `del_timestamp`)
select `qc_detail_id`,
       `qc_order_no`,
       `container_code`,
       `batch_code`,
       `spu`,
       `sku_code`,
       `amount`,
       `wait_amount`,
       `pass_amount`,
       `not_pass_amount`,
       `qc_result`,
       `qc_not_passed_reason`,
       `remark`,
       `picture_ids`,
       `weight`,
       `relation_qc_detail_id`,
       `create_user`,
       `create_username`,
       `update_user`,
       `update_username`,
       `create_time`,
       `update_time`,
       `version`,
       `del_timestamp`
from `cn_wms`.`qc_detail`;

insert into `cn_scm`.qc_receive_order(`qc_receive_order_id`, `qc_order_no`, `receive_order_no`, `receive_type`,
                                      `supplier_code`, `delivery_order_no`, `goods_category`, `scm_biz_no`,
                                      `create_time`, `create_user`, `update_time`, `update_user`, `del_timestamp`,
                                      `version`)
select qo.qc_order_id,
       qo.qc_order_no,
       qo.receive_order_no,
       ro.receive_type,
       ro.supplier_code,
       ro.delivery_order_no,
       ro.goods_category,
       ro.scm_biz_no,
       ro.create_time,
       ro.create_user,
       ro.update_time,
       ro.update_user,
       ro.del_timestamp,
       ro.version
from `cn_wms`.`qc_order` AS qo
         inner join `cn_wms`.`receive_order` AS ro
                    on qo.receive_order_no = ro.receive_order_no;

insert into `cn_scm`.`qc_on_shelves_order`(`qc_on_shelves_order_id`, `qc_order_no`, `on_shelves_order_no`,
                                           `plan_amount`, `type`, `create_time`, `create_user`, `update_time`,
                                           `update_user`, `del_timestamp`, `version`)
select oso.on_shelves_order_id,
       qo.qc_order_no,
       oso.on_shelves_order_no,
       oso.plan_amount,
       oso.`type`,
       oso.create_time,
       oso.create_user,
       oso.update_time,
       oso.update_user,
       oso.del_timestamp,
       oso.version
from `cn_wms`.`qc_order` AS qo
         inner join `cn_wms`.`on_shelves_order` as oso
                    on qo.qc_order_no = oso.qc_order_no;

update cn_scm.defect_handling as dh
set dh.warehouse_code =
        (select qo.warehouse_code
         from cn_wms.qc_order as qo
         where qo.qc_order_no = dh.qc_order_no)
where dh.warehouse_code = '';