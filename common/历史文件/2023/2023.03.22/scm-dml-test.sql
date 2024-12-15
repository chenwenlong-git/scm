# 查找平台为 'Gorgius-Global-SHOPIFY' 的数据(测试环境)
select purchase_parent_order_id
from purchase_parent_order
where platform = 'Gorgius-Global-SHOPIFY';

update purchase_parent_order
set platform = 'PC016'
where platform = 'Gorgius-Global-SHOPIFY'
  and purchase_parent_order_id in ();

select purchase_child_order_id
from purchase_child_order
where platform = 'Gorgius-Global-SHOPIFY';

update purchase_child_order
set platform = 'PC016'
where platform = 'Gorgius-Global-SHOPIFY'
  and purchase_child_order_id in ();

# 修复结算单问题(测试环境)
select a.purchase_child_order_no,
       a.item_settle        as "sku维度结算金额总和",
       b.total_settle_price as "单据维度结算金额"
from (select pcoi.purchase_child_order_no,
             sum(pcoi.settle_price * pcoi.`quality_goods_cnt`) as item_settle
      from `purchase_child_order_item` as pcoi
      GROUP BY pcoi.purchase_child_order_no) as a
         INNER JOIN
     (select pco.purchase_child_order_no, pco.total_settle_price
      from purchase_child_order as pco) as b
     on a.purchase_child_order_no = b.purchase_child_order_no
where a.item_settle != b.total_settle_price;


update purchase_child_order as pco
set total_settle_price = (select sum(pcoi.settle_price * pcoi.`quality_goods_cnt`)
                          from `purchase_child_order_item` as pcoi
                          where pcoi.purchase_child_order_no = '')
where pco.purchase_child_order_no = '';

# 软删除结算单
select purchase_settle_order_no
from purchase_settle_order
where del_timestamp = 0
  and purchase_settle_status = "WAIT_CONFIRM";

update purchase_settle_order
set del_timestamp = 20230322150643
    and purchase_settle_order_no in ();

update purchase_settle_order_item
set del_timestamp = 20230322150643
    and purchase_settle_order_no in ();
