package com.hete.supply.scm.server.scm.dao;

import com.hete.supply.scm.server.scm.entity.bo.ProduceDataItemGetSuggestSupplierBo;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemSupplierPo;
import com.hete.supply.scm.server.scm.entity.vo.ProduceDataItemGetSuggestSupplierVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 生产信息详情关联供应商 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-02-23
 */
@Mapper
interface ProduceDataItemSupplierMapper extends BaseDataMapper<ProduceDataItemSupplierPo> {

    List<ProduceDataItemGetSuggestSupplierVo> getListBySuggestSupplierBoList(@NotEmpty @Param("boList") List<ProduceDataItemGetSuggestSupplierBo> boList);

}
