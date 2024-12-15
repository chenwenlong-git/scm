package com.hete.supply.scm.server.scm.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataAttrSkuDto;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataAttrSkuVo;
import com.hete.supply.scm.server.scm.entity.dto.ProduceDataAttrValueDto;
import com.hete.supply.scm.server.scm.entity.dto.ProduceDataAttrValueListDto;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataAttrPo;
import com.hete.supply.scm.server.scm.entity.vo.ProduceDataAttrValueListVo;
import com.hete.supply.scm.server.scm.entity.vo.ProduceDataAttrValueVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 生产信息的生产属性表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-21
 */
@Mapper
interface ProduceDataAttrMapper extends BaseDataMapper<ProduceDataAttrPo> {

    List<ProduceDataAttrValueVo> getProduceDataAttrValue(@Param("dtoList") List<ProduceDataAttrValueDto> dtoList);

    IPage<String> selectProduceDataAttrWeightPage(Page<Void> page, @Param("attributeNameIdList") List<Long> attributeNameIdList);

    List<ProduceDataAttrValueListVo> getProduceDataAttrValueListByValueList(@Param("dto") ProduceDataAttrValueListDto dto);

    IPage<String> selectProduceDataAttrPage(Page<Void> page, @Param("attributeNameIdList") List<Long> attributeNameIdList);

    IPage<ProduceDataAttrSkuVo> getPageBySkuList(Page<Void> page, @Param("dto") ProduceDataAttrSkuDto dto);

    IPage<String> getSkuByPage(Page<String> page, List<Long> attributeNameIdList);
}
