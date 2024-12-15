package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopChildOrderChangePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 开发子单变更记录表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-01
 */
@Component
@Validated
public class DevelopChildOrderChangeDao extends BaseDao<DevelopChildOrderChangeMapper, DevelopChildOrderChangePo> {

    public DevelopChildOrderChangePo getOneByDevelopChildOrderNo(String developChildOrderNo) {
        if (StringUtils.isBlank(developChildOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<DevelopChildOrderChangePo>lambdaQuery()
                .eq(DevelopChildOrderChangePo::getDevelopChildOrderNo, developChildOrderNo));
    }

    public List<DevelopChildOrderChangePo> getListByNoList(List<String> developChildOrderNoList) {
        if (CollectionUtils.isEmpty(developChildOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopChildOrderChangePo>lambdaQuery()
                .in(DevelopChildOrderChangePo::getDevelopChildOrderNo, developChildOrderNoList));
    }

}
