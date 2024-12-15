package com.hete.supply.scm.server.scm.dao;

import com.hete.supply.scm.server.scm.entity.po.ProduceDataSpecPo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 生产信息产品规格书 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-11-01
 */
@Mapper
interface ProduceDataSpecMapper extends BaseDataMapper<ProduceDataSpecPo> {

    List<ProduceDataSpecPo> getProduceDataSpecInit();
}
