-- 在 repair_order_result 表中添加原料使用数量字段
ALTER TABLE repair_order_result
    ADD COLUMN material_usage_quantity INT UNSIGNED DEFAULT 0 NOT NULL COMMENT '原料使用数量' AFTER completed_quantity;

-- 在 repair_order_item 表中添加发货数量字段
ALTER TABLE repair_order_item
    ADD COLUMN delivery_num INT UNSIGNED DEFAULT 0 NOT NULL COMMENT '发货数量' AFTER act_process_scrap_cnt;

-- 在 repair_order_item 表中添加返修单号字段
alter table process_material_back
    ADD COLUMN
        repair_order_no varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '返修单号' after process_order_no;
ALTER TABLE `process_material_back`
    ADD INDEX `idx_process_material_back_3` (`repair_order_no`) USING BTREE;