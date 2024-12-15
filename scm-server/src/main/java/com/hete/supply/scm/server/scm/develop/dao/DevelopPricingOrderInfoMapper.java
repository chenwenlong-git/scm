package com.hete.supply.scm.server.scm.develop.dao;

import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopPricingOrderInfoByPriceTimeBo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPricingOrderInfoPo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 核价单表详情信息 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-02
 */
@Mapper
interface DevelopPricingOrderInfoMapper extends BaseDataMapper<DevelopPricingOrderInfoPo> {

    List<DevelopPricingOrderInfoByPriceTimeBo> getListBySampleOrderNoAndPriceTime(@NotEmpty @Param("developSampleOrderNoList") List<String> developSampleOrderNoList);
}
