UPDATE develop_sample_order
SET develop_sample_status = 'ON_SHELVES'
WHERE develop_sample_status IN ('WAIT_CONFIRM_BILL', 'SETTLE');

UPDATE supplier
SET supplier_alias =
        CASE
            WHEN LENGTH(supplier_code) = 1 AND SUBSTRING(supplier_code, 1, 1) BETWEEN '0' AND '9'
                THEN CONCAT('0', supplier_code)
            ELSE supplier_code
            END
WHERE supplier_code != supplier_alias
  AND supplier_alias = '';

UPDATE supplier
SET reconciliation_cycle = 'MONTH'
WHERE del_timestamp = 0
  AND reconciliation_cycle = '';
UPDATE supplier
SET settle_time = '1'
WHERE del_timestamp = 0
  AND settle_time = '';