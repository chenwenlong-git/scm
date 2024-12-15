CREATE TABLE `finance_payment_item`
(
    `finance_payment_item_id`       bigint unsigned                                               NOT NULL COMMENT '主键id',
    `payment_biz_no`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '关联业务单号',
    `payment_biz_type`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '付款业务类型:PREPAYMENT(预付款),',
    `supplier_code`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '预付款对象(供应商code)',
    `account`                       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '收款账户',
    `subject`                       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '主体',
    `supplier_payment_account_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '账号类型',
    `bank_name`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '开户银行',
    `account_username`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '户名',
    `bank_subbranch_name`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '银行支行',
    `account_remarks`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '账号备注',
    `payment_subject`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '付款主体',
    `payment_reason`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '付款事由',
    `payment_remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '付款备注说明',
    `recipient_subject`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收方主体',
    `payment_money`                 decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '付款金额',
    `currency`                      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '币种',
    `exchange_rate`                 decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '汇率',
    `rmb_payment_money`             decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '汇率转换人民币付款金额',
    `payment_date`                  datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '付款时间',
    `bank_province`                 varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '银行所在地区 省份/州',
    `bank_city`                     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '银行所在地区 城市',
    `bank_area`                     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '银行所在地区 区',
    `create_time`                   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                 bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                       int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`finance_payment_item_id`) USING BTREE,
    KEY `idx_finance_payment_item_2` (`supplier_code`) USING BTREE,
    KEY `idx_finance_payment_item_1` (`payment_biz_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='财务相关单付款明细';

CREATE TABLE `finance_prepayment_order`
(
    `finance_prepayment_order_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `prepayment_order_no`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '预付款单号',
    `prepayment_order_status`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '预付款状态:TO_BE_FOLLOW_SUBMIT(待跟单提交),APPROVING(审批中),TO_BE_PAYMENT(待付款),PARTIAL_PAYMENT(部分付款),FULL_PAYMENT(全部付款),DELETED(作废),',
    `supplier_code`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '预付款对象(供应商code)',
    `prepayment_type`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '预付类型:PREPAYMENT_OF_GOODS(预付货款),',
    `prepayment_reason`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '预付款事由',
    `prepayment_money`            decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '预付金额(rmb)',
    `payment_money`               decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '付款金额(rmb)',
    `workflow_no`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '飞书审批单号',
    `can_deduction_money`         decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '可抵扣金额(rmb)',
    `deduction_status`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '抵扣状态:UNASSOCIATED(未关联对账单),ASSOCIATED(已关联对账单),',
    `apply_date`                  datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '申请日期',
    `prepayment_remark`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '预付款备注',
    `ctrl_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '当前操作人',
    `task_id`                     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审批taskId',
    `follow_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '跟单',
    `create_time`                 datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                 datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`               bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                     int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`finance_prepayment_order_id`) USING BTREE,
    KEY `idx_finance_prepayment_order_1` (`prepayment_order_no`) USING BTREE,
    KEY `idx_finance_prepayment_order_2` (`supplier_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='预付款单';

CREATE TABLE `finance_prepayment_order_item`
(
    `finance_prepayment_order_item_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `prepayment_order_no`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '预付款单号',
    `supplier_code`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '预付款对象(供应商code)',
    `account`                          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '收款账户',
    `subject`                          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '主体',
    `supplier_payment_account_type`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '账号类型',
    `bank_name`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '银行账号',
    `account_username`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '户名',
    `bank_subbranch_name`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '银行支行',
    `account_remarks`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '账号备注',
    `bank_province`                    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '银行所在地区 省份/州',
    `bank_city`                        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '银行所在地区 城市',
    `bank_area`                        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '银行所在地区 区',
    `prepayment_money`                 decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '付款金额',
    `currency`                         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '币种:RMB(人民币),USD(美元),',
    `exchange_rate`                    decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '汇率',
    `rmb_prepayment_money`             decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '收款金额(rmb)',
    `expected_prepayment_date`         datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '期望收款时间',
    `create_time`                      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                    bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                          int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`finance_prepayment_order_item_id`) USING BTREE,
    KEY `idx_finance_prepayment_order_item_1` (`prepayment_order_no`) USING BTREE,
    KEY `idx_finance_prepayment_order_item_2` (`supplier_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='预付款单明细';

CREATE TABLE `finance_reco_order`
(
    `finance_reco_order_id`     bigint unsigned                                               NOT NULL COMMENT '主键id',
    `finance_reco_order_no`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '对账单号',
    `finance_reco_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '对账单状态:PENDING_ORDER("待收单"),WAIT_SUBMIT("待跟单提交"),WAIT_CONFIRM("待工厂确认"),UNDER_REVIEW("审批中"),COMPLETE("对账完成"),DELETE("已作废")',
    `supplier_code`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商代码',
    `reconciliation_cycle`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '对账周期',
    `reconciliation_start_time` datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '对账周期开始时间',
    `reconciliation_end_time`   datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '对账周期结束时间',
    `collect_order_time`        datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '收单时间',
    `settle_price`              decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '对账结算金额(有可能负数)',
    `receive_price`             decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '对账应收金额',
    `pay_price`                 decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '对账应付金额',
    `finance_settle_order_no`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '关联结算单号',
    `workflow_no`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '飞书工作流单号',
    `submit_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '提交人',
    `submit_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '提交人的名称',
    `submit_time`               datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '提交时间',
    `confirm_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '确认人',
    `confirm_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '确认人的名称',
    `confirm_time`              datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '确认时间',
    `supplier_confirm_time`     datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '供应商确认时间',
    `complete_time`             datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '对账完成时间',
    `ctrl_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '当前操作人',
    `task_id`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审批taskId',
    `follow_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '跟单人',
    `follow_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '跟单人的名称',
    `comment`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工厂确认意见',
    `create_time`               datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`               datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`             bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                   int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`finance_reco_order_id`) USING BTREE,
    KEY `idx_finance_reco_order_1` (`finance_reco_order_no`) USING BTREE,
    KEY `idx_finance_reco_order_2` (`supplier_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='财务对账单表';

CREATE TABLE `finance_reco_order_item`
(
    `finance_reco_order_item_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `finance_reco_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '对账单号',
    `supplier_code`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商代码',
    `finance_reco_fund_type`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '款项类型',
    `collect_order_type`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收单类型',
    `collect_order_no`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收单单据',
    `finance_reco_pay_type`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收付类型',
    `num`                        int                                                          NOT NULL DEFAULT '0' COMMENT '单据总数量',
    `total_price`                decimal(13, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '单据总金额（对账金额）',
    `create_time`                datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`              bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                    int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`finance_reco_order_item_id`) USING BTREE,
    KEY `idx_finance_reco_order_item_1` (`finance_reco_order_no`) USING BTREE,
    KEY `idx_finance_reco_order_item_2` (`supplier_code`) USING BTREE,
    KEY `idx_finance_reco_order_item_3` (`collect_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='财务对账单明细表';


CREATE TABLE `finance_reco_order_item_inspect`
(
    `finance_reco_order_item_inspect_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `finance_reco_order_item_id`         bigint                                                        NOT NULL COMMENT '关联财务对账单明细ID',
    `finance_reco_order_item_sku_id`     bigint                                                        NOT NULL COMMENT '关联财务对账单明细SKU ID',
    `finance_reco_order_no`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '对账单号',
    `reco_order_inspect_type`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '检验类型',
    `original_value`                     decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '收单数据',
    `inspect_value`                      decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '检验数据',
    `inspect_result`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '检验结果:TRUE(通过),FALSE(不通过)',
    `inspect_result_rule`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '检验结果规则',
    `create_time`                        datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                        datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                      bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                            int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`finance_reco_order_item_inspect_id`) USING BTREE,
    KEY `idx_finance_reco_order_item_inspect_1` (`finance_reco_order_item_id`) USING BTREE,
    KEY `idx_finance_reco_order_item_inspect_2` (`finance_reco_order_item_sku_id`) USING BTREE,
    KEY `idx_finance_reco_order_item_inspect_3` (`finance_reco_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='财务对账单明细校验表';

CREATE TABLE `finance_reco_order_item_sku`
(
    `finance_reco_order_item_sku_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `finance_reco_order_item_id`     bigint                                                        NOT NULL COMMENT '关联财务对账单明细ID',
    `finance_reco_order_no`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '对账单号',
    `collect_order_no`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '收单单据',
    `finance_reco_fund_type`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '款项类型',
    `collect_order_type`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '收单类型',
    `finance_reco_pay_type`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '收付类型',
    `reco_order_item_sku_status`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '状态',
    `supplier_code`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商代码',
    `num`                            int                                                           NOT NULL DEFAULT '0' COMMENT '数量',
    `price`                          decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '单价',
    `total_price`                    decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '收单金额（单价*数量）',
    `sku`                            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'SKU',
    `sku_batch_code`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku批次码',
    `remarks`                        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
    `association_time`               datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '收单关联时间',
    `collect_order_item_id`          bigint                                                        NOT NULL DEFAULT '0' COMMENT '关联单明细的ID',
    `create_time`                    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                  bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                        int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`finance_reco_order_item_sku_id`) USING BTREE,
    KEY `idx_finance_reco_order_item_sku_1` (`finance_reco_order_item_id`) USING BTREE,
    KEY `idx_finance_reco_order_item_sku_2` (`finance_reco_order_no`) USING BTREE,
    KEY `idx_finance_reco_order_item_sku_3` (`collect_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='财务对账单明细sku详情表';

CREATE TABLE `finance_reco_prepayment`
(
    `finance_reco_prepayment_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `finance_reco_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '对账单号',
    `prepayment_order_no`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '预付款单号',
    `supplier_code`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商编码',
    `deduction_money`            decimal(13, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '抵扣金额(rmb)',
    `create_time`                datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`              bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                    int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`finance_reco_prepayment_id`) USING BTREE,
    UNIQUE KEY `uk_finance_reco_prepayment_1` (`finance_reco_order_no`, `prepayment_order_no`, `del_timestamp`) USING BTREE,
    KEY `idx_finance_reco_prepayment_1` (`prepayment_order_no`) USING BTREE,
    KEY `idx_finance_reco_prepayment_2` (`finance_reco_order_no`) USING BTREE,
    KEY `idx_finance_reco_prepayment_3` (`supplier_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='对账-预付款关联表';

CREATE TABLE `finance_settle_order`
(
    `finance_settle_order_id`     bigint unsigned                                               NOT NULL COMMENT '主键id',
    `finance_settle_order_no`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '结算单号',
    `finance_settle_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '结算单状态',
    `supplier_code`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商代码',
    `settle_amount`               decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '总结算金额=应付结算汇总-应收结算汇总',
    `receive_amount`              decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '应收结算汇总',
    `pay_amount`                  decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '应付结算汇总',
    `follow_user`                 varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '跟进采购员',
    `supplier_submit_time`        datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '工厂提交时间',
    `follower_confirm_user`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '跟单确认人',
    `follower_confirm_username`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '跟单确认人名称',
    `follower_confirm_time`       datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '跟单确认时间',
    `workflow_no`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '飞书审批单号',
    `apply_time`                  datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '工作流申请时间',
    `ctrl_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '当前操作人',
    `ctrl_username`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '当前操作人名称',
    `task_id`                     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审批taskId',
    `workflow_finish_time`        datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '审核完成时间',
    `settle_finish_time`          datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '结算完成时间',
    `create_time`                 datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                 datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`               bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                     int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`finance_settle_order_id`) USING BTREE,
    KEY `idx_finance_settle_order_1` (`finance_settle_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='财务结算单表';

CREATE TABLE `finance_settle_order_item`
(
    `finance_settle_order_item_id`   bigint unsigned                                              NOT NULL COMMENT '主键id',
    `finance_settle_order_no`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '结算单号',
    `finance_settle_order_item_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '明细类型(对账/结转)',
    `business_no`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联单据号',
    `settle_amount`                  decimal(13, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '总结算金额',
    `receive_amount`                 decimal(13, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '应收结算汇总',
    `pay_amount`                     decimal(13, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '应付结算汇总',
    `create_time`                    datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                    datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                  bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                        int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`finance_settle_order_item_id`) USING BTREE,
    KEY `idx_finance_settle_order_item_1` (`finance_settle_order_no`) USING BTREE,
    KEY `idx_finance_settle_order_item_2` (`business_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='财务结算单明细表';

CREATE TABLE `finance_settle_carryover_order`
(
    `finance_settle_carryover_order_id`     bigint unsigned                                              NOT NULL COMMENT '主键id',
    `finance_settle_carryover_order_no`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '结转单号',
    `finance_settle_carryover_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '结转单状态',
    `finance_settle_order_no`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '结算单号',
    `supplier_code`                         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商代码',
    `carryover_amount`                      decimal(10, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '结转金额',
    `available_carryover_amount`            decimal(10, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '可结转金额',
    `create_time`                           datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`                       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                           datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`                       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                         bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                               int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`finance_settle_carryover_order_id`) USING BTREE,
    KEY `idx_finance_settle_carryover_order_1` (`finance_settle_carryover_order_no`) USING BTREE,
    UNIQUE KEY `uk_finance_settle_carryover_order_1` (`finance_settle_order_no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='财务结算结转单表';

CREATE TABLE `finance_settle_order_receive`
(
    `finance_settle_order_receive_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `finance_settle_order_no`         varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '结算单号',
    `supplier_code`                   varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '供应商代码',
    `account`                         varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '收款账户',
    `subject`                         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '主体',
    `supplier_payment_account_type`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '账号类型',
    `bank_name`                       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '银行账号',
    `account_username`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '户名',
    `bank_subbranch_name`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '银行支行',
    `account_remarks`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '账号备注',
    `bank_province`                   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '银行所在地区 省份/州',
    `bank_city`                       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '银行所在地区 城市',
    `bank_area`                       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '银行所在地区 区',
    `currency`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '币种:RMB(人民币),USD(美元),',
    `currency_amount`                 decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '币种金额',
    `exchange_rate`                   decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '汇率',
    `expect_receive_amount`           decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '预计收款金额（人民币）',
    `expect_receive_date`             datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '期望收款时间',
    `create_time`                     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                     varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`                 varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                     varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`                 varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                   bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                         int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`finance_settle_order_receive_id`) USING BTREE,
    KEY `idx_finance_settle_order_receive_1` (`finance_settle_order_no`) USING BTREE,
    KEY `idx_finance_settle_order_receive_2` (`supplier_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='对账单收款明细';


CREATE TABLE `supplier_subject`
(
    `supplier_subject_id`   bigint unsigned                                               NOT NULL COMMENT '主键id',
    `supplier_code`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商代码',
    `supplier_subject_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '主体类型',
    `subject`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '主体',
    `legal_person`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '法定代表人',
    `contacts_name`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '联系人',
    `contacts_phone`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '联系人电话',
    `register_money`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '注册资金',
    `business_scope`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '经营范围',
    `business_address`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '经营地址',
    `credit_code`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '社会信用代码',
    `supplier_export`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '进出口资质：否(FALSE)、是(TRUE)',
    `supplier_invoicing`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '开票资质：否(FALSE)、是(TRUE)',
    `tax_point`             decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '税点',
    `create_time`           datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`           datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`         bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`               int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplier_subject_id`) USING BTREE,
    UNIQUE KEY `uk_supplier_subject_1` (`subject`, `del_timestamp`) USING BTREE,
    KEY `idx_supplier_subject_1` (`supplier_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供应商主体信息';


ALTER TABLE `cn_scm`.`supplier`
    ADD COLUMN `settle_time`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '结算日期(字符串)每月的某一天' AFTER `supplier_alias`,
    ADD COLUMN `reconciliation_cycle` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '对账周期:MONTH("月结"),week("周结")' AFTER `settle_time`;

ALTER TABLE `cn_scm`.`supplier_payment_account`
    ADD COLUMN `subject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '主体' AFTER `feishu_audit_order_no`;

ALTER TABLE `cn_scm`.`purchase_deliver_order`
    ADD INDEX `idx_purchase_deliver_order_3` (`warehousing_time`) USING BTREE;

ALTER TABLE `cn_scm`.`feishu_audit_order`
    MODIFY COLUMN `latest_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '最后一次备注（包含审批通过、失败原因)' AFTER `fail_msg`;