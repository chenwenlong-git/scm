# 初始化采购子单完结状态(考虑是否执行)
update purchase_child_order AS pco
    INNER JOIN purchase_child_order_item as pcoi
    on pco.purchase_child_order_no = pcoi.purchase_child_order_no
set pco.purchase_order_status = 'FINISH'
where pco.purchase_order_status = 'WAREHOUSED'
  and pcoi.purchase_cnt = pcoi.quality_goods_cnt;

# 初始化采购子单已结算变更为完结状态
update purchase_child_order
set purchase_order_status = 'FINISH'
where purchase_order_status = 'SETTLE';

# 初始化采购子单采购未交数
UPDATE purchase_child_order_item as pcoi
    INNER JOIN (SELECT pdo.purchase_child_order_no, SUM(pdoi.quality_goods_cnt) AS sumQgc
                FROM purchase_deliver_order as pdo
                         INNER JOIN purchase_deliver_order_item as pdoi
                                    ON pdoi.purchase_deliver_order_no = pdo.purchase_deliver_order_no
                WHERE pdo.deliver_order_status != 'DELETED'
                  AND pdo.del_timestamp = 0
                  AND pdoi.del_timestamp = 0
                GROUP BY purchase_child_order_no) AS purchase_deliver
    ON pcoi.purchase_child_order_no = purchase_deliver.purchase_child_order_no
SET pcoi.undelivered_cnt = CASE
                               WHEN (pcoi.purchase_cnt - purchase_deliver.sumQgc) < 0
                                   THEN 0
                               ELSE (pcoi.purchase_cnt - purchase_deliver.sumQgc) END
WHERE pcoi.del_timestamp = 0;

# 作废,已完结,已结算的采购子单可发货数数据清零
update `purchase_child_order`
set shippable_cnt = 0
where `purchase_order_status` = 'DELETE'
   OR `purchase_order_status` = 'FINISH'
   OR `purchase_order_status` = 'SETTLE';

# 作废,已完结,已结算的采购子单采购未交数数据清零
update purchase_child_order_item as pcoi
    INNER JOIN purchase_child_order AS pco
    ON pcoi.purchase_child_order_no = pco.purchase_child_order_no
SET pcoi.undelivered_cnt = 0
where pco.`purchase_order_status` = 'DELETE'
   OR pco.`purchase_order_status` = 'FINISH'
   OR pco.`purchase_order_status` = 'SETTLE';

# 初始化采购母单未交数
UPDATE purchase_parent_order AS ppo
    JOIN (SELECT purchase_parent_order_no, SUM(undelivered_cnt) AS sumUcCnt
          FROM purchase_child_order_item AS pcoi
          GROUP BY purchase_parent_order_no) AS pcoisum ON ppo.purchase_parent_order_no = pcoisum.purchase_parent_order_no
SET ppo.undelivered_cnt = pcoisum.sumUcCnt;


# 校验 初始化采购母单未交数
SELECT A.purchase_parent_order_no, A.undelivered_cnt AS A_x, SUM(B.undelivered_cnt) AS sum_x
FROM purchase_parent_order AS A
         JOIN purchase_child_order_item AS B ON A.purchase_parent_order_no = B.purchase_parent_order_no
GROUP BY A.purchase_parent_order_no;

# 初始化采购母单未交数 (sku维度)
UPDATE purchase_parent_order_item AS A
    JOIN (SELECT purchase_parent_order_no, sku, SUM(undelivered_cnt) AS sum_x
          FROM purchase_child_order_item AS B
          GROUP BY purchase_parent_order_no, sku) AS B
    ON A.purchase_parent_order_no = B.purchase_parent_order_no
        AND A.sku = B.sku
SET A.undelivered_cnt = B.sum_x;

# 校验 初始化采购母单未交数 (sku维度)
SELECT A.purchase_parent_order_no, A.sku, A.undelivered_cnt AS A_x, B.sum_x
FROM purchase_parent_order_item AS A
         JOIN (SELECT purchase_parent_order_no, sku, SUM(undelivered_cnt) AS sum_x
               FROM purchase_child_order_item AS B
               GROUP BY purchase_parent_order_no, sku) AS B ON A.purchase_parent_order_no = B.purchase_parent_order_no
    AND A.sku = B.sku;

# 采购母单维度对待提交,待审核,已取消的采购未交数清零
update purchase_parent_order AS ppo
set ppo.undelivered_cnt = 0
where ppo.purchase_parent_order_status = 'WAIT_ORDER'
   OR ppo.purchase_parent_order_status = 'WAIT_APPROVE'
   OR ppo.purchase_parent_order_status = 'DELETED';

# 采购母单sku维度对待提交,待审核,已取消的采购未交数清零
update purchase_parent_order_item AS ppoi
    INNER JOIN purchase_parent_order AS ppo
    ON ppoi.purchase_parent_order_no = ppo.purchase_parent_order_no
set ppoi.undelivered_cnt = 0
where ppo.purchase_parent_order_status = 'WAIT_ORDER'
   OR ppo.purchase_parent_order_status = 'WAIT_APPROVE'
   OR ppo.purchase_parent_order_status = 'DELETED';