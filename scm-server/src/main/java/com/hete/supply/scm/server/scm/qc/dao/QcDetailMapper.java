package com.hete.supply.scm.server.scm.qc.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.QcSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.QcState;
import com.hete.supply.scm.server.scm.qc.entity.bo.QcDetailExportBo;
import com.hete.supply.scm.server.scm.qc.entity.bo.QcWaitDetailBo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 质检单详情 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-10-09
 */
@Mapper
interface QcDetailMapper extends BaseDataMapper<QcDetailPo> {

    /**
     * 获取质检明细导出的统计数据。
     *
     * @param qcSearchDto 查询条件
     * @return 质检明细导出的统计总数
     */
    Integer getQcDetailExportTotals(@Param("qcSearchDto") QcSearchDto qcSearchDto);

    /**
     * 根据质检单号列表查询质检明细并返回分页数据。
     *
     * @param page        分页信息
     * @param qcSearchDto 质检单号列表
     * @return 质检明细的分页数据
     */
    IPage<QcDetailExportBo> getQcDetailExportList(Page<QcDetailPo> page, QcSearchDto qcSearchDto);

    /**
     * 统计待质检数据
     *
     * @param skuCodeList:
     * @param qcStateList:
     * @return List<QcWaitDetailBo>
     * @author ChenWenLong
     * @date 2023/12/26 15:12
     */
    List<QcWaitDetailBo> getQcWaitDetail(@Param("skuCodeList") List<String> skuCodeList, @Param("qcStateList") List<QcState> qcStateList);

}
