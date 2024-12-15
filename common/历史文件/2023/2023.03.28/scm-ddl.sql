ALTER TABLE `cn_scm`.`overseas_warehouse_msg`
    ADD UNIQUE INDEX `uk_overseas_warehouse_msg_2` (`overseas_shipping_mark_no`) USING BTREE;