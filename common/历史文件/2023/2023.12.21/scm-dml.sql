# 对拆单补交的采购单进行跟单人赋值
update `purchase_child_order` AS a
    INNER JOIN purchase_child_order as b
    on a.source_purchase_child_order_no = b.purchase_child_order_no
    INNER JOIN purchase_child_order_change as ac
    on a.purchase_child_order_no = ac.purchase_child_order_no
    INNER JOIN purchase_child_order_change as bc
    on b.purchase_child_order_no = bc.purchase_child_order_no
set ac.confirm_user     = bc.confirm_user,
    ac.confirm_username = bc.confirm_username
where a.`source_purchase_child_order_no` != ''
  and a.`del_timestamp` = 0


# 修复PO1429579405-01的数据
update purchase_child_order
set timely_delivery_cnt  = 29,
    shippable_cnt        = 0,
    timely_delivery_rate = 0.97
where purchase_child_order_no = 'PO1429579405-01'

# 修复可发货数为负
# 查询语句
select *
from `purchase_child_order`
where `shippable_cnt` < 0 # 修复语句
update purchase_child_order
set shippable_cnt = 0
where shippable_cnt < 0


update purchase_child_order as pco
    INNER JOIN purchase_child_order_change as pcoc
    on pco.purchase_child_order_no = pcoc.purchase_child_order_no
set pcoc.receive_order_time = pcoc.place_order_time
where pco.`source_purchase_child_order_no` != ''