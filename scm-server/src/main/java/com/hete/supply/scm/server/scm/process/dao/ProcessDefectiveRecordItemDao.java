package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessDefectiveRecordItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 加工单次品记录明细表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-05-31
 */
@Component
@Validated
public class ProcessDefectiveRecordItemDao extends BaseDao<ProcessDefectiveRecordItemMapper, ProcessDefectiveRecordItemPo> {

    /**
     * 通过次品记录单号查询
     *
     * @param processDefectiveRecordNos
     * @return
     */
    public List<ProcessDefectiveRecordItemPo> getByProcessDefectiveRecordNos(List<String> processDefectiveRecordNos) {
        if (CollectionUtils.isEmpty(processDefectiveRecordNos)) {
            return new ArrayList<>();
        }
        return list(Wrappers.<ProcessDefectiveRecordItemPo>lambdaQuery()
                .in(ProcessDefectiveRecordItemPo::getProcessDefectiveRecordNo, processDefectiveRecordNos));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }
}
