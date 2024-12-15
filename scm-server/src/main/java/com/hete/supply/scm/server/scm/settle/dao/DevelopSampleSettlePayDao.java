package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.entity.po.DevelopSampleSettlePayPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 样品结算单支付明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-04
 */
@Component
@Validated
public class DevelopSampleSettlePayDao extends BaseDao<DevelopSampleSettlePayMapper, DevelopSampleSettlePayPo> {

    public List<DevelopSampleSettlePayPo> getListByDevelopSampleSettleOrderNo(String developSampleSettleOrderNo) {
        if (StringUtils.isBlank(developSampleSettleOrderNo)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<DevelopSampleSettlePayPo>lambdaQuery()
                .eq(DevelopSampleSettlePayPo::getDevelopSampleSettleOrderNo, developSampleSettleOrderNo));
    }

    public Map<String, List<DevelopSampleSettlePayPo>> getListByNoList(List<String> developSampleSettleOrderNoList) {
        if (CollectionUtils.isEmpty(developSampleSettleOrderNoList)) {
            return Collections.emptyMap();
        }
        return list(Wrappers.<DevelopSampleSettlePayPo>lambdaQuery()
                .in(DevelopSampleSettlePayPo::getDevelopSampleSettleOrderNo, developSampleSettleOrderNoList))
                .stream()
                .collect(Collectors.groupingBy(DevelopSampleSettlePayPo::getDevelopSampleSettleOrderNo));
    }
}
