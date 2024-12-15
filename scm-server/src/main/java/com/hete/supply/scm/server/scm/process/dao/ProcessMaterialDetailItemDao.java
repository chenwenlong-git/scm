package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialDetailItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 加工单原料明细表详情 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-05-31
 */
@Component
@Validated
public class ProcessMaterialDetailItemDao extends BaseDao<ProcessMaterialDetailItemMapper, ProcessMaterialDetailItemPo> {

    /**
     * 获取原料明细详情
     *
     * @param processMaterialDetailId
     * @return
     */
    public List<ProcessMaterialDetailItemPo> getByProcessMaterialDetailId(Long processMaterialDetailId) {
        return list(Wrappers.<ProcessMaterialDetailItemPo>lambdaQuery().eq(ProcessMaterialDetailItemPo::getProcessMaterialDetailId, processMaterialDetailId));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<ProcessMaterialDetailItemPo> getListByProcessMaterialDetailIdList(List<Long> processMaterialDetailIdList) {
        if (CollectionUtils.isEmpty(processMaterialDetailIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProcessMaterialDetailItemPo>lambdaQuery()
                .in(ProcessMaterialDetailItemPo::getProcessMaterialDetailId, processMaterialDetailIdList));
    }
}
