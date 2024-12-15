UPDATE cn_scm.develop_child_order_attr AS dcoa
    INNER JOIN cn_plm.attribute_name AS an
    ON an.attr_name = dcoa.attr_name AND an.attr_type = "PRODUCE" AND an.del_timestamp = 0
SET dcoa.attribute_name_id = COALESCE(an.attribute_name_id, dcoa.attribute_name_id);


UPDATE cn_scm.produce_data_attr AS pda
    INNER JOIN cn_plm.attribute_name AS an
    ON an.attr_name = pda.attr_name AND an.attr_type = "PRODUCE" AND an.del_timestamp = 0
SET pda.attribute_name_id = COALESCE(an.attribute_name_id, pda.attribute_name_id);


UPDATE cn_scm.develop_review_sample_order_info AS drsoi
    INNER JOIN cn_plm.attribute_name AS an
    ON an.attr_name = drsoi.sample_info_key AND an.attr_type = "PRODUCE" AND an.del_timestamp = 0
SET drsoi.attribute_name_id = COALESCE(an.attribute_name_id, drsoi.attribute_name_id);

UPDATE cn_scm.process_order_sample AS pos
    INNER JOIN cn_plm.attribute_name AS an
    ON an.attr_name = pos.sample_info_key AND an.attr_type = "PRODUCE" AND an.del_timestamp = 0
SET pos.source_document_number = COALESCE(an.attribute_name_id, pos.source_document_number);