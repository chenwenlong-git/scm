UPDATE sample_child_order AS sco
    JOIN supplier AS s
    ON sco.supplier_code = s.supplier_code
SET sco.supplier_name = s.supplier_name;


UPDATE sample_deliver_order AS sdo
    JOIN supplier AS s
    ON sdo.supplier_code = s.supplier_code
SET sdo.supplier_name = s.supplier_name;


UPDATE sample_receipt_order AS sro
    JOIN supplier AS s
    ON sro.supplier_code = s.supplier_code
SET sro.supplier_name = s.supplier_name;


UPDATE sample_return_order AS sro
    JOIN supplier AS s
    ON sro.supplier_code = s.supplier_code
SET sro.supplier_name = s.supplier_name;


UPDATE purchase_settle_order AS pso
    JOIN supplier AS s
    ON pso.supplier_code = s.supplier_code
SET pso.supplier_name = s.supplier_name;

UPDATE purchase_child_order AS pco
    JOIN supplier AS s
    ON pco.supplier_code = s.supplier_code
SET pco.supplier_name = s.supplier_name;

UPDATE purchase_return_order AS pro
    JOIN supplier AS s
    ON pro.supplier_code = s.supplier_code
SET pro.supplier_name = s.supplier_name;

UPDATE shipping_mark AS sm
    JOIN supplier AS s
    ON sm.supplier_code = s.supplier_code
SET sm.supplier_name = s.supplier_name;

UPDATE purchase_deliver_order AS pdo
    JOIN supplier AS s
    ON pdo.supplier_code = s.supplier_code
SET pdo.supplier_name = s.supplier_name;

UPDATE purchase_raw_receipt_order AS prro
    JOIN supplier AS s
    ON prro.supplier_code = s.supplier_code
SET prro.supplier_name = s.supplier_name;