package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderDescPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 加工单加工描述 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Component
@Validated
public class ProcessOrderDescDao extends BaseDao<ProcessOrderDescMapper, ProcessOrderDescPo> {

    /**
     * 通过加工单号查询
     *
     * @param processOrderNo
     * @return
     */
    public List<ProcessOrderDescPo> getByProcessOrderNo(String processOrderNo) {
        return list(Wrappers.<ProcessOrderDescPo>lambdaQuery()
                .eq(ProcessOrderDescPo::getProcessOrderNo, processOrderNo));
    }

    /**
     * 通过多个加工单号查询
     *
     * @param processOrderNos
     * @return
     */
    public List<ProcessOrderDescPo> getByProcessOrderNos(List<String> processOrderNos) {
        return list(Wrappers.<ProcessOrderDescPo>lambdaQuery()
                .in(ProcessOrderDescPo::getProcessOrderNo, processOrderNos));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

}
