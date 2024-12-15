-- 新增交接数量
alter table qc_detail
    add column hand_over_amount int unsigned default '0' not null comment '交接数量'
        after category_name;

-- 初始化交接数量
update qc_detail
set hand_over_amount=amount
where relation_qc_detail_id = 0;