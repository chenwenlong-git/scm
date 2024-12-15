package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialBackItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;


/**
 * <p>
 * 加工原料归还单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-03-14
 */
@Component
@Validated
public class ProcessMaterialBackItemDao extends BaseDao<ProcessMaterialBackItemMapper, ProcessMaterialBackItemPo> {

    /**
     * 通过 id 查询归还明细
     *
     * @param materialBackId
     * @return
     */
    public List<ProcessMaterialBackItemPo> getByMaterialBackId(Long materialBackId) {
        return list(Wrappers.<ProcessMaterialBackItemPo>lambdaQuery()
                .eq(ProcessMaterialBackItemPo::getProcessMaterialBackId, materialBackId));
    }

    /**
     * 通过 批量 id 查询归还明细
     *
     * @param processMaterialBackIds
     * @return
     */
    public List<ProcessMaterialBackItemPo> listByProcessMaterialBackIds(List<Long> processMaterialBackIds) {
        if (CollectionUtils.isEmpty(processMaterialBackIds)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessMaterialBackItemPo>lambdaQuery()
                .in(ProcessMaterialBackItemPo::getProcessMaterialBackId, processMaterialBackIds));
    }
}
