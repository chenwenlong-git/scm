#process_order表与process_order_item表
### 注意事项 udb 下单人数据要准备好
SELECT epo.no                    AS 加工单号,
       epo.status                AS 加工单状态,
       case epo.order_category
           WHEN "1" THEN "Luvme-Global-shopify"
           WHEN "3" THEN "Uwins-Global-shopify"
           WHEN "10" THEN "Gorgius-Global-shopify"
           ELSE "其他" END       AS 平台,
       epo.reference_no          AS 需求编号,
       eo.reference_no           AS 订单编号,
       case epo.repo_name
           WHEN "金塬仓" THEN "JY01"
           WHEN "半成品待加工仓" THEN "GZ007"
           WHEN "退货仓" THEN "TH01"
           ELSE "JY01" END       AS 仓库编码,
       epo.repo_name             AS 仓库名称,
       case epo.repo_name
           WHEN "金塬仓" THEN "国内自营"
           WHEN "半成品待加工仓" THEN "加工仓"
           WHEN "退货仓" THEN "国内自营"
           ELSE "JY01" END       AS 收货仓库标签,
       epo.customer_name         AS 客户姓名,
       epo.user_real_name        AS 创建人,
       epo.created_at            AS 创建时间,
       epo.note                  AS 加工单备注,
       epo.print_note            AS 出库备注,
       epop.product_reference_no AS sku,
       epop.num                  AS 加工数量,
       epop.price                AS 采购单价
FROM erp_process_orders AS epo
         LEFT JOIN erp_orders AS eo
                   ON epo.order_id = eo.id
         LEFT JOIN erp_process_order_products AS epop
                   ON epop.process_order_id = epo.id
         LEFT JOIN erp_repos AS er
                   ON er.id = epo.repo_id
WHERE (epo.`status` = 1 OR epo.`status` = 4)
  and epo.order_category in (1, 3, 10)


# process_order_material表
SELECT epo.`no`                  AS 加工单号,
       epom.product_reference_no AS sku,
       epom.num                  AS 出库数量
FROM erp_process_orders AS epo
         LEFT JOIN erp_process_order_materials AS epom
                   ON epom.process_order_id = epo.id
WHERE (epo.`status` = 1 OR epo.`status` = 4)
  and epo.order_category in (1, 3, 10)


# process_order_procedure表
SELECT po.NO                AS 加工单号,
       pos.step_option_name AS 工序名称,
       (SELECT price
        FROM erp_performance_setting_items AS psi
        WHERE psi.step_id = pos.step_id
          AND psi.option_name = pos.step_option_name
        LIMIT 1)            AS 人工提成,
       pos.good_num
FROM erp_process_order_steps AS pos
         LEFT JOIN erp_process_orders AS po
                   ON pos.process_order_id = po.id
WHERE pos.step_option_name NOT in ("货到加工", "加工完成", "货到质检", "货回仓库")
  and (po.`status` = 1
    or po.`status` = 4)
  and po.order_category in (1, 3, 10)


# process_order_desc 表
# 需要先执行 ERP 里面的 processOrderDesc:change 命令
SELECT epo.`no`    AS 加工单号,
       etpod.name  AS 描述名称,
       etpod.value AS 描述值
FROM erp_process_orders AS epo
         LEFT JOIN erp_tmp_process_order_descs AS etpod
                   ON etpod.process_order_id = epo.id
WHERE (epo.`status` = 1 OR epo.`status` = 4)
  and epo.order_category in (1, 3, 10)
  and etpod.name is not null;
