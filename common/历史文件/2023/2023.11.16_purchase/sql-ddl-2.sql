# 执行sql-dml.sql之后再执行
ALTER TABLE `cn_scm`.`purchase_parent_order_change`
    ADD UNIQUE INDEX `uk_purchase_parent_order_change_2` (`purchase_parent_order_no`) USING BTREE;