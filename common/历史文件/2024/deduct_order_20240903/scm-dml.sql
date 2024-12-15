update deduct_order
set deduct_status = "AUDITED"
where deduct_status = "SETTLE";
update supplement_order
set supplement_status = "AUDITED"
where supplement_status = "SETTLE";