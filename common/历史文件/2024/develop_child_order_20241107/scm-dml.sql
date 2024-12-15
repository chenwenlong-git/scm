UPDATE `cn_scm`.`produce_data`
SET del_timestamp = 20241107183043
WHERE sku = ""
  AND del_timestamp = 0;
UPDATE `cn_scm`.`produce_data_spu`
SET del_timestamp = 20241107183043
WHERE spu = ""
  AND del_timestamp = 0;