package com.hete.supply.scm.server.scm.defect.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.DefectHandingSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DefectHandingExportVo;
import com.hete.supply.scm.api.scm.facade.DefectHandingFacade;
import com.hete.supply.scm.server.scm.defect.service.biz.DefectBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;

/**
 * @author ChenWenLong
 * @date 2024/2/28 15:50
 */
@DubboService
@RequiredArgsConstructor
@Validated
public class DefectHandingFacadeImpl implements DefectHandingFacade {
    private final DefectBizService defectBizService;


    @Override
    public CommonResult<Integer> getExportTotals(DefectHandingSearchDto dto) {
        return CommonResult.success(defectBizService.getExportTotals(dto));
    }


    @Override
    public CommonResult<ExportationListResultBo<DefectHandingExportVo>> getExportList(DefectHandingSearchDto dto) {
        return defectBizService.getExportList(dto);
    }

}
