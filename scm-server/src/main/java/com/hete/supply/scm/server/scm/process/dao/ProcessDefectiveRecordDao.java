package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessDefectiveRecordPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 加工单次品记录表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-05-31
 */
@Component
@Validated
public class ProcessDefectiveRecordDao extends BaseDao<ProcessDefectiveRecordMapper, ProcessDefectiveRecordPo> {

    /**
     * 通过单个加工单号查询
     *
     * @param processOrderNo
     * @return
     */
    public List<ProcessDefectiveRecordPo> getByProcessOrderNo(String processOrderNo) {
        if (StringUtils.isBlank(processOrderNo)) {
            return new ArrayList<>();
        }
        return list(Wrappers.<ProcessDefectiveRecordPo>lambdaQuery()
                .eq(ProcessDefectiveRecordPo::getProcessOrderNo, processOrderNo));
    }

    /**
     * 通过单号查询
     *
     * @param processDefectiveRecordNo
     * @return
     */
    public ProcessDefectiveRecordPo getByRecordNo(String processDefectiveRecordNo) {
        if (StringUtils.isBlank(processDefectiveRecordNo)) {
            return null;
        }
        return getOne(Wrappers.<ProcessDefectiveRecordPo>lambdaQuery()
                .eq(ProcessDefectiveRecordPo::getProcessDefectiveRecordNo, processDefectiveRecordNo));
    }

    /**
     * 通过多个单号查询
     *
     * @param processDefectiveRecordNoList
     * @return
     */
    public List<ProcessDefectiveRecordPo> getByRecordNoList(List<String> processDefectiveRecordNoList) {
        if (CollectionUtils.isEmpty(processDefectiveRecordNoList)) {
            return new ArrayList<>();
        }
        return list(Wrappers.<ProcessDefectiveRecordPo>lambdaQuery()
                .in(ProcessDefectiveRecordPo::getProcessDefectiveRecordNo, processDefectiveRecordNoList));
    }

    /**
     * 通过多个加工单号查询
     *
     * @param processOrderNos
     * @return
     */
    public List<ProcessDefectiveRecordPo> getByProcessOrderNos(List<String> processOrderNos) {
        if (CollectionUtils.isEmpty(processOrderNos)) {
            return new ArrayList<>();
        }
        return list(Wrappers.<ProcessDefectiveRecordPo>lambdaQuery()
                .in(ProcessDefectiveRecordPo::getProcessOrderNo, processOrderNos));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }
}
