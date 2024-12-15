package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopReviewOrderUnusualPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 * 开发审版异常处理报告表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-26
 */
@Component
@Validated
public class DevelopReviewOrderUnusualDao extends BaseDao<DevelopReviewOrderUnusualMapper, DevelopReviewOrderUnusualPo> {

    public DevelopReviewOrderUnusualPo getOneByDevSampleNo(String developSampleOrderNo) {
        if (StringUtils.isBlank(developSampleOrderNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<DevelopReviewOrderUnusualPo>lambdaQuery()
                .eq(DevelopReviewOrderUnusualPo::getDevelopSampleOrderNo, developSampleOrderNo));
    }
}
