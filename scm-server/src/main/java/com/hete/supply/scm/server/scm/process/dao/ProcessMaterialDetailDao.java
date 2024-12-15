package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialDetailPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 加工单原料明细表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-05-31
 */
@Component
@Validated
public class ProcessMaterialDetailDao extends BaseDao<ProcessMaterialDetailMapper, ProcessMaterialDetailPo> {

    /**
     * 获取加工单原料明细
     *
     * @param deliveryNo
     * @return
     */
    public ProcessMaterialDetailPo getByDeliveryNo(String deliveryNo) {
        return getOne(Wrappers.<ProcessMaterialDetailPo>lambdaQuery().eq(ProcessMaterialDetailPo::getDeliveryNo, deliveryNo));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<ProcessMaterialDetailPo> getListByProcessOrderNo(String processOrderNo) {
        if (StringUtils.isBlank(processOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<ProcessMaterialDetailPo>lambdaQuery()
                .eq(ProcessMaterialDetailPo::getProcessOrderNo, processOrderNo));
    }
}
