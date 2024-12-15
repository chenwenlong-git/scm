CREATE TABLE `qc_detail`
(
    `qc_detail_id`          bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '质检单详情id',
    `qc_order_no`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '质检单号',
    `container_code`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '容器编码',
    `batch_code`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '批次码',
    `spu`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '赫特spu',
    `sku_code`              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '赫特sku',
    `amount`                int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '质检总数',
    `wait_amount`           int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '待质检数量',
    `pass_amount`           int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '通过数量',
    `not_pass_amount`       int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '不通过数量',
    `qc_result`             varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '质检结果:PASSED(合格),FEW_NOT_PASSED(部分不合格),NOT_PASSED(不合格)',
    `qc_not_passed_reason`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '质检不合格原因',
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
    `picture_ids`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '图片id,多个用英文逗号分隔',
    `weight`                decimal(8, 2) unsigned                                        NOT NULL DEFAULT '0.00' COMMENT 'sku重量，克',
    `relation_qc_detail_id` bigint                                                        NOT NULL DEFAULT '0' COMMENT '不合格明细关联的明细id',
    `create_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人编码',
    `create_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人编码',
    `update_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `create_time`           datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`           datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`               int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '乐观锁版本',
    `del_timestamp`         bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '删除标识，0正常、非0删除',
    PRIMARY KEY (`qc_detail_id`) USING BTREE,
    KEY `idx_qc_detail_1` (`qc_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='质检单详情';

CREATE TABLE `qc_on_shelves_order`
(
    `qc_on_shelves_order_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `qc_order_no`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '质检单号',
    `on_shelves_order_no`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '上架单号',
    `plan_amount`            int unsigned                                                 NOT NULL DEFAULT '0' COMMENT '上架数量',
    `type`                   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT '' COMMENT '上架单生成类型：让步(CONCESSION)',
    `create_time`            datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`            datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`          bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`qc_on_shelves_order_id`) USING BTREE,
    KEY `idx_qc_on_shelves_order_1` (`qc_order_no`) USING BTREE,
    KEY `idx_qc_on_shelves_order_2` (`on_shelves_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='质检单上架单关联表';

CREATE TABLE `qc_order`
(
    `qc_order_id`      bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '质检单id',
    `qc_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '质检单号',
    `warehouse_code`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库编码',
    `receive_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '收货单号',
    `process_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '加工单号',
    `qc_type`          varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '质检类型：ALL_CHECK（全检）、SAMPLE_CHECK（抽检）、NOT_CHECK（免检）',
    `qc_amount`        int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '质检数量',
    `qc_state`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '质检状态：WAIT_HAND_OVER(待交接),TO_BE_QC(待质检),QCING(质检中),TO_BE_AUDIT(待审核),AUDITED(已确认)',
    `qc_result`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '质检结果:PASSED(合格),FEW_NOT_PASSED(部分不合格),NOT_PASSED(不合格)',
    `hand_over_time`   datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '确认交接时间',
    `task_finish_time` datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '质检任务完成时间',
    `audit_time`       datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '审核时间',
    `hand_over_user`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '交接人',
    `operator`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '质检人',
    `operator_name`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '质检人',
    `auditor`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '审核人',
    `sku_dev_type`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '商品开发类型：NORMAL(常规),LIMITED(limited)',
    `create_user`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人编码',
    `create_username`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_user`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人编码',
    `update_username`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `create_time`      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '乐观锁版本',
    `del_timestamp`    bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '删除标识，0正常、非0删除',
    PRIMARY KEY (`qc_order_id`) USING BTREE,
    UNIQUE KEY `uk_qc_order_1` (`qc_order_no`) USING BTREE,
    KEY `idx_qc_order_1` (`process_order_no`) USING BTREE,
    KEY `idx_qc_order_2` (`update_time`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='质检单';

CREATE TABLE `qc_order_defect_config`
(
    `qc_order_defect_config_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `defect_category`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '次品类别',
    `defect_code`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '次品代码',
    `defect_status`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '次品配置状态',
    `parent_defect_config_id`   bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '父级id',
    `create_time`               datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`               datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`             bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                   int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`qc_order_defect_config_id`) USING BTREE,
    UNIQUE KEY `uk_qc_order_defect_config_1` (`defect_code`, `del_timestamp`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='质检次品配置';

CREATE TABLE `qc_receive_order`
(
    `qc_receive_order_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `qc_order_no`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '质检单号',
    `receive_order_no`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货单号',
    `receive_type`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '入库类型:BULK(大货入库),TRANSFER(调拨入库),PROCESS_MATERIAL(原料入库),PROCESS_PRODUCT(加工成品入库),SAMPLE(样品入库),SALE_RETURN(销退入库),PROFIT(盘盈入库),RETURN(归还入库),DEFECTIVE_PROCESS_PRODUCT(次品加工入库),DOWN_RANK(降档入库),OTHER(其他入库)',
    `supplier_code`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商编码',
    `delivery_order_no`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '出库单号',
    `goods_category`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品类目',
    `scm_biz_no`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应链单据号',
    `create_time`         datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`         datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`       bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`             int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`qc_receive_order_id`) USING BTREE,
    KEY `idx_qc_receive_order_1` (`qc_order_no`) USING BTREE,
    KEY `idx_qc_receive_order_2` (`receive_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='质检单收货单关联表';


CREATE TABLE `produce_data_spec`
(
    `produce_data_spec_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `spu`                  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu',
    `sku`                  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `product_link`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '产品规格书链接',
    `create_time`          datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`          datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`        bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`              int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`produce_data_spec_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='生产信息产品规格书';

