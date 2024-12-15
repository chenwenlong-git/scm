ALTER TABLE `cn_scm`.`purchase_child_order`
    MODIFY COLUMN `purchase_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购单状态:WAIT_ORDER(待下单),WAIT_APPROVE(待审核),WAIT_CONFIRM(待确认),WAIT_RECEIVE_ORDER(待接单),WAIT_SCHEDULING(待排产),WAIT_COMMISSIONING(待投产),COMMISSION(前处理),PRETREATMENT(后处理),SEWING(三联机中),AFTER_TREATMENT(高针中),POST_QC(后整质检中),WAIT_DELIVER(待发货),WAIT_RECEIPT(待收货),RECEIPTED(已收货),WAIT_QC(待质检),WAIT_WAREHOUSING(待入库),WAREHOUSED(已入库),SETTLE(已结算),RETURN(已退货),DELETE(已作废),FINISH(已完结),\n' AFTER `sample_child_order_no`,
    MODIFY COLUMN `is_first_order` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否首单:TRUE(是),FALSE(否),' AFTER `purchase_order_status`,
    MODIFY COLUMN `is_urgent_order` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否加急:TRUE(是),FALSE(否),' AFTER `is_first_order`,
    MODIFY COLUMN `is_normal_order` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否正常采购:TRUE(是),FALSE(否),' AFTER `is_urgent_order`,
    MODIFY COLUMN `purchase_biz_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '拆分类型:PROCESS(加工采购),PRODUCT(大货采购),' AFTER `platform`,
    MODIFY COLUMN `is_direct_send` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否直发:TRUE(是),FALSE(否),' AFTER `raw_warehouse_name`,
    MODIFY COLUMN `is_upload_overseas_msg` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否上传海外仓文件信息:TRUE(是),FALSE(否),' AFTER `is_direct_send`;

ALTER TABLE `cn_scm`.`purchase_child_order_item`
    MODIFY COLUMN `discount_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '优惠类型:NO_DISCOUNT(无折扣),PROVIDE_RAW(我方提供原料),' AFTER `purchase_price`;

ALTER TABLE `cn_scm`.`purchase_child_order_raw`
    MODIFY COLUMN `purchase_raw_biz_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购原料类型:DEMAND(需求原料),ACTUAL_DELIVER(实发原料),' AFTER `delivery_cnt`;

ALTER TABLE `cn_scm`.`purchase_deliver_order`
    MODIFY COLUMN `deliver_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发货状态:WAIT_DELIVER(待发货),WAIT_RECEIVE(待收货),WAIT_QC(待质检),WAIT_WAREHOUSING(待入库),WAREHOUSED(已入库),DELETED(作废),' AFTER `purchase_receipt_order_no`,
    MODIFY COLUMN `is_direct_send` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否直发:TRUE(是),FALSE(否),' AFTER `deliver_date`,
    MODIFY COLUMN `has_shipping_mark` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否直发:TRUE(是),FALSE(否),' AFTER `is_direct_send`;

ALTER TABLE `cn_scm`.`purchase_parent_order`
    MODIFY COLUMN `sku_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku类型:SKU(商品sku),SM_SKU(辅料sku),' AFTER `spu`,
    MODIFY COLUMN `purchase_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购单状态:WAIT_ORDER(待下单),WAIT_APPROVE(待审核),WAIT_CONFIRM(待确认),WAIT_RECEIVE_ORDER(待接单),WAIT_SCHEDULING(待排产),WAIT_COMMISSIONING(待投产),COMMISSION(前处理),PRETREATMENT(后处理),SEWING(三联机中),AFTER_TREATMENT(高针中),POST_QC(后整质检中),WAIT_DELIVER(待发货),WAIT_RECEIPT(待收货),RECEIPTED(已收货),WAIT_QC(待质检),WAIT_WAREHOUSING(待入库),WAREHOUSED(已入库),SETTLE(已结算),RETURN(已退货),DELETE(已作废),FINISH(已完结),' AFTER `deliver_date`,
    MODIFY COLUMN `is_first_order` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否首单:TRUE(是),FALSE(否),' AFTER `order_remarks`,
    MODIFY COLUMN `is_urgent_order` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否加急:TRUE(是),FALSE(否),' AFTER `is_first_order`,
    MODIFY COLUMN `is_normal_order` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否正常采购:TRUE(是),FALSE(否),' AFTER `is_urgent_order`,
    MODIFY COLUMN `is_direct_send` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否直发:TRUE(是),FALSE(否),' AFTER `is_normal_order`;

ALTER TABLE `cn_scm`.`purchase_raw_receipt_order`
    MODIFY COLUMN `receipt_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货状态:WAIT_RECEIVE(待收货),RECEIPTED(已收货),' AFTER `purchase_raw_receipt_order_no`,
    MODIFY COLUMN `raw_receipt_biz_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原料收货类型:PURCHASE(采购原料收货),SAMPLE(样品原料收货),' AFTER `supplier_name`;

ALTER TABLE `cn_scm`.`purchase_return_order`
    MODIFY COLUMN `return_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货状态:WAIT_RECEIVE(待收货),RECEIPTED(已收货),' AFTER `purchase_return_order_no`;

