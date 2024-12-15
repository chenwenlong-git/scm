package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPricingOrderMechanismPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 核价单表详情机制信息 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-02
 */
@Component
@Validated
public class DevelopPricingOrderMechanismDao extends BaseDao<DevelopPricingOrderMechanismMapper, DevelopPricingOrderMechanismPo> {

    public List<DevelopPricingOrderMechanismPo> getListByDevelopPricingOrderNo(String developPricingOrderNo) {
        if (StringUtils.isBlank(developPricingOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopPricingOrderMechanismPo>lambdaQuery()
                .eq(DevelopPricingOrderMechanismPo::getDevelopPricingOrderNo, developPricingOrderNo));
    }
}
