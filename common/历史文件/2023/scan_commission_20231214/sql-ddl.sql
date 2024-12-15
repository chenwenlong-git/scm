CREATE TABLE `process_commission_rule`
(
    `process_commission_rule_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `process_code`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工序代码',
    `commission_level`           int                                                          NOT NULL DEFAULT '0' COMMENT '提成等级',
    `start_quantity`             int                                                          NOT NULL DEFAULT '0' COMMENT '数量起始值',
    `end_quantity`               int                                                          NOT NULL DEFAULT '0' COMMENT '数量结束值',
    `commission_coefficient`     decimal(10, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '提成系数',
    `create_time`                datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`              bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                    int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_commission_rule_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='工序提成规则';


CREATE TABLE `scan_commission_detail`
(
    `scan_commission_detail_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `process_order_scan_id`     bigint unsigned                                              NOT NULL COMMENT '扫码记录ID',
    `commission_category`       varchar(255) COLLATE utf8mb4_general_ci                      NOT NULL DEFAULT '' COMMENT '提成类目',
    `commission_attribute`      varchar(255) COLLATE utf8mb4_general_ci                      NOT NULL DEFAULT '' COMMENT '提成属性',
    `commission_rule`           varchar(500) COLLATE utf8mb4_general_ci                      NOT NULL DEFAULT '' COMMENT '提成规则',
    `quantity`                  int                                                          NOT NULL DEFAULT '0' COMMENT '数量',
    `unit_commission`           decimal(10, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '单位提成',
    `total_amount`              decimal(10, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '提成总额',
    `create_time`               datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`               datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`             bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                   int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '乐观锁版本',
    PRIMARY KEY (`scan_commission_detail_id`) USING BTREE,
    KEY `idx_scan_commission_detail_1` (`process_order_scan_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='工序扫码提成明细';

CREATE TABLE process_settle_detail_report
(
    `process_settle_detail_report_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `process_settle_order_id`         bigint                                                        NOT NULL COMMENT '结算单主键id',
    `process_settle_order_item_id`    bigint unsigned                                               NOT NULL COMMENT '结算单明细主键id',
    `complete_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '0' COMMENT '完成人的用户',
    `complete_username`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '完成人的用户名',
    `process_code`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '工序代码',
    `process_label`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '工序标签',
    `process_name`                    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工序名称',
    `quality_goods_cnt`               int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '正品数量',
    `first_level_quality_goods_cnt`   int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '一级提成正品数量',
    `first_level_total_commission`    DECIMAL(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '一级提成总金额',
    `second_level_quality_goods_cnt`  int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '二级提成正品数量',
    `second_level_total_commission`   DECIMAL(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '二级提成总金额',
    `create_time`                     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                   bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                         int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_settle_detail_report_id`) USING BTREE,
    KEY `idx_process_settle_detail_report_1` (`process_settle_order_item_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='加工结算详情报表';


ALTER TABLE process_settle_order_scan
    ADD COLUMN process_order_scan_id BIGINT NOT NULL DEFAULT 0 COMMENT '扫码记录主键id' AFTER process_settle_order_scan_id;