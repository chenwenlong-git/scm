UPDATE purchase_settle_order
SET del_timestamp = 1
WHERE `month` = "2023-09";

UPDATE purchase_settle_order_item
SET del_timestamp = 1
WHERE purchase_settle_order_no IN
      (SELECT purchase_settle_order_no FROM purchase_settle_order WHERE `month` = "2023-09");

UPDATE purchase_settle_order_pay
SET del_timestamp = 1
WHERE purchase_settle_order_no IN
      (SELECT purchase_settle_order_no FROM purchase_settle_order WHERE `month` = "2023-09");