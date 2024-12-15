package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderSamplePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 加工单生产信息 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-05-25
 */
@Component
@Validated
public class ProcessOrderSampleDao extends BaseDao<ProcessOrderSampleMapper, ProcessOrderSamplePo> {


    /**
     * 通过加工单号查询
     *
     * @param processOrderNo
     * @return
     */
    public List<ProcessOrderSamplePo> getByProcessOrderNo(String processOrderNo) {
        return list(Wrappers.<ProcessOrderSamplePo>lambdaQuery()
                .eq(ProcessOrderSamplePo::getProcessOrderNo, processOrderNo));
    }

    /**
     * 通过多个加工单号查询
     *
     * @param processOrderNoList
     * @return
     */
    public List<ProcessOrderSamplePo> getByProcessOrderNos(List<String> processOrderNoList) {
        if (CollectionUtils.isEmpty(processOrderNoList)) {
            return new ArrayList<>();
        }

        return list(Wrappers.<ProcessOrderSamplePo>lambdaQuery()
                .in(ProcessOrderSamplePo::getProcessOrderNo, processOrderNoList));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }
}
