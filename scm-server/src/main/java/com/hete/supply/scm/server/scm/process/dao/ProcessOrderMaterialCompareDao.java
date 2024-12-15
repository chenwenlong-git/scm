package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderMaterialComparePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 加工原料表对照关系表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-11-09
 */
@Component
@Validated
public class ProcessOrderMaterialCompareDao extends BaseDao<ProcessOrderMaterialCompareMapper, ProcessOrderMaterialComparePo> {

    public List<ProcessOrderMaterialComparePo> listByMaterialIds(List<Long> materialIdList) {
        if (CollectionUtils.isEmpty(materialIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProcessOrderMaterialComparePo>lambdaQuery()
                .in(ProcessOrderMaterialComparePo::getProcessOrderMaterialId, materialIdList));
    }

    @Override
    public boolean removeBatch(Collection<ProcessOrderMaterialComparePo> entityList) {
        return super.removeBatch(entityList);
    }
}
