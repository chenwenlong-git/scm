package com.hete.supply.scm.server.scm.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataSpuPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 生产信息表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-09-27
 */
@Component
@Validated
public class ProduceDataSpuDao extends BaseDao<ProduceDataSpuMapper, ProduceDataSpuPo> {
    public ProduceDataSpuPo getBySpu(String spu) {
        return baseMapper.selectOne(Wrappers.<ProduceDataSpuPo>lambdaQuery()
                .eq(ProduceDataSpuPo::getSpu, spu));
    }

    public List<ProduceDataSpuPo> getListBySpuList(List<String> spuList) {
        if (CollectionUtils.isEmpty(spuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataSpuPo>lambdaQuery()
                .in(ProduceDataSpuPo::getSpu, spuList));
    }

}