ALTER TABLE `cn_scm`.`scm_image`
    MODIFY COLUMN `image_biz_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '业务枚举:PURCHASE_SETTLE_PAY(采购结算单凭证),SUPPLEMENT_ORDER(补款单凭证),DEDUCT_ORDER(扣款单凭证),PURCHASE_PARENT_ORDER(采购需求单),SAMPLE_PARENT_ORDER(样品需求单),SAMPLE_CTRL_SUCCESS(打样成功),SUPPLIER_LICENSE(供应商企业营业执照),SAMPLE_CHANGE(样品改善图片),OVERSEAS_SHIPPING_MARK(海外仓箱唛pdf),OVERSEAS_BAR_CODE(海外仓条码pdf),OVERSEAS_TRACKING(海外运单pdf),' AFTER `file_code`;

ALTER TABLE `cn_scm`.`shipping_mark`
    MODIFY COLUMN `shipping_mark_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '箱唛状态:WAIT_DELIVER(待发货),DELIVERED(已发货),DELETED(作废),' AFTER `shipping_mark_no`,
    MODIFY COLUMN `shipping_mark_biz_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '箱唛单据类型:PURCHASE_CHILD(采购子单),SAMPLE_CHILD(样品子单),' AFTER `shipping_mark_status`,
    MODIFY COLUMN `is_direct_send` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否直发:TRUE(是),FALSE(否),' AFTER `warehouse_types`;

ALTER TABLE `cn_scm`.`sm_category`
    MODIFY COLUMN `category_level` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '品类级别:FIRST_CATEGORY(一级品类),SECOND_CATEGORY(二级品类),THIRD_CATEGORY(三级品类),' AFTER `parent_category_code`;

ALTER TABLE `cn_scm`.`subsidiary_material`
    MODIFY COLUMN `material_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '辅料类型:BASE(基础类型),COMBINE(组合产品),' AFTER `category_code`,
    MODIFY COLUMN `measurement` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '计量单位:VOLUME(卷),CENTIMETER(厘米),BAG(袋),BUCKET(桶),BRANCH(支),SHEET(张),HANDFUL(把),BOX(盒),PAIR(双),STRIP(条),BOTTLE(瓶),METRE(米),INDIVIDUAL(个),' AFTER `material_type`,
    MODIFY COLUMN `use_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '使用类型:COMMONLY_USED(常用),NOT_COMMONLY_USED(不常用),' AFTER `unit`,
    MODIFY COLUMN `shelves_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '上架类型:ON_SHELVES(上架),DOWN_SHELVES(下架),' AFTER `sm_sku`;


ALTER TABLE `cn_scm`.`goods_process`
    MODIFY COLUMN `goods_process_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '状态：BINDED(绑定),UNBINDED(未绑定),' AFTER `category_name`;

ALTER TABLE `cn_scm`.`process`
    MODIFY COLUMN `process_label` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工序标签：PLUCK_HAIR(拔毛),BRAIDED_HAIR(编发造型),SEWING_HEAD(缝头套),CLIPS(卡子/松紧带),ICON_PERM(离子烫),FLOATING(漂扣),CLEAN(清洗),DYEING(染色),DOLL_HAIR(娃娃发),BLOCK_LACE(修剪发块蕾丝),MODELING(造型),SPECIAL_PROCEDURE(特殊工序),' AFTER `process_id`,
    MODIFY COLUMN `process_first` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '一级工序：WAIT_HANDLE(前处理),HANDLING(缝制中),HANDLED(后处理),' AFTER `process_label`,
    MODIFY COLUMN `process_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '状态：ENABLED(启用),DISABLED(禁用),' AFTER `commission`;


ALTER TABLE `cn_scm`.`process_desc`
    MODIFY COLUMN `process_first` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '一级工序：WAIT_HANDLE(前处理),HANDLING(缝制中),HANDLED(后处理),' AFTER `name`;


