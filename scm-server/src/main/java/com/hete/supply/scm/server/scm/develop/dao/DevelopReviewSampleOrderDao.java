package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopReviewSampleOrderPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 开发审版关联样品单表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-31
 */
@Component
@Validated
public class DevelopReviewSampleOrderDao extends BaseDao<DevelopReviewSampleOrderMapper, DevelopReviewSampleOrderPo> {

    public List<DevelopReviewSampleOrderPo> getListByDevelopSampleOrderNoList(List<String> developSampleOrderNoList) {
        if (CollectionUtils.isEmpty(developSampleOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopReviewSampleOrderPo>lambdaQuery()
                .in(DevelopReviewSampleOrderPo::getDevelopSampleOrderNo, developSampleOrderNoList));
    }

    public List<DevelopReviewSampleOrderPo> getListByReviewNo(String developReviewOrderNo) {
        if (StringUtils.isBlank(developReviewOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<DevelopReviewSampleOrderPo>lambdaQuery()
                .eq(DevelopReviewSampleOrderPo::getDevelopReviewOrderNo, developReviewOrderNo));
    }

    public DevelopReviewSampleOrderPo getOneBySampleOrderNo(String developSampleOrderNo) {
        if (StringUtils.isBlank(developSampleOrderNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<DevelopReviewSampleOrderPo>lambdaQuery()
                .eq(DevelopReviewSampleOrderPo::getDevelopSampleOrderNo, developSampleOrderNo));
    }

    public List<DevelopReviewSampleOrderPo> getListByReviewNoList(List<String> developReviewOrderNoList) {
        if (CollectionUtils.isEmpty(developReviewOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<DevelopReviewSampleOrderPo>lambdaQuery()
                .in(DevelopReviewSampleOrderPo::getDevelopReviewOrderNo, developReviewOrderNoList));
    }
}
