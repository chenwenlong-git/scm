ALTER TABLE supplier_inventory
    ADD defective_inventory INT DEFAULT 0 NOT NULL COMMENT '不良库存' AFTER self_provide_inventory;

ALTER TABLE `cn_scm`.`purchase_child_order`
    ADD COLUMN `split_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '拆单补交类型:SUPPLIER_SPLIT(供应商拆单),FOLLOW_SPLIT(跟单拆单),GOODS_SPLIT(商品拆单),' AFTER `delay_days`;

ALTER TABLE supplier_inventory_record
    ADD supplier_inventory_record_status varchar(32) default ''
        not null comment '库存变更记录状态：PENDING_APPROVAL(待审核),EFFECTIVE(已生效),REJECTED(已拒绝)'
        AFTER record_remark;