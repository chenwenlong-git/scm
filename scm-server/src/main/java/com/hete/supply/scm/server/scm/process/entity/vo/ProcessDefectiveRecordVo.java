package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.plm.api.goods.entity.vo.PlmAttrSkuVo;
import com.hete.supply.scm.api.scm.entity.enums.DefectiveHandleMethod;
import com.hete.supply.scm.api.scm.entity.enums.DefectiveHandleType;
import com.hete.supply.scm.api.scm.entity.enums.ProcessDefectiveRecordOrderType;
import com.hete.supply.scm.api.scm.entity.enums.ProcessDefectiveRecordStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author RockyHuas
 * @date 2023/06/01 10:52
 */
@Data
@NoArgsConstructor
public class ProcessDefectiveRecordVo {

    @ApiModelProperty(value = "单号")
    private String processDefectiveRecordNo;

    @ApiModelProperty(value = "次品处理方式")
    private DefectiveHandleMethod defectiveHandleMethod;

    @ApiModelProperty(value = "次品处理类型")
    private DefectiveHandleType defectiveHandleType;

    @ApiModelProperty(value = "次品处理状态")
    private ProcessDefectiveRecordStatus processDefectiveRecordStatus;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "变体属性")
    private List<PlmAttrSkuVo> variantSkuList;

    @ApiModelProperty(value = "次品数量")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "不良原因")
    private String badReason;

    @ApiModelProperty(value = "图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "负责人编号")
    private String principalUser;

    @ApiModelProperty(value = "负责人名称")
    private String principalUsername;

    @ApiModelProperty(value = "供应商编号")
    private String supplierUser;

    @ApiModelProperty(value = "供应商名称")
    private String supplierUsername;

    @ApiModelProperty(value = "关联的单据编号")
    private String relatedOrderNo;

    @ApiModelProperty(value = "打印的单据编号")
    private String printOrderNo;

    @ApiModelProperty(value = "关联的单据类型")
    private ProcessDefectiveRecordOrderType processDefectiveRecordOrderType;

    @ApiModelProperty(value = "关联的单据状态")
    private String relatedOrderStatus;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
