package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopExceptionOrderPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 开发异常处理表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-01
 */
@Component
@Validated
public class DevelopExceptionOrderDao extends BaseDao<DevelopExceptionOrderMapper, DevelopExceptionOrderPo> {

    public List<DevelopExceptionOrderPo> getListByDevelopPamphletOrderNo(String developPamphletOrderNo) {
        if (StringUtils.isBlank(developPamphletOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopExceptionOrderPo>lambdaQuery()
                .eq(DevelopExceptionOrderPo::getDevelopPamphletOrderNo, developPamphletOrderNo));
    }
}
