package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.entity.dto.ProduceDataAttrImportDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.*;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;

/**
 * @author ChenWenLong
 * @date 2024/1/23 09:56
 */
public interface ProduceDataImportationFacade {
    /**
     * 导入生产信息原料sku
     *
     * @param importDto
     * @return
     */
    CommonResult<ImportationResultVo> importProduceDataItemRaw(ImportationReqDto<ProduceDataItemRawImportationDto> importDto);

    /**
     * 导入生产信息规格书
     *
     * @param importDto
     * @return
     */
    CommonResult<ImportationResultVo> importProduceDataSpec(ImportationReqDto<ProduceDataSpecImportationDto> importDto);

    /**
     * 导入生产信息的BOM供应商
     *
     * @param importDto
     * @return
     */
    CommonResult<ImportationResultVo> importProduceDataItemSupplier(ImportationReqDto<ProduceDataItemSupplierImportationDto> importDto);

    /**
     * 导入生产属性
     *
     * @param importDto
     * @return
     */
    CommonResult<ImportationResultVo> importProduceDataAttr(ImportationReqDto<ProduceDataAttrImportationDto> importDto);

    /**
     * @Description 商品信息导入
     * @author yanjiawei
     * @Date 2024/11/20 17:26
     */
    CommonResult<ImportationResultVo> importProdDataAttr(ImportationReqDto<ProduceDataAttrImportDto> dto);

    /**
     * @Description 商品+供应商信息导入
     * @author yanjiawei
     * @Date 2024/11/20 10:23
     */
    CommonResult<ImportationResultVo> importProduceDataRawProcess(ImportationReqDto<ProduceDataRawProcessImportDto> importDto);
}
