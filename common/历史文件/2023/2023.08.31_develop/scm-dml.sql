UPDATE purchase_parent_order
set purchase_order_type = 'FIRST_ORDER'
where purchase_order_type = 'TRUE';

UPDATE purchase_parent_order
set purchase_order_type = 'NORMAL'
where purchase_order_type = 'FALSE';

UPDATE purchase_child_order
set purchase_order_type = 'FIRST_ORDER'
where purchase_order_type = 'TRUE';

UPDATE purchase_child_order
set purchase_order_type = 'NORMAL'
where purchase_order_type = 'FALSE';