package com.hete.supply.scm.server.scm.process.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.ProcessMaterialReceiptCreateDto;
import com.hete.supply.scm.api.scm.entity.dto.ProcessMaterialReceiptQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.vo.ProcessMaterialReceiptExportVo;
import com.hete.supply.scm.api.scm.facade.ProcessMaterialReceiptFacade;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessMaterialReceiptBizService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author: RockyHuas
 * @date: 2022/11/21 17:49
 */
@DubboService
@RequiredArgsConstructor
public class ProcessMaterialReceiptFacadeImpl implements ProcessMaterialReceiptFacade {
    private final ProcessMaterialReceiptBizService processMaterialReceiptBizService;

    /**
     * 获取需要导出的总数
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<Integer> getExportTotals(ProcessMaterialReceiptQueryByApiDto dto) {
        return CommonResult.success(processMaterialReceiptBizService.getExportTotals(dto));
    }

    /**
     * 获取需要导出的列表
     *
     * @param dto
     * @return
     */
    @Override
    public CommonPageResult<ProcessMaterialReceiptExportVo> getExportList(ProcessMaterialReceiptQueryByApiDto dto) {
        return new CommonPageResult<>(processMaterialReceiptBizService.getExportList(dto));
    }

    /**
     * 创建原料收货单
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<Boolean> create(@RequestBody @Valid ProcessMaterialReceiptCreateDto dto) {
        return CommonResult.success(processMaterialReceiptBizService.create(dto));
    }
}
