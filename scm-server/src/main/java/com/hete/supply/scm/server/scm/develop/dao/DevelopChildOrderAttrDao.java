package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopChildOrderAttrPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 开发子单属性值表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-02
 */
@Component
@Validated
public class DevelopChildOrderAttrDao extends BaseDao<DevelopChildOrderAttrMapper, DevelopChildOrderAttrPo> {

    public List<DevelopChildOrderAttrPo> getListByDevelopChildOrderNo(String developChildOrderNo) {
        if (StringUtils.isBlank(developChildOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<DevelopChildOrderAttrPo>lambdaQuery()
                .eq(DevelopChildOrderAttrPo::getDevelopChildOrderNo, developChildOrderNo));
    }

    public void deleteByDevelopChildOrderNo(String developChildOrderNo) {
        baseMapper.delete(Wrappers.<DevelopChildOrderAttrPo>lambdaUpdate()
                .eq(DevelopChildOrderAttrPo::getDevelopChildOrderNo, developChildOrderNo));
    }
}
