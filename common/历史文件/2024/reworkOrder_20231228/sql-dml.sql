update process_material_receipt
set material_receipt_type='PROCESSING_MATERIAL'
where del_timestamp = 0
  and process_order_no != ''