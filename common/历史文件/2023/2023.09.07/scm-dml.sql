update purchase_child_order_change
set place_order_time = create_time
where place_order_time = '1970-01-01 00:00:00'