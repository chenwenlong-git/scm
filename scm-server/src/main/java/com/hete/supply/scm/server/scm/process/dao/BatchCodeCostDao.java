package com.hete.supply.scm.server.scm.process.dao;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hete.supply.scm.server.scm.process.entity.po.BatchCodeCostPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * SKU批次成本信息 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-25
 */
@Component
@Validated
public class BatchCodeCostDao extends BaseDao<BatchCodeCostMapper, BatchCodeCostPo> {

    public List<BatchCodeCostPo> listByRelateOrderNo(String processOrderNo) {
        return StrUtil.isBlank(processOrderNo) ?
                Collections.emptyList() :
                list(new LambdaQueryWrapper<BatchCodeCostPo>().eq(BatchCodeCostPo::getRelateOrderNo, processOrderNo));
    }

    public List<BatchCodeCostPo> listByRelateOrderNoNBatchCode(String processOrderNo,
                                                               String batchCode) {
        return StrUtil.isBlank(processOrderNo) ?
                Collections.emptyList() :
                list(new LambdaQueryWrapper<BatchCodeCostPo>().eq(BatchCodeCostPo::getRelateOrderNo, processOrderNo)
                        .eq(BatchCodeCostPo::getBatchCode, batchCode));
    }
}
