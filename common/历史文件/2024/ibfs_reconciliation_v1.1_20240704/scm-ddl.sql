alter table `cn_scm`.`finance_reco_order_item_sku`
    modify column `remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注';
