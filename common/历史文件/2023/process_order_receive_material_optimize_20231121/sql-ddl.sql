ALTER TABLE process_order
    ADD COLUMN `receive_material_time` DATETIME NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '回料时间' AFTER `produced_time`;