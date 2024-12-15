package com.hete.supply.scm.server.scm.develop.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.plm.api.developorder.enums.DevelopCreateType;
import com.hete.supply.plm.api.goods.enums.SkuDevType;
import com.hete.supply.scm.api.scm.entity.enums.DevelopChildOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ReviewResult;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 开发子单表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_child_order")
@ApiModel(value = "DevelopChildOrderPo对象", description = "开发子单表")
public class DevelopChildOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "develop_child_order_id", type = IdType.ASSIGN_ID)
    private Long developChildOrderId;


    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;


    @ApiModelProperty(value = "开发母单号")
    private String developParentOrderNo;


    @ApiModelProperty(value = "开发子单状态")
    private DevelopChildOrderStatus developChildOrderStatus;


    @ApiModelProperty(value = "商品类目")
    private String category;

    @ApiModelProperty(value = "商品类目id")
    private Long categoryId;


    @ApiModelProperty(value = "产品名称")
    private String skuEncode;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "spu")
    private String spu;


    @ApiModelProperty(value = "开款需求(PLM提供枚举)")
    private DevelopCreateType developCreateType;


    @ApiModelProperty(value = "是否加急(PLM提供枚举)")
    private BooleanType isUrgent;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDate;

    @ApiModelProperty(value = "预期收货时间")
    private LocalDateTime expectedArrivalDate;


    @ApiModelProperty(value = "取消原因")
    private String cancelReason;


    @ApiModelProperty(value = "开发类型(PLM提供枚举)")
    private SkuDevType skuDevType;


    @ApiModelProperty(value = "平台")
    private String platform;


    @ApiModelProperty(value = "平台ID")
    private Long platformId;


    @ApiModelProperty(value = "样品价格")
    private BigDecimal samplePrice;

    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "大货价格")
    private BigDecimal purchasePrice;


    @ApiModelProperty(value = "是否上架")
    private BooleanType isOnShelves;

    @ApiModelProperty(value = "审版结果")
    private ReviewResult reviewResult;

    @ApiModelProperty(value = "异常待处理")
    private BooleanType hasException;

    @ApiModelProperty(value = "是否打样")
    private BooleanType isSample;

    @ApiModelProperty(value = "是否需要原料")
    private BooleanType isNeedRaw;

    @ApiModelProperty(value = "是否下首单")
    private BooleanType hasFirstOrder;

    @ApiModelProperty(value = "预计购买成本")
    private BigDecimal expectedPurchaseCost;

    @ApiModelProperty(value = "需求描述")
    private String demandDesc;

    @ApiModelProperty(value = "打样数量")
    private Integer proofCnt;

    @ApiModelProperty(value = "母单创建人")
    private String parentCreateUser;

    @ApiModelProperty(value = "母单创建人")
    private String parentCreateUsername;

    @ApiModelProperty(value = "母单创建时间")
    private LocalDateTime parentCreateTime;

    @ApiModelProperty(value = "打版次数")
    private Integer pamphletTimes;

    @ApiModelProperty(value = "产前样单号")
    private String prenatalSampleOrderNo;

    @ApiModelProperty(value = "首单号")
    private String firstSampleOrderNo;

    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;


}
