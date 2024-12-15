package com.hete.supply.scm.server.scm.process.dao;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderScanRelateCountBo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderScanRelatePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 工序扫码关联表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-02-27
 */
@Component
@Validated
public class ProcessOrderScanRelateDao extends BaseDao<ProcessOrderScanRelateMapper, ProcessOrderScanRelatePo> {

    public List<ProcessOrderScanRelatePo> listByProcessOrderScanId(Long processOrderScanId) {
        return Objects.isNull(processOrderScanId) ?
                Collections.emptyList() :
                list(Wrappers.<ProcessOrderScanRelatePo>lambdaQuery()
                        .eq(ProcessOrderScanRelatePo::getProcessOrderScanId, processOrderScanId));
    }

    /**
     * 计算指定工序在指定时间范围内完成的正品数量总和。
     *
     * @param independentProcessCode 工序编码
     * @param processOrderScanId     工序订单扫描ID
     * @param completeUser           完成操作的用户
     * @param completeTimeBegin      完成时间范围开始时间
     * @param completeTimeEnd        完成时间范围结束时间
     * @return 指定时间范围内完成的正品数量总和
     */
    public int calculateQualityGoodsCountTotal(String independentProcessCode,
                                               Long processOrderScanId,
                                               String completeUser,
                                               LocalDateTime completeTimeBegin,
                                               LocalDateTime completeTimeEnd) {
        Integer result = baseMapper.calculateQualityGoodsCountTotal(independentProcessCode, processOrderScanId,
                completeUser,
                completeTimeBegin, completeTimeEnd);
        return Objects.isNull(result) ? 0 : result;
    }

    public List<ProcessOrderScanRelateCountBo> countProcessOrderScanByIds(Collection<Long> processOrderScanIds) {
        return baseMapper.countProcessOrderScanByIds(processOrderScanIds);
    }
}
