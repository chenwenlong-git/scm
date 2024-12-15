#ERP
导出加工模版-工序数据(第一步)
SELECT t.`name`               AS "模版名称",
       p.product_reference_no AS "SKU",
       t.steps                AS "工序"
FROM erp_process_order_type_templates AS t
         LEFT JOIN erp_template_products AS p ON t.id = p.process_order_type_template_id
WHERE t.steps IS NOT NULL
  AND t.steps <> '';

#ERP
导出加工模版-原料数据(第二步)
SELECT t.`name`               AS "模版名称",
       m.product_reference_no as "材料SKU",
       m.product_name         as "材料名称",
       m.num                  as "材料数量"
FROM erp_process_order_type_templates AS t
         LEFT JOIN erp_template_materials as m on t.id = m.process_order_type_template_id;

