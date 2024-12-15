ALTER TABLE `cn_scm`.`sku_attr_price`
    ADD COLUMN `material_attr_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '材料属性值' AFTER `size_attr_value`,
    DROP
        INDEX `uk_sku_attr_price_1`,
    ADD UNIQUE INDEX `uk_sku_attr_price_1` (`lace_attr_value` ASC, `size_attr_value` ASC, `material_attr_value` ASC,
                                            `del_timestamp`) USING BTREE;