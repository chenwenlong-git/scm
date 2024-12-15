package com.hete.supply.scm.server.scm.ibfs.dao;

import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoOrderStatus;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderItemSkuPo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 财务对账单明细sku详情表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-13
 */
@Mapper
interface FinanceRecoOrderItemSkuMapper extends BaseDataMapper<FinanceRecoOrderItemSkuPo> {

    List<FinanceRecoOrderItemSkuPo> getListByCollectOrderNoAndNotStatus(@Param("collectOrderNoList") List<String> collectOrderNoList,
                                                                        @Param("financeRecoOrderStatusNotList") List<FinanceRecoOrderStatus> financeRecoOrderStatusNotList);
}
