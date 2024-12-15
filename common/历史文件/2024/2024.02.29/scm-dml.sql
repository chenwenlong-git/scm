UPDATE supplier
SET supplier_alias =
        CASE
            WHEN LENGTH(supplier_code) = 1 AND SUBSTRING(supplier_code, 1, 1) BETWEEN '0' AND '9'
                THEN CONCAT('0', supplier_code)
            ELSE supplier_code
            END
WHERE supplier_code != supplier_alias;