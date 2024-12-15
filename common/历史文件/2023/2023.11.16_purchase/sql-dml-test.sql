# 只在开发测试环境执行
# 检查数据
SELECT *
FROM purchase_parent_order_change as ppoc
WHERE NOT EXISTS (SELECT 1
                  FROM purchase_parent_order as ppo
                  WHERE ppoc.purchase_parent_order_id = ppo.purchase_parent_order_id);

SELECT *
FROM purchase_child_order_change as pcoc
WHERE NOT EXISTS (SELECT 1
                  FROM purchase_child_order as pco
                  WHERE pcoc.purchase_child_order_id = pco.purchase_child_order_id);

# 删除脏数据
delete
from purchase_parent_order_change
where purchase_parent_order_change_id in ('1600379551518552065', '1600380898548641794', '1601099883188400129');
delete
from purchase_child_order_change
where purchase_child_order_change_id in
      ('1601117061251960833', '1601118089577861122', '1601118723734040577', '1601119342813310978');





