package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialReceiptItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

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
public class ProcessMaterialReceiptItemDao extends BaseDao<ProcessMaterialReceiptItemMapper, ProcessMaterialReceiptItemPo> {

    /**
     * 通过多个原料收货单获取详情
     *
     * @param materialReceiptIds
     * @return
     */
    public List<ProcessMaterialReceiptItemPo> getByMaterialReceiptIds(List<Long> materialReceiptIds) {
        if (CollectionUtils.isEmpty(materialReceiptIds)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessMaterialReceiptItemPo>lambdaQuery()
                .in(ProcessMaterialReceiptItemPo::getProcessMaterialReceiptId, materialReceiptIds)
        );
    }

    /**
     * 通过多个 sku 查询
     *
     * @param skus
     * @return
     */
    public List<ProcessMaterialReceiptItemPo> getBySkus(List<String> skus) {
        return list(Wrappers.<ProcessMaterialReceiptItemPo>lambdaQuery()
                .in(ProcessMaterialReceiptItemPo::getSku, skus)
        );
    }
}
