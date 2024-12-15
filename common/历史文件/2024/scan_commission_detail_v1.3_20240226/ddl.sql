CREATE TABLE `process_composite`
(
    `process_composite_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `parent_process_id`    bigint unsigned                                              NOT NULL COMMENT '组合工序ID',
    `parent_process_code`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '组合工序代码',
    `sub_process_id`       bigint unsigned                                              NOT NULL COMMENT '非组合工序ID',
    `sub_process_code`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '组合工序代码',
    `create_time`          datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`          datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`        bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`              int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '乐观锁版本',
    PRIMARY KEY (`process_composite_id`) USING BTREE,
    KEY `idx_process_composite_1` (`parent_process_code`),
    KEY `idx_process_composite_2` (`sub_process_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='复合工序关系表';


CREATE TABLE `process_order_scan_relate`
(
    `process_order_scan_relate_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `process_order_scan_id`        bigint                                                       NOT NULL DEFAULT '0' COMMENT 'process_order_scan表主键id',
    `process_code`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联的工序代码',
    `process_commission`           decimal(10, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '关联的工序提成',
    `extra_commission`             decimal(10, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '额外提成单价',
    `quality_goods_cnt`            int unsigned                                                 NOT NULL DEFAULT '0' COMMENT '正品数量',
    `complete_time`                datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '扫码完成时间',
    `complete_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扫码完成人 id',
    `complete_username`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扫码完成人名称',
    `create_time`                  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                      int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_order_scan_relate_id`) USING BTREE,
    KEY `idx_process_order_scan_relate_1` (`process_order_scan_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='工序扫码关联表';

alter table process
    add column
        `process_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工序类型'
        after process_label;