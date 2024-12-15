update supplier_inventory_record
set effective_time = update_time
where del_timestamp = 0
  AND supplier_inventory_record_status = 'EFFECTIVE';