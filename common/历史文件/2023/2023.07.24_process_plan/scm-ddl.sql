CREATE TABLE `employee_grade`
(
    `employee_grade_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `grade_type`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '职级类别:COLORIST(染发师),STYLIST(造型师),CLIPS(缝卡子),HEADGEAR(缝头套),',
    `grade_name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '职级名称',
    `grade_level`       decimal(5, 1)                                                 NOT NULL DEFAULT '0.0' COMMENT '职级等级，数值越大职级越高',
    `create_time`       datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`       datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`     bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`           int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`employee_grade_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='员工职级表';

CREATE TABLE `employee_grade_process`
(
    `employee_grade_process_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `employee_grade_id`         bigint                                                        NOT NULL DEFAULT '0' COMMENT '职级id',
    `process_id`                bigint                                                        NOT NULL DEFAULT '0' COMMENT '工序id',
    `process_name`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工序名称',
    `process_num`               int                                                           NOT NULL DEFAULT '0' COMMENT '工序产能数',
    `create_time`               datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`               datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`             bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                   int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`employee_grade_process_id`) USING BTREE,
    UNIQUE KEY `uk_employee_grade_process_1` (`employee_grade_id`, `process_id`, `del_timestamp`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='员工职级工序能力关系表';


CREATE TABLE `employee_grade_relation`
(
    `employee_grade_relation_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `employee_no`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '员工编号',
    `employee_name`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '员工名称',
    `employee_grade_id`          bigint                                                        NOT NULL DEFAULT '0' COMMENT '员工职级表主键id',
    `create_time`                datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`              bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                    int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`employee_grade_relation_id`) USING BTREE,
    UNIQUE KEY `uk_employee_grade_relation` (`employee_no`, `employee_grade_id`, `del_timestamp`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='员工职级关系表';



CREATE TABLE `employee_process_ability`
(
    `employee_process_ability_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `production_pool_code`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '产能池编号',
    `employee_no`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '员工编号',
    `employee_name`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '员工名称',
    `process_id`                  bigint                                                        NOT NULL DEFAULT '0' COMMENT '工序 ID',
    `process_name`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工序名称',
    `total_processed_num`         int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '总加工数量',
    `available_processed_num`     int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '可加工数量',
    `validity_time`               datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '产能池有效期截止时间',
    `create_time`                 datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                 datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`               bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                     int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`employee_process_ability_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='工序人员产能信息表(产能池)';

CREATE TABLE `process_procedure_employee_plan`
(
    `process_procedure_employee_plan_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `production_pool_code`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '产能池编号',
    `process_order_no`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '加工单号',
    `process_order_procedure_id`         bigint                                                        NOT NULL DEFAULT '0' COMMENT '加工单工序表主键id',
    `process_id`                         bigint                                                        NOT NULL DEFAULT '0' COMMENT '工序 ID',
    `process_name`                       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工序名称',
    `employee_no`                        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '员工编号',
    `employee_name`                      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '员工名称',
    `process_num`                        int                                                           NOT NULL DEFAULT '0' COMMENT '工序产能数',
    `commission`                         decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '工序提成',
    `expect_begin_time`                  datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '预计开始时间',
    `expect_end_time`                    datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '预计结束时间',
    `receive_material_time`              datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '接收原料时间',
    `act_begin_time`                     datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '实际开始时间',
    `act_end_time`                       datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '实际结束时间',
    `create_time`                        datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                        datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                      bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                            int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_procedure_employee_plan_id`) USING BTREE,
    KEY `idx_process_order_no` (`process_order_no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='工序人员排产计划表';


ALTER TABLE `cn_scm`.`process_order`
    ADD COLUMN `missing_information`                     VARCHAR(255)                                                 NOT NULL DEFAULT '' COMMENT 'OUT_OF_STOCK(无库存),NOT_EXIST_PROCESS(无工序信息),NOT_EXIST_MATERIAL(无原料信息)' AFTER `process_order_no`,
    ADD COLUMN `is_receive_material`                     VARCHAR(32)                                                  NOT NULL DEFAULT '' COMMENT 'TRUE(已回料),FALSE(未回料),NO_RETURN_REQUIRED(无需回料)' AFTER `process_order_no`,
    ADD COLUMN `over_plan`                               VARCHAR(32)                                                  NOT NULL DEFAULT '' COMMENT 'TRUE(已超额),FALSE(未超额)' AFTER `process_order_no`,
    ADD COLUMN `need_process_plan`                       VARCHAR(32)                                                  NOT NULL DEFAULT '' COMMENT 'TRUE(需要排产),FALSE(无需排产)' AFTER `process_order_no`,
    ADD COLUMN `process_plan_time`                       DATETIME                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '排产时间' AFTER `material_back_status`,
    ADD COLUMN `process_plan_earliest_expect_begin_time` DATETIME                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '排产工序最早开始时间' AFTER `process_plan_time`,
    ADD COLUMN `process_plan_latest_expect_end_time`     DATETIME                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '排产工序最晚完成时间' AFTER `process_plan_earliest_expect_begin_time`,
    ADD COLUMN `process_plan_delay`                      VARCHAR(32) COLLATE utf8mb4_general_ci                       NOT NULL DEFAULT '' COMMENT 'TRUE(已延误),FALSE(无延误)' AFTER `process_plan_latest_expect_end_time`,
    ADD COLUMN `process_completion_time`                 DATETIME                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '工序完成时间' AFTER `process_plan_latest_expect_end_time`,
    ADD COLUMN `container_code`                          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '容器编码' AFTER `available_product_num`;

ALTER TABLE `cn_scm`.`process`
    ADD COLUMN `complex_coefficient` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '复杂系数' AFTER `process_status`,
    ADD COLUMN `setup_duration`      int          NOT NULL DEFAULT '0' COMMENT '工序整备时长（分钟）' after `commission`;

ALTER TABLE `cn_scm`.`process_order_scan`
    ADD COLUMN `processing_time`     DATETIME                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '加工时间' AFTER `receipt_username`,
    ADD COLUMN `processing_user`     VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '加工人 id' AFTER `processing_time`,
    ADD COLUMN `processing_username` VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '加工人名称' AFTER `processing_user`;