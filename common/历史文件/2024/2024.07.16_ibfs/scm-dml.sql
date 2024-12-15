UPDATE finance_prepayment_order_item fpoi
    JOIN finance_prepayment_order fpo
    ON fpoi.prepayment_order_no = fpo.prepayment_order_no
SET fpoi.target_prepayment_money = CASE
                                       WHEN fpo.currency = fpoi.currency THEN fpoi.prepayment_money
                                       WHEN fpo.currency = 'USD' AND fpoi.currency = 'RMB'
                                           THEN ROUND(fpoi.prepayment_money / 7.3, 2)
                                       WHEN fpo.currency = 'RMB' AND fpoi.currency = 'USD'
                                           THEN ROUND(fpoi.prepayment_money * 7.3, 2)
    END;