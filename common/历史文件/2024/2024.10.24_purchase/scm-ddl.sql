ALTER TABLE `cn_scm`.`purchase_child_order`
    MODIFY COLUMN `purchase_order_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购单类型:FIRST_ORDER(首单),PRENATAL(产前样),NORMAL(常规),WH(网红),REPAIR(返修)' AFTER `purchase_order_status`,
    ADD COLUMN `return_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '退货单号' AFTER `promise_date_chg`;