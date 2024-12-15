UPDATE develop_sample_order_process AS dsop
SET dsop.process_code = (SELECT p.process_code
                         FROM process AS p
                         WHERE p.process_first = dsop.process_first
                           AND p.process_second_code = dsop.process_second_code
                           AND p.process_label = dsop.process_label
                         LIMIT 1),
    dsop.process_name = (SELECT p.process_name
                         FROM process AS p
                         WHERE p.process_first = dsop.process_first
                           AND p.process_second_code = dsop.process_second_code
                           AND p.process_label = dsop.process_label
                         LIMIT 1);


UPDATE produce_data_item_process AS pdip
SET pdip.process_code = (SELECT p.process_code
                         FROM process AS p
                         WHERE p.process_first = pdip.process_first
                           AND p.process_second_code = pdip.process_second_code
                           AND p.process_label = pdip.process_label
                         LIMIT 1),
    pdip.process_name = (SELECT p.process_name
                         FROM process AS p
                         WHERE p.process_first = pdip.process_first
                           AND p.process_second_code = pdip.process_second_code
                           AND p.process_label = pdip.process_label
                         LIMIT 1);