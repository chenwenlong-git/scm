ALTER TABLE `cn_scm`.`defect_handling`
    MODIFY COLUMN `defect_handling_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '次品类型：BULK_DEFECT(质检不合格),PROCESS_DEFECT(加工次品),INSIDE_DEFECT(库内抽查),MATERIAL_DEFECT(原料次品),' AFTER `defect_handling_programme`;

ALTER TABLE `cn_scm`.`supplier`
    MODIFY COLUMN `logistics_aging` int NOT NULL DEFAULT 0 COMMENT '物流时效(天)' AFTER `remarks`;