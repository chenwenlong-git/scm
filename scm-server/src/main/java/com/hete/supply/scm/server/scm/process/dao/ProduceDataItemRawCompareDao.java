package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProduceDataItemRawComparePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 生产资料原料对照关系表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-11-09
 */
@Component
@Validated
public class ProduceDataItemRawCompareDao extends BaseDao<ProduceDataItemRawCompareMapper, ProduceDataItemRawComparePo> {

    public List<ProduceDataItemRawComparePo> listByProdRawIdList(List<Long> prodRawIdList) {
        if (CollectionUtils.isEmpty(prodRawIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataItemRawComparePo>lambdaQuery()
                .in(ProduceDataItemRawComparePo::getProduceDataItemRawId, prodRawIdList));
    }

    @Override
    public boolean removeBatch(Collection<ProduceDataItemRawComparePo> entityList) {
        return super.removeBatch(entityList);
    }
}
