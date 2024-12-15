package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.entity.po.GoodsProcessRelationPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 商品工序关系表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Component
@Validated
public class GoodsProcessRelationDao extends BaseDao<GoodsProcessRelationMapper, GoodsProcessRelationPo> {

    /**
     * 通过商品工序 id 查询
     *
     * @param goodsProcessIds
     * @return
     */
    public List<GoodsProcessRelationPo> getByGoodsProcessIds(List<Long> goodsProcessIds) {
        return list(Wrappers.<GoodsProcessRelationPo>lambdaQuery()
                .in(GoodsProcessRelationPo::getGoodsProcessId, goodsProcessIds));
    }

    /**
     * 通过工序查询
     *
     * @param processIds
     * @return
     */
    public List<GoodsProcessRelationPo> getByProcessIds(List<Long> processIds) {
        return list(Wrappers.<GoodsProcessRelationPo>lambdaQuery()
                .in(GoodsProcessRelationPo::getProcessId, processIds));
    }


    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }


    public List<GoodsProcessRelationPo> getByGoodsProcessId(Long goodsProcessId) {
        return list(Wrappers.<GoodsProcessRelationPo>lambdaQuery()
                .eq(GoodsProcessRelationPo::getGoodsProcessId, goodsProcessId));
    }
}
