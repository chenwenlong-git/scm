update finance_prepayment_order
set currency = 'RMB'
where currency = '';

update finance_payment_item
set rmb_exchange_rate    = exchange_rate,
    target_payment_money = rmb_payment_money;