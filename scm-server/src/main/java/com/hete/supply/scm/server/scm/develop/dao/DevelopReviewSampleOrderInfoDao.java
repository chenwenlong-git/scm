package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopReviewSampleOrderInfoPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 开发审版关联样品单属性表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-31
 */
@Component
@Validated
public class DevelopReviewSampleOrderInfoDao extends BaseDao<DevelopReviewSampleOrderInfoMapper, DevelopReviewSampleOrderInfoPo> {

    public List<DevelopReviewSampleOrderInfoPo> getListByDevelopSampleOrderNoList(List<String> developSampleOrderNoList) {
        if (CollectionUtils.isEmpty(developSampleOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopReviewSampleOrderInfoPo>lambdaQuery()
                .in(DevelopReviewSampleOrderInfoPo::getDevelopSampleOrderNo, developSampleOrderNoList));
    }

    public List<DevelopReviewSampleOrderInfoPo> getListByDevelopReviewNo(String developReviewOrderNo) {
        if (StringUtils.isBlank(developReviewOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<DevelopReviewSampleOrderInfoPo>lambdaQuery()
                .eq(DevelopReviewSampleOrderInfoPo::getDevelopReviewOrderNo, developReviewOrderNo));
    }

    public List<DevelopReviewSampleOrderInfoPo> getListByDevelopSampleOrderNo(String developSampleOrderNo) {
        if (StringUtils.isBlank(developSampleOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopReviewSampleOrderInfoPo>lambdaQuery()
                .eq(DevelopReviewSampleOrderInfoPo::getDevelopSampleOrderNo, developSampleOrderNo));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }


}
