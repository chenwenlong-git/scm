package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.entity.po.DevelopSampleSettleItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 样品结算单明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-04
 */
@Component
@Validated
public class DevelopSampleSettleItemDao extends BaseDao<DevelopSampleSettleItemMapper, DevelopSampleSettleItemPo> {

    public List<DevelopSampleSettleItemPo> getListByLikeDevelopChildOrderNo(String developChildOrderNo) {
        if (StringUtils.isBlank(developChildOrderNo)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<DevelopSampleSettleItemPo>lambdaQuery()
                .like(DevelopSampleSettleItemPo::getDevelopChildOrderNo, developChildOrderNo));
    }

    public List<DevelopSampleSettleItemPo> getListByLikeDevelopSampleOrderNo(String developSampleOrderNo) {
        if (StringUtils.isBlank(developSampleOrderNo)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<DevelopSampleSettleItemPo>lambdaQuery()
                .like(DevelopSampleSettleItemPo::getDevelopSampleOrderNo, developSampleOrderNo));
    }

    public List<DevelopSampleSettleItemPo> getListByDevelopSampleOrderNoList(List<String> developSampleOrderNoList) {
        if (CollectionUtils.isEmpty(developSampleOrderNoList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<DevelopSampleSettleItemPo>lambdaQuery()
                .in(DevelopSampleSettleItemPo::getDevelopSampleOrderNo, developSampleOrderNoList));
    }

    public List<DevelopSampleSettleItemPo> getListByDevelopSampleSettleOrderNo(String developSampleSettleOrderNo) {
        if (StringUtils.isBlank(developSampleSettleOrderNo)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<DevelopSampleSettleItemPo>lambdaQuery()
                .eq(DevelopSampleSettleItemPo::getDevelopSampleSettleOrderNo, developSampleSettleOrderNo));
    }

    public List<DevelopSampleSettleItemPo> getListByNoList(List<String> developSampleSettleOrderNoList) {
        if (CollectionUtils.isEmpty(developSampleSettleOrderNoList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<DevelopSampleSettleItemPo>lambdaQuery()
                .in(DevelopSampleSettleItemPo::getDevelopSampleSettleOrderNo, developSampleSettleOrderNoList));
    }
}
