use
    scm;

ALTER TABLE `cn_scm`.`purchase_settle_order`
    DROP INDEX `uk_purchase_settle_order_1`,
    ADD UNIQUE INDEX `uk_purchase_settle_order_1` (`purchase_settle_status` ASC, `supplier_code` ASC, `month` ASC,
                                                   `del_timestamp` ASC) USING BTREE;

ALTER TABLE `cn_scm`.`process_settle_order`
    DROP INDEX `uk_process_settle_order_1`,
    ADD UNIQUE INDEX `uk_process_settle_order_1` (`month` ASC, `del_timestamp` ASC) USING BTREE;