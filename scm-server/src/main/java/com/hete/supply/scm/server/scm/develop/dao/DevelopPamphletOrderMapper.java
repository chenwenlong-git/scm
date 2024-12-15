package com.hete.supply.scm.server.scm.develop.dao;

import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopPamphletGroupByStatusBo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPamphletOrderPo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 开发版单表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-31
 */
@Mapper
interface DevelopPamphletOrderMapper extends BaseDataMapper<DevelopPamphletOrderPo> {

    List<DevelopPamphletGroupByStatusBo> getListByGroupByStatus(@Param("supplierCodeList") List<String> supplierCodeList);
}
