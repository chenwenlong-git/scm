ALTER TABLE `cn_scm`.`supplier`
    ADD UNIQUE INDEX `uk_supplier_1` (`supplier_code` ASC) USING BTREE;