ALTER TABLE `cn_scm`.`process_material_back`
    MODIFY COLUMN `back_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '归还状态：WAIT_BACK(待归还),COMPLETED_BACK(已归还),' AFTER `receipt_no`;

ALTER TABLE `cn_scm`.`process_material_receipt`
    MODIFY COLUMN `process_material_receipt_status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '状态：WAIT_RECEIVE(待收货),RECEIVED(已收货),' AFTER `delivery_no`;

ALTER TABLE `cn_scm`.`process_order`
    MODIFY COLUMN `process_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '状态：LACK(缺货),WAIT_ORDER(待下单),WAIT_PRODUCE(待投产),PRODUCED(已投产),PROCESSING(加工中),WAIT_MOVING(完工待交接),CHECKING(后整质检中),WAIT_DELIVERY(待发货),WAIT_RECEIPT(待收货),WAIT_STORE(待入库),STORED(已入库),DELETED(已作废),REWORKING(返工中),' AFTER `process_order_no`,
    MODIFY COLUMN `process_order_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '类型：NORMAL(常规),EXTRA(补单),LIMITED(limited),REWORKING(非limited返工),LIMITED_REWORKING(limited返工),' AFTER `process_order_status`,
    MODIFY COLUMN `material_back_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原料归还状态：NO_BACK(无需归还),PARTIAL_BACK(部分归还),UN_BACK(未归还),' AFTER `current_process_label`;


ALTER TABLE `cn_scm`.`process_order_procedure`
    MODIFY COLUMN `process_label` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工序标签：PLUCK_HAIR(拔毛),BRAIDED_HAIR(编发造型),SEWING_HEAD(缝头套),CLIPS(卡子/松紧带),ICON_PERM(离子烫),FLOATING(漂扣),CLEAN(清洗),DYEING(染色),DOLL_HAIR(娃娃发),BLOCK_LACE(修剪发块蕾丝),MODELING(造型),SPECIAL_PROCEDURE(特殊工序),' AFTER `process_name`;

ALTER TABLE `cn_scm`.`process_order_scan`
    MODIFY COLUMN `process_label` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联的工序标签：PLUCK_HAIR(拔毛),BRAIDED_HAIR(编发造型),SEWING_HEAD(缝头套),CLIPS(卡子/松紧带),ICON_PERM(离子烫),FLOATING(漂扣),CLEAN(清洗),DYEING(染色),DOLL_HAIR(娃娃发),BLOCK_LACE(修剪发块蕾丝),MODELING(造型),SPECIAL_PROCEDURE(特殊工序),' AFTER `process_order_no`,
    MODIFY COLUMN `process_first` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联的一级工序：WAIT_HANDLE(前处理),HANDLING(缝制中),HANDLED(后处理),' AFTER `process_label`;

ALTER TABLE `cn_scm`.`process_template`
    MODIFY COLUMN `process_template_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '类型：CATEGORY(品类),SKU(商品sku),' AFTER `name`;


ALTER TABLE `cn_scm`.`sample_child_order`
    MODIFY COLUMN `sample_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品单状态：WAIT_DISBURSEMENT(待接款),WAIT_TYPESET(待打版),WAIT_RECEIVE_ORDER(待接单),RECEIVED_ORDER(已接单),TYPESETTING(打版中),WAIT_RECEIVED_SAMPLE(待收样),WAIT_SAMPLE(待选样),SELECTED(已选中),SETTLE(已结算),PROOFING_FAIL(打样失败),REFUSED(已拒绝),DELETED(作废),' AFTER `sample_child_order_no`,
    MODIFY COLUMN `sample_result` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '选样结果：WAIT_SAMPLE(待选样),SAMPLE_SUCCESS(打样成功),SAMPLE_RETURN(失败退样),FAIL_SALE(失败闪售),' AFTER `platform`,
    MODIFY COLUMN `warehouse_types` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库类型：DOMESTIC_SELF_RUN(国内自营),DOMESTIC_THIRD_PARTY(国内三方),FOREIGN_SELF_RUN(海外自营),FOREIGN_THIRD_PARTY(海外三方),MACHINING_WAREHOUSE(加工仓),VIRTUAL_WAREHOUSE(虚拟仓),DEFECTIVE_PROCESS(不良品加工),' AFTER `warehouse_name`,
    MODIFY COLUMN `sample_dev_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '开发类型：DISBURSEMENT_NEW(全新开款),OLD_DERIVATIVE(老款衍生),BRAND_OPTIMIZE(本款优化),NEW_SKU(新开SKU),DEFECTIVE_REOPEN(衍生样开款),SUPPLY_RECOMMEND(供应商自荐),LIMITED(limited),SMALL_INNOVATION(微创新),OUTSOURCING_SAMPLE(委外打样),' AFTER `raw_warehouse_name`,
    MODIFY COLUMN `sample_produce_label` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '生产标签：FIRST(首选),EFFECTIVE(生效),INVALID(失效),' AFTER `sample_dev_type`;

