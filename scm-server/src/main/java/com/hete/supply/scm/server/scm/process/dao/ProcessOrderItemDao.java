package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderItemPo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 加工单明细表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Component
@Validated
public class ProcessOrderItemDao extends BaseDao<ProcessOrderItemMapper, ProcessOrderItemPo> {

    /**
     * 通过加工单号
     *
     * @param processOrderNo
     * @return
     */
    public List<ProcessOrderItemPo> getByProcessOrderNo(String processOrderNo) {
        return list(Wrappers.<ProcessOrderItemPo>lambdaQuery()
                .eq(ProcessOrderItemPo::getProcessOrderNo, processOrderNo));
    }


    /**
     * 通过加工单号和是否是首次创建
     *
     * @param processOrderNo
     * @return
     */
    public List<ProcessOrderItemPo> getByProcessOrderNoAndIsFirst(String processOrderNo, BooleanType isFirst) {
        return list(Wrappers.<ProcessOrderItemPo>lambdaQuery()
                .eq(ProcessOrderItemPo::getProcessOrderNo, processOrderNo)
                .eq(ProcessOrderItemPo::getIsFirst, isFirst));
    }

    /**
     * 通过多个加工单号查询
     *
     * @param processOrderNos
     * @return
     */
    public List<ProcessOrderItemPo> getByProcessOrderNos(Collection<String> processOrderNos) {
        if (CollectionUtils.isEmpty(processOrderNos)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessOrderItemPo>lambdaQuery()
                .in(ProcessOrderItemPo::getProcessOrderNo, processOrderNos));
    }

    /**
     * 通过 sku 查询
     *
     * @param sku
     * @return
     */
    public List<ProcessOrderItemPo> getBySku(String sku) {
        return list(Wrappers.<ProcessOrderItemPo>lambdaQuery()
                .eq(ProcessOrderItemPo::getSku, sku));
    }

    /**
     * 通过多个 sku 查询
     *
     * @param skus
     * @return
     */
    public List<ProcessOrderItemPo> getBySkus(Collection<String> skus) {
        return list(Wrappers.<ProcessOrderItemPo>lambdaQuery()
                .in(ProcessOrderItemPo::getSku, skus));
    }

    /**
     * 查询次品数查询
     *
     * @param defectiveGoodsCnt
     * @return
     */
    public List<ProcessOrderItemPo> getByHasDefective(Integer defectiveGoodsCnt) {
        return list(Wrappers.<ProcessOrderItemPo>lambdaQuery()
                .ge(ProcessOrderItemPo::getDefectiveGoodsCnt, defectiveGoodsCnt));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }
}
