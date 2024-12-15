package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseDemandType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseParentOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SkuType;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseSkuCntVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/7/7 16:22
 */
@Data
@NoArgsConstructor
public class PurchaseSearchNewVo {

    @ApiModelProperty(value = "id")
    private Long purchaseParentOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "采购单状态")
    private PurchaseParentOrderStatus purchaseParentOrderStatus;

    @ApiModelProperty(value = "需求对象")
    private SkuType skuType;

    @ApiModelProperty(value = "商品类目")
    private String categoryName;

    @ApiModelProperty(value = "商品sku")
    private List<PurchaseSkuCntVo> purchaseSkuCntList;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private String warehouseTypes;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人名称")
    private String createUsername;

    @ApiModelProperty(value = "子单列表信息")
    private List<PurchaseSearchNewItemVo> purchaseSearchNewItemList;

    @ApiModelProperty(value = "封样图片")
    private List<String> sealImageFileCodeList;

    @ApiModelProperty(value = "采购可拆单数")
    private Integer canSplitCnt;

    @ApiModelProperty(value = "采购需求类型")
    private PurchaseDemandType purchaseDemandType;
}
