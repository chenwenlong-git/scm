update purchase_parent_order_change as ppoc
set ppoc.purchase_parent_order_no = (select ppo.purchase_parent_order_no
                                     from purchase_parent_order as ppo
                                     where ppoc.purchase_parent_order_id = ppo.purchase_parent_order_id);

update purchase_child_order_change as pcoc
set pcoc.purchase_child_order_no = (select pco.purchase_child_order_no
                                    from purchase_child_order as pco
                                    WHERE pcoc.purchase_child_order_id = pco.purchase_child_order_id)




