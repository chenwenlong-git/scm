ALTER TABLE `cn_scm`.`develop_review_sample_order`
    MODIFY COLUMN `develop_sample_dev_opinion` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品开发意见' AFTER `develop_sample_stage`,
    MODIFY COLUMN `develop_sample_qlty_opinion` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品质量意见' AFTER `develop_sample_dev_opinion`,
    MODIFY COLUMN `abnormal_hair` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '脱断发情况' AFTER `develop_sample_qlty_opinion`,
    MODIFY COLUMN `floating_hair` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '浮发情况' AFTER `abnormal_hair`,
    MODIFY COLUMN `mesh_cap_fit` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '网帽服帖度' AFTER `floating_hair`,
    MODIFY COLUMN `hair_feel` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '毛发手感' AFTER `mesh_cap_fit`;

ALTER TABLE `cn_scm`.`purchase_child_order`
    MODIFY COLUMN `purchase_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购单状态:WAIT_APPROVE(待审核),WAIT_CONFIRM(计划确认),WAIT_FOLLOWER_CONFIRM(跟单确认),WAIT_RECEIVE_ORDER(待接单),WAIT_SCHEDULING(待排产),WAIT_COMMISSIONING(待投产),COMMISSION(前处理),PRETREATMENT(后处理),SEWING(三联机中),AFTER_TREATMENT(高针中),POST_QC(后整质检中),WAIT_DELIVER(待发货),WAIT_RECEIPT(待收货),RECEIPTED(已收货),WAIT_QC(待质检),WAIT_WAREHOUSING(待入库),WAREHOUSED(已入库),RETURN(已退货),SETTLE(已结算),DELETE(已作废),FINISH(已完结),\n' AFTER `sample_child_order_no`;


ALTER TABLE process_order
    ADD COLUMN `receive_material_time` DATETIME NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '回料时间' AFTER `produced_time`;