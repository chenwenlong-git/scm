package com.hete.supply.scm.server.scm.production.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.production.entity.po.SkuRiskLogPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 商品风险日志表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-10-12
 */
@Component
@Validated
public class SkuRiskLogDao extends BaseDao<SkuRiskLogMapper, SkuRiskLogPo> {

    public List<SkuRiskLogPo> listBySkuRiskId(Long skuRiskId) {
        if (null == skuRiskId) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SkuRiskLogPo>lambdaQuery()
                .eq(SkuRiskLogPo::getSkuRiskId, skuRiskId));
    }
}
