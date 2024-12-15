package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.ProcessSettleOrderDto;
import com.hete.supply.scm.api.scm.entity.vo.ProcessSettleOrderExportVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;

public interface ProcessSettleOrderFacade {

    /**
     * 获取加工结算总数
     *
     * @param dto
     * @return
     */
    public CommonResult<Integer> getExportTotals(ProcessSettleOrderDto dto);

    /**
     * 获取加工结算导出列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult<ProcessSettleOrderExportVo> getExportList(ProcessSettleOrderDto dto);
}
