package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.DevelopReviewSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.DevelopReviewOrderType;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopReviewGroupByStatusBo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopReviewOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopReviewSearchVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 开发审版单表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-31
 */
@Mapper
interface DevelopReviewOrderMapper extends BaseDataMapper<DevelopReviewOrderPo> {

    IPage<DevelopReviewSearchVo> searchDevelopReview(Page<Void> page, @Param("dto") DevelopReviewSearchDto dto);

    List<DevelopReviewGroupByStatusBo> getListByGroupByStatus(@Param("typeList") List<DevelopReviewOrderType> typeList,
                                                              @Param("supplierCodeList") List<String> supplierCodeList);

    Integer getReviewExportTotals(@Param("dto") DevelopReviewSearchDto dto);
}
