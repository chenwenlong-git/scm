package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderRawPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 开发需求样品单原料 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-03
 */
@Component
@Validated
public class DevelopSampleOrderRawDao extends BaseDao<DevelopSampleOrderRawMapper, DevelopSampleOrderRawPo> {

    public List<DevelopSampleOrderRawPo> getListByDevelopSampleOrderNoList(List<String> developSampleOrderNoList) {
        if (CollectionUtils.isEmpty(developSampleOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderRawPo>lambdaQuery()
                .in(DevelopSampleOrderRawPo::getDevelopSampleOrderNo, developSampleOrderNoList));
    }

    public void deleteByDevelopSampleOrderNoList(List<String> developSampleOrderNoList) {
        baseMapper.delete(Wrappers.<DevelopSampleOrderRawPo>lambdaUpdate()
                .in(DevelopSampleOrderRawPo::getDevelopSampleOrderNo, developSampleOrderNoList));
    }

    public List<DevelopSampleOrderRawPo> getListByDevelopPamphletOrderNo(String developPamphletOrderNo) {
        if (StringUtils.isBlank(developPamphletOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderRawPo>lambdaQuery()
                .eq(DevelopSampleOrderRawPo::getDevelopPamphletOrderNo, developPamphletOrderNo));
    }

    public int removeBatchByIds(List<Long> idList) {
        return baseMapper.deleteBatchIds(idList);
    }


}
