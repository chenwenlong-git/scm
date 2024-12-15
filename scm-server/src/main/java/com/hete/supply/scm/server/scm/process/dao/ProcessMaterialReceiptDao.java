package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.ProcessMaterialReceiptQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.vo.ProcessMaterialReceiptExportVo;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessMaterialReceiptQueryDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialReceiptPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessMaterialReceiptVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 加工原料收货单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Component
@Validated
public class ProcessMaterialReceiptDao extends BaseDao<ProcessMaterialReceiptMapper, ProcessMaterialReceiptPo> {

    /**
     * 分页查询
     *
     * @param page
     * @param processMaterialReceiptQueryDto
     * @return
     */
    public CommonPageResult.PageInfo<ProcessMaterialReceiptVo> getByPage(Page<Void> page, ProcessMaterialReceiptQueryDto processMaterialReceiptQueryDto) {
        IPage<ProcessMaterialReceiptVo> pageResult = baseMapper.getByPage(page, processMaterialReceiptQueryDto);

        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 统计导出的总数
     *
     * @param dto
     * @return
     */
    public Integer getExportTotals(ProcessMaterialReceiptQueryByApiDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    /**
     * 查询导出的列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<ProcessMaterialReceiptExportVo> getExportList(Page<Void> page, ProcessMaterialReceiptQueryByApiDto dto) {
        IPage<ProcessMaterialReceiptExportVo> pageResult = baseMapper.getExportList(page, dto);

        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 通过加工单查询原料收货单
     *
     * @param processOrderNo
     * @return
     */
    public List<ProcessMaterialReceiptPo> getByProcessOrderNo(String processOrderNo) {
        if (StringUtils.isBlank(processOrderNo)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessMaterialReceiptPo>lambdaQuery()
                .eq(ProcessMaterialReceiptPo::getProcessOrderNo, processOrderNo)
        );
    }

    public List<ProcessMaterialReceiptPo> listByRepairOrderNo(String repairOrderNo) {
        if (StringUtils.isBlank(repairOrderNo)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessMaterialReceiptPo>lambdaQuery()
                .eq(ProcessMaterialReceiptPo::getRepairOrderNo, repairOrderNo));
    }

    public List<ProcessMaterialReceiptPo> listByRepairOrderNos(Collection<String> repairOrderNos) {
        return CollectionUtils.isEmpty(repairOrderNos) ?
                Collections.emptyList() :
                list(Wrappers.<ProcessMaterialReceiptPo>lambdaQuery()
                        .in(ProcessMaterialReceiptPo::getRepairOrderNo, repairOrderNos));
    }

    public List<ProcessMaterialReceiptPo> getByProcessOrderNos(List<String> processOrderNos) {
        return list(Wrappers.<ProcessMaterialReceiptPo>lambdaQuery()
                .in(ProcessMaterialReceiptPo::getProcessOrderNo, processOrderNos)
        );

    }

    /**
     * 通过出库单号查询收货单
     *
     * @param deliveryNo
     * @return
     */
    public ProcessMaterialReceiptPo getByDeliveryNo(String deliveryNo) {
        return getOne(Wrappers.<ProcessMaterialReceiptPo>lambdaQuery().eq(ProcessMaterialReceiptPo::getDeliveryNo, deliveryNo));
    }

    /**
     * 通过通过出库单查询
     *
     * @param deliveryNoList
     * @return
     */
    public List<ProcessMaterialReceiptPo> getByDeliveryNos(List<String> deliveryNoList) {
        return list(Wrappers.<ProcessMaterialReceiptPo>lambdaQuery().in(ProcessMaterialReceiptPo::getDeliveryNo, deliveryNoList));
    }

    public List<ProcessMaterialReceiptPo> getListByRepairOrderNoList(List<String> repairOrderNoList) {
        if (CollectionUtils.isEmpty(repairOrderNoList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessMaterialReceiptPo>lambdaQuery()
                .in(ProcessMaterialReceiptPo::getRepairOrderNo, repairOrderNoList));

    }

    public List<ProcessMaterialReceiptPo> getListByRepairOrderNo(String repairOrderNo) {
        if (StringUtils.isBlank(repairOrderNo)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessMaterialReceiptPo>lambdaQuery()
                .eq(ProcessMaterialReceiptPo::getRepairOrderNo, repairOrderNo));

    }
}
