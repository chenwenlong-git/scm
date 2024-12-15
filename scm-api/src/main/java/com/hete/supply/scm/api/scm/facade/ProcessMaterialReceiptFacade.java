package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.ProcessMaterialReceiptCreateDto;
import com.hete.supply.scm.api.scm.entity.dto.ProcessMaterialReceiptQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.vo.ProcessMaterialReceiptExportVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;

/**
 * @Author: RockyHuas
 * @date: 2022/11/28 16:21
 */
public interface ProcessMaterialReceiptFacade {

    /**
     * 获取原料收货总数
     *
     * @param dto
     * @return
     */
    public CommonResult<Integer> getExportTotals(ProcessMaterialReceiptQueryByApiDto dto);

    /**
     * 获取原料收货列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult<ProcessMaterialReceiptExportVo> getExportList(ProcessMaterialReceiptQueryByApiDto dto);

    /**
     * 创建原料收货单
     *
     * @param dto
     * @return
     */
    public CommonResult<Boolean> create(ProcessMaterialReceiptCreateDto dto);
}
