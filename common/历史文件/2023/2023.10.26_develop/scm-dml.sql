UPDATE cn_scm.develop_sample_order
SET develop_sample_type = "NORMAL";

update develop_review_order
set develop_review_related = "PURCHASE"
where prenatal_sample_order_no != '';

update develop_review_order
set develop_review_related = "NORMAL"
where prenatal_sample_order_no = '';