ALTER TABLE `cn_scm`.`sample_child_order_process`
    MODIFY COLUMN `process_label` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工序标签：PLUCK_HAIR(拔毛),BRAIDED_HAIR(编发造型),SEWING_HEAD(缝头套),CLIPS(卡子/松紧带),ICON_PERM(离子烫),FLOATING(漂扣),CLEAN(清洗),DYEING(染色),DOLL_HAIR(娃娃发),BLOCK_LACE(修剪发块蕾丝),MODELING(造型),SPECIAL_PROCEDURE(特殊工序),' AFTER `process_name`;

ALTER TABLE `cn_scm`.`sample_child_order_raw`
    MODIFY COLUMN `sample_raw_biz_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品原料业务类型：FORMULA(原料配方),ACTUAL_DELIVER(实发原料),DEMAND(需求原料),' AFTER `delivery_cnt`;

ALTER TABLE `cn_scm`.`sample_child_order_result`
    MODIFY COLUMN `sample_result_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '状态：WAIT_HANDLE(待处理),HANDLED(已处理),' AFTER `sample_result`,
    MODIFY COLUMN `sample_result` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '选样结果：WAIT_SAMPLE(待选样),SAMPLE_SUCCESS(打样成功),SAMPLE_RETURN(失败退样),FAIL_SALE(失败闪售),' AFTER `sample_child_order_no`;

ALTER TABLE `cn_scm`.`sample_deliver_order`
    MODIFY COLUMN `warehouse_types` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库类型：DOMESTIC_SELF_RUN(国内自营),DOMESTIC_THIRD_PARTY(国内三方),FOREIGN_SELF_RUN(海外自营),FOREIGN_THIRD_PARTY(海外三方),MACHINING_WAREHOUSE(加工仓),VIRTUAL_WAREHOUSE(虚拟仓),DEFECTIVE_PROCESS(不良品加工),' AFTER `warehouse_name`,
    MODIFY COLUMN `sample_deliver_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品发货单状态：WAIT_RECEIVED_SAMPLE(待收样),RECEIVED_SAMPLE(已收样),CANCELED(已取消),' AFTER `sample_deliver_order_no`;

ALTER TABLE `cn_scm`.`sample_parent_order`
    MODIFY COLUMN `sample_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品单状态：WAIT_DISBURSEMENT(待接款),WAIT_TYPESET(待打版),WAIT_RECEIVE_ORDER(待接单),RECEIVED_ORDER(已接单),TYPESETTING(打版中),WAIT_RECEIVED_SAMPLE(待收样),WAIT_SAMPLE(待选样),SELECTED(已选中),SETTLE(已结算),PROOFING_FAIL(打样失败),REFUSED(已拒绝),DELETED(作废),' AFTER `sample_parent_order_no`,
    MODIFY COLUMN `warehouse_types` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库类型：DOMESTIC_SELF_RUN(国内自营),DOMESTIC_THIRD_PARTY(国内三方),FOREIGN_SELF_RUN(海外自营),FOREIGN_THIRD_PARTY(海外三方),MACHINING_WAREHOUSE(加工仓),VIRTUAL_WAREHOUSE(虚拟仓),DEFECTIVE_PROCESS(不良品加工),' AFTER `warehouse_name`,
    MODIFY COLUMN `sample_dev_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '开发类型：DISBURSEMENT_NEW(全新开款),OLD_DERIVATIVE(老款衍生),BRAND_OPTIMIZE(本款优化),NEW_SKU(新开SKU),DEFECTIVE_REOPEN(衍生样开款),SUPPLY_RECOMMEND(供应商自荐),LIMITED(limited),SMALL_INNOVATION(微创新),OUTSOURCING_SAMPLE(委外打样),' AFTER `sample_order_status`;

ALTER TABLE `cn_scm`.`sample_parent_order_process`
    MODIFY COLUMN `process_label` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工序标签：PLUCK_HAIR(拔毛),BRAIDED_HAIR(编发造型),SEWING_HEAD(缝头套),CLIPS(卡子/松紧带),ICON_PERM(离子烫),FLOATING(漂扣),CLEAN(清洗),DYEING(染色),DOLL_HAIR(娃娃发),BLOCK_LACE(修剪发块蕾丝),MODELING(造型),SPECIAL_PROCEDURE(特殊工序),' AFTER `process_name`;

ALTER TABLE `cn_scm`.`sample_receipt_order`
    MODIFY COLUMN `receipt_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品收货单状态：WAIT_RECEIVE(待收货),RECEIPTED(已收货),' AFTER `total_receipt`;

