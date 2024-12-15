package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderExtraPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * <p>
 * 加工单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Component
@Validated
public class ProcessOrderExtraDao extends BaseDao<ProcessOrderExtraMapper, ProcessOrderExtraPo> {

    /**
     * @param processOrderNo
     * @return
     */
    public ProcessOrderExtraPo getByProcessOrderNo(String processOrderNo) {
        return getOne(Wrappers.<ProcessOrderExtraPo>lambdaQuery()
                .eq(ProcessOrderExtraPo::getProcessOrderNo, processOrderNo));
    }

    public ProcessOrderExtraPo getByCheckOrderNo(String checkOrderNo) {
        return getOne(Wrappers.<ProcessOrderExtraPo>lambdaQuery()
                .eq(ProcessOrderExtraPo::getCheckOrderNo, checkOrderNo));
    }

    /**
     * 通过多个加工单号查询
     *
     * @param processOrderNos
     * @return
     */
    public List<ProcessOrderExtraPo> getByProcessOrderNos(List<String> processOrderNos) {
        return list(Wrappers.<ProcessOrderExtraPo>lambdaQuery()
                .in(ProcessOrderExtraPo::getProcessOrderNo, processOrderNos));
    }
}
