package com.hete.supply.scm.server.scm.ibfs.dao;

import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoOrderStatus;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderItemRelationPo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.RecoOrderItemRelationCheckVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 财务对账单明细SKU关联使用单据表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-13
 */
@Mapper
interface FinanceRecoOrderItemRelationMapper extends BaseDataMapper<FinanceRecoOrderItemRelationPo> {

    List<RecoOrderItemRelationCheckVo> getListByBusinessIdAndNotStatus(@Param("businessIdList") List<Long> businessIdList,
                                                                       @Param("financeRecoOrderStatusNotList") List<FinanceRecoOrderStatus> financeRecoOrderStatusNotList);
}
