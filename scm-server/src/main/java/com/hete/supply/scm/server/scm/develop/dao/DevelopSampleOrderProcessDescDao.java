package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderProcessDescPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 开发需求样品单描述 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-03
 */
@Component
@Validated
public class DevelopSampleOrderProcessDescDao extends BaseDao<DevelopSampleOrderProcessDescMapper, DevelopSampleOrderProcessDescPo> {

    public List<DevelopSampleOrderProcessDescPo> getListByDevelopSampleOrderNoList(List<String> developSampleOrderNoList) {
        if (CollectionUtils.isEmpty(developSampleOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderProcessDescPo>lambdaQuery()
                .in(DevelopSampleOrderProcessDescPo::getDevelopSampleOrderNo, developSampleOrderNoList));
    }

    public void deleteByDevelopSampleOrderNoList(List<String> developSampleOrderNoList) {
        baseMapper.delete(Wrappers.<DevelopSampleOrderProcessDescPo>lambdaUpdate()
                .in(DevelopSampleOrderProcessDescPo::getDevelopSampleOrderNo, developSampleOrderNoList));
    }

    public List<DevelopSampleOrderProcessDescPo> getListByDevelopPamphletOrderNo(String developPamphletOrderNo) {
        if (StringUtils.isBlank(developPamphletOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopSampleOrderProcessDescPo>lambdaQuery()
                .eq(DevelopSampleOrderProcessDescPo::getDevelopPamphletOrderNo, developPamphletOrderNo));
    }

    public int removeBatchByIds(List<Long> idList) {
        return baseMapper.deleteBatchIds(idList);
    }

}
