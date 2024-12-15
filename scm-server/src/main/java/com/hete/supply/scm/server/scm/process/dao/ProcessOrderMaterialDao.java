package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderMaterialPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 加工单原料表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Component
@Validated
public class ProcessOrderMaterialDao extends BaseDao<ProcessOrderMaterialMapper, ProcessOrderMaterialPo> {

    /**
     * 通过加工单号查询
     *
     * @param processOrderNo
     * @return
     */
    public List<ProcessOrderMaterialPo> getByProcessOrderNo(String processOrderNo) {
        if (StringUtils.isBlank(processOrderNo)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessOrderMaterialPo>lambdaQuery()
                .eq(ProcessOrderMaterialPo::getProcessOrderNo, processOrderNo));
    }

    /**
     * 通过sku查询
     *
     * @param skuList
     * @return
     */
    public List<ProcessOrderMaterialPo> getBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return new ArrayList<>();
        }
        return list(Wrappers.<ProcessOrderMaterialPo>lambdaQuery()
                .in(ProcessOrderMaterialPo::getSku, skuList));
    }

    /**
     * 通过多个加工单号查询
     *
     * @param processOrderNos
     * @return
     */
    public List<ProcessOrderMaterialPo> getByProcessOrderNos(List<String> processOrderNos) {
        if (CollectionUtils.isEmpty(processOrderNos)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessOrderMaterialPo>lambdaQuery()
                .in(ProcessOrderMaterialPo::getProcessOrderNo, processOrderNos));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<ProcessOrderMaterialPo> getListByDeliveryNo(String deliveryNo) {
        if (StringUtils.isBlank(deliveryNo)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessOrderMaterialPo>lambdaQuery()
                .in(ProcessOrderMaterialPo::getDeliveryNo, deliveryNo));
    }

    /**
     * 根据返修单号查询加工单原料信息列表
     *
     * @param repairOrderNo 返修单号，不能为空
     * @return 符合条件的加工单原料信息列表，如果返修单号为空或查询结果为空，则返回空列表
     */
    public List<ProcessOrderMaterialPo> listByRepairOrderNo(String repairOrderNo) {
        if (StringUtils.isBlank(repairOrderNo)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessOrderMaterialPo>lambdaQuery()
                .in(ProcessOrderMaterialPo::getRepairOrderNo, repairOrderNo));
    }

    public List<ProcessOrderMaterialPo> listByDeliveryNo(String deliveryNo) {
        if (StringUtils.isBlank(deliveryNo)) {
            return null;
        }
        return list(Wrappers.<ProcessOrderMaterialPo>lambdaQuery()
                .eq(ProcessOrderMaterialPo::getDeliveryNo, deliveryNo));
    }

    /**
     * 根据出库单号列表查询对应的加工单原料信息列表
     *
     * @param deliveryNos 出库单号列表
     * @return 加工单物料信息列表
     */
    public List<ProcessOrderMaterialPo> listByDeliveryNos(Collection<String> deliveryNos) {
        return CollectionUtils.isEmpty(deliveryNos) ?
                Collections.emptyList() : list(Wrappers.<ProcessOrderMaterialPo>lambdaQuery()
                .in(ProcessOrderMaterialPo::getDeliveryNo, deliveryNos));
    }

    public List<ProcessOrderMaterialPo> getListByRepairOrderNo(String repairOrderNo) {
        if (StringUtils.isBlank(repairOrderNo)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessOrderMaterialPo>lambdaQuery()
                .eq(ProcessOrderMaterialPo::getRepairOrderNo, repairOrderNo));
    }

    @Override
    public boolean removeBatch(Collection<ProcessOrderMaterialPo> entityList) {
        return super.removeBatch(entityList);
    }
}