ALTER TABLE `cn_scm`.`sample_return_order`
    MODIFY COLUMN `return_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '退货单状态：WAIT_RECEIVE(待收货),RECEIPTED(已收货)' AFTER `sample_return_order_no`;

ALTER TABLE `cn_scm`.`sample_return_order_item`
    MODIFY COLUMN `return_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货结果：WAIT_RECEIVE(待收货),RECEIPTED(已收货),' AFTER `sample_child_order_no`;

ALTER TABLE `cn_scm`.`supplier`
    MODIFY COLUMN `supplier_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商类型：ONESELF_BUSINESS(自营供应商),COOPERATION(合作供应商),TEMPORARY(临时供应商),' AFTER `supplier_name`,
    MODIFY COLUMN `supplier_grade` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商等级：STRATEGY(战略),IMPORTANT(重要),COMMONLY(一般),ELIMINATE(淘汰),' AFTER `supplier_status`;

ALTER TABLE `cn_scm`.`process_settle_order`
    MODIFY COLUMN `process_settle_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '加工结算单状态：WAIT_SETTLE(待核算),SETTLE_WAIT_EXAMINE(结算待审核),AUDITED(已审核),' AFTER `process_settle_order_no`;

ALTER TABLE `cn_scm`.`process_settle_order_bill`
    MODIFY COLUMN `supplement_deduct_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '单据类型：PRICE(价差款),PROCESS(加工款),QUALITY(品质款),OTHER(其他),PAY(预付款),' AFTER `business_no`;

ALTER TABLE `cn_scm`.`purchase_settle_order`
    MODIFY COLUMN `purchase_settle_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购结算单状态：WAIT_CONFIRM(待对账),WAIT_SETTLE(供应商确认),SETTLE_WAIT_EXAMINE(财务审核),AUDITED(待支付),PART_SETTLE(部分支付),SETTLE(已支付),' AFTER `purchase_settle_order_no`;

ALTER TABLE `cn_scm`.`purchase_settle_order_item`
    MODIFY COLUMN `purchase_settle_item_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联单据类型：PRODUCT_PURCHASE(大货采购单),PROCESS_PURCHASE(加工采购单),SAMPLE(样品采购单),REPLENISH(补款单),DEDUCT(扣款单),' AFTER `business_no`;

ALTER TABLE `cn_scm`.`deduct_order`
    MODIFY COLUMN `deduct_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款类型：PRICE(价差扣款),PROCESS(加工扣款),QUALITY(品质扣款),OTHER(其他),PAY(预付款),' AFTER `deduct_order_no`,
    MODIFY COLUMN `deduct_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款状态：WAIT_SUBMIT(待提交),WAIT_CONFIRM(待确认),WAIT_EXAMINE(待审核),AUDITED(已审核),SETTLE(已结算),' AFTER `deduct_type`;

ALTER TABLE `cn_scm`.`deduct_order_purchase`
    MODIFY COLUMN `deduct_order_purchase_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联单据类型：PRODUCT_PURCHASE(大货采购单),PROCESS_PURCHASE(加工采购单),PURCHASE_RETURN(采购退货单),SAMPLE_RETURN(样品退货单),SAMPLE(样品采购单),' AFTER `business_no`;

ALTER TABLE `cn_scm`.`deduct_order_quality`
    MODIFY COLUMN `deduct_order_purchase_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联单据类型：PRODUCT_PURCHASE(大货采购单),PROCESS_PURCHASE(加工采购单),PURCHASE_RETURN(采购退货单),SAMPLE_RETURN(样品退货单),SAMPLE(样品采购单),' AFTER `business_no`;

ALTER TABLE `cn_scm`.`supplement_order`
    MODIFY COLUMN `supplement_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '补款类型：PRICE(价差补款),PROCESS(加工补款),OTHER(其他),' AFTER `supplement_order_no`,
    MODIFY COLUMN `supplement_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '补款状态：WAIT_SUBMIT(待提交),WAIT_CONFIRM(待确认),WAIT_EXAMINE(待审核),AUDITED(已审核),SETTLE(已结算),' AFTER `supplement_type`;

ALTER TABLE `cn_scm`.`supplement_order_purchase`
    MODIFY COLUMN `supplement_order_purchase_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联单据类型：PRODUCT_PURCHASE(大货采购单),PROCESS_PURCHASE(加工采购单),SAMPLE(样品采购单),' AFTER `business_no`;

