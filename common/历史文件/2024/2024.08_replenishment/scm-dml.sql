update purchase_child_order
set place_order_user     = create_user,
    place_order_username = create_username;

update purchase_parent_order
set place_order_user     = create_user,
    place_order_username = create_username;

update purchase_child_order_raw_deliver
set particular_location = 'FALSE';

update purchase_child_order_item
set init_purchase_cnt = purchase_cnt
where `purchase_cnt` != `init_purchase_cnt`;

ALTER TABLE `cn_scm`.`purchase_child_order`
    MODIFY COLUMN `split_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '拆单补交类型:SUPPLIER_SPLIT(供应商拆单),FOLLOW_SPLIT(跟单拆单),GOODS_SPLIT(商品拆单),SUGGEST_SPLIT(推荐拆单)' AFTER `delay_days`;
