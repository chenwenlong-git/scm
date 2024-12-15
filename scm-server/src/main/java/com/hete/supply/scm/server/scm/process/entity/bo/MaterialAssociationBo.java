package com.hete.supply.scm.server.scm.process.entity.bo;

import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialBackItemPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialBackPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialReceiptItemPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialReceiptPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/3/1.
 */
@Data
@ApiModel(description = "原料关联信息")
public class MaterialAssociationBo {

    @ApiModelProperty(notes = "关联单号")
    private String relateOrderNo;

    @ApiModelProperty(notes = "原料收货列表")
    private List<ProcessMaterialReceiptPo> materialReceiptList = Collections.emptyList();

    @ApiModelProperty(notes = "原料收货单明细列表")
    private List<ProcessMaterialReceiptItemPo> materialReceiptItemList = Collections.emptyList();

    @ApiModelProperty(notes = "原料归还列表")
    private List<ProcessMaterialBackPo> materialBackList = Collections.emptyList();

    @ApiModelProperty(notes = "原料归还明细明细列表")
    private List<ProcessMaterialBackItemPo> materialBackItemList = Collections.emptyList();
}
