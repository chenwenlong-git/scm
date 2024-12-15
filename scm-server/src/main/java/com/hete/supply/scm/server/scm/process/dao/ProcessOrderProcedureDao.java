package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessProcedureComplexCoefficientBo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderProcedurePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 加工单工序 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Component
@Validated
public class ProcessOrderProcedureDao extends BaseDao<ProcessOrderProcedureMapper, ProcessOrderProcedurePo> {

    /**
     * 通过加工单号查询
     *
     * @param processOrderNo
     * @return
     */
    public List<ProcessOrderProcedurePo> getByProcessOrderNo(String processOrderNo) {
        return list(Wrappers.<ProcessOrderProcedurePo>lambdaQuery()
                .eq(ProcessOrderProcedurePo::getProcessOrderNo, processOrderNo)
                .orderByAsc(ProcessOrderProcedurePo::getSort));
    }


    /**
     * 通过工序 id 查询
     *
     * @param processIds
     * @return
     */
    public List<ProcessOrderProcedurePo> getByProcessIds(List<Long> processIds) {
        return list(Wrappers.<ProcessOrderProcedurePo>lambdaQuery()
                .in(ProcessOrderProcedurePo::getProcessId, processIds));
    }

    /**
     * 通过多个加工单号查询
     *
     * @param processOrderNos
     * @return
     */
    public List<ProcessOrderProcedurePo> getByProcessOrderNos(List<String> processOrderNos) {
        if (CollectionUtils.isEmpty(processOrderNos)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessOrderProcedurePo>lambdaQuery()
                .in(ProcessOrderProcedurePo::getProcessOrderNo, processOrderNos)
                .orderByAsc(ProcessOrderProcedurePo::getSort));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<ProcessProcedureComplexCoefficientBo> getProcessProcedureCapacityNumBo(List<String> processOrderNos) {
        if (CollectionUtils.isEmpty(processOrderNos)) {
            return Collections.emptyList();
        }
        return baseMapper.getProcessProcedureCapacityNumBo(processOrderNos);
    }

    public List<ProcessOrderProcedurePo> getbyIds(List<Long> ids) {
        return list(Wrappers.<ProcessOrderProcedurePo>lambdaQuery()
                .in(ProcessOrderProcedurePo::getProcessOrderProcedureId, ids));
    }
}
