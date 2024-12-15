package com.hete.supply.scm.server.scm.adjust.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.mc.api.workflow.entity.dto.FeiShuWorkflowCreateDto;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.server.scm.adjust.entity.bo.AdjustApproveBo;
import com.hete.supply.scm.server.scm.adjust.entity.bo.GoodsAdjustDetailItemBo;
import com.hete.supply.scm.server.scm.adjust.entity.bo.OrderAdjustDetailItemBo;
import com.hete.supply.scm.server.scm.enums.FeishuAuditOrderType;
import com.hete.supply.scm.server.scm.enums.FeishuWorkflowType;
import com.hete.supply.scm.server.scm.feishu.dao.FeishuAuditOrderDao;
import com.hete.supply.scm.server.scm.feishu.service.base.AbstractApproveCreator;
import com.hete.support.id.service.IdGenerateService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author weiwenxin
 * @date 2024/6/18 15:46
 */
public class AdjustPriceApproveCreator extends AbstractApproveCreator<AdjustApproveBo> {
    public AdjustPriceApproveCreator(IdGenerateService idGenerateService,
                                     McRemoteService mcRemoteService,
                                     FeishuAuditOrderDao feishuAuditOrderDao) {
        super(idGenerateService, mcRemoteService, feishuAuditOrderDao);
    }

    @Override
    protected FeishuWorkflowType getFeiShuWorkflowType() {
        return FeishuWorkflowType.ADJUST_PRICE_APPROVE;
    }

    @Override
    protected FeishuAuditOrderType getFeiShuAuditOrderType() {
        return FeishuAuditOrderType.ADJUST_PRICE_APPROVE;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.TextField> getTextFieldList(AdjustApproveBo adjustApproveBo) {
        List<FeiShuWorkflowCreateDto.TextField> textFieldList = new ArrayList<>();
        final FeiShuWorkflowCreateDto.TextField textField1 = new FeiShuWorkflowCreateDto.TextField();
        final FeiShuWorkflowCreateDto.TextField textField2 = new FeiShuWorkflowCreateDto.TextField();
        textField1.setId("adjustPriceApproveNo");
        textField1.setType("input");
        textField2.setId("supplierCode");
        textField2.setType("input");
        textField1.setValue(adjustApproveBo.getAdjustPriceApproveNo());
        textField2.setValue(adjustApproveBo.getSupplierCode());

        textFieldList.add(textField1);
        textFieldList.add(textField2);
        return textFieldList;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.MultiTextField> getMultiTextField(AdjustApproveBo adjustApproveBo) {
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.DayField> getDayField(AdjustApproveBo adjustApproveBo) {
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.DayZoneField> getDayZoneField(AdjustApproveBo adjustApproveBo) {
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.MultiFileField> getMultiFileField(AdjustApproveBo adjustApproveBo) {
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.DetailField> getDetailField(AdjustApproveBo adjustApproveBo) {
        final List<OrderAdjustDetailItemBo> orderAdjustList = Optional.ofNullable(adjustApproveBo.getOrderAdjustList()).orElse(new ArrayList<>());
        final List<GoodsAdjustDetailItemBo> goodsAdjustList = Optional.ofNullable(adjustApproveBo.getGoodsAdjustList()).orElse(new ArrayList<>());
        if (CollectionUtils.isEmpty(orderAdjustList) && CollectionUtils.isEmpty(goodsAdjustList)) {
            return null;
        }

        List<FeiShuWorkflowCreateDto.DetailField> detailFieldList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(orderAdjustList)) {
            final FeiShuWorkflowCreateDto.DetailField detailField = new FeiShuWorkflowCreateDto.DetailField();
            List<List<? super FeiShuWorkflowCreateDto.BaseField>> textFieldListList = new ArrayList<>();
            detailField.setId("orderAdjustList");
            detailField.setType("fieldList");

            orderAdjustList.forEach(itemBo -> {
                List<? super FeiShuWorkflowCreateDto.BaseField> textFieldList = new ArrayList<>();
                final FeiShuWorkflowCreateDto.TextField textField1 = new FeiShuWorkflowCreateDto.TextField();
                final FeiShuWorkflowCreateDto.TextField textField2 = new FeiShuWorkflowCreateDto.TextField();
                final FeiShuWorkflowCreateDto.TextField textField3 = new FeiShuWorkflowCreateDto.TextField();
                final FeiShuWorkflowCreateDto.TextField textField4 = new FeiShuWorkflowCreateDto.TextField();
                final FeiShuWorkflowCreateDto.TextField textField5 = new FeiShuWorkflowCreateDto.TextField();

                textField1.setId("sku");
                textField1.setType("input");
                textField2.setId("purchaseMsg");
                textField2.setType("input");
                textField3.setId("orderAdjust");
                textField3.setType("input");
                textField4.setId("adjustReason");
                textField4.setType("input");
                textField5.setId("remark");
                textField5.setType("input");

                textField1.setValue(itemBo.getSkuMsg());
                textField2.setValue(itemBo.getPurchaseMsg());
                textField3.setValue("结算单价(前): " + itemBo.getOriginalPrice() + "\n结算单价(后): " + itemBo.getOrderAdjustStr());
                textField4.setValue(itemBo.getAdjustReason());
                textField5.setValue(itemBo.getRemark());

                textFieldList.add(textField1);
                textFieldList.add(textField2);
                textFieldList.add(textField3);
                textFieldList.add(textField4);
                textFieldList.add(textField5);
                textFieldListList.add(textFieldList);
            });

            detailField.setValue(textFieldListList);
            detailFieldList.add(detailField);
        }


        // 商品调价
        if (CollectionUtils.isNotEmpty(goodsAdjustList)) {

            final FeiShuWorkflowCreateDto.DetailField detailFieldGoods = new FeiShuWorkflowCreateDto.DetailField();
            List<List<? super FeiShuWorkflowCreateDto.BaseField>> textFieldListGoodsList = new ArrayList<>();
            detailFieldGoods.setId("goodsAdjustList");
            detailFieldGoods.setType("fieldList");

            goodsAdjustList.forEach(itemBo -> {
                List<? super FeiShuWorkflowCreateDto.BaseField> textFieldGoodsList = new ArrayList<>();
                final FeiShuWorkflowCreateDto.TextField textField1 = new FeiShuWorkflowCreateDto.TextField();
                final FeiShuWorkflowCreateDto.TextField textField2 = new FeiShuWorkflowCreateDto.TextField();
                final FeiShuWorkflowCreateDto.TextField textField3 = new FeiShuWorkflowCreateDto.TextField();
                final FeiShuWorkflowCreateDto.TextField textField4 = new FeiShuWorkflowCreateDto.TextField();
                final FeiShuWorkflowCreateDto.TextField textField5 = new FeiShuWorkflowCreateDto.TextField();
                final FeiShuWorkflowCreateDto.TextField textField6 = new FeiShuWorkflowCreateDto.TextField();


                textField1.setId("sku");
                textField1.setType("input");
                textField2.setId("channel");
                textField2.setType("input");
                textField3.setId("originalPrice");
                textField3.setType("input");
                textField4.setId("adjustPrice");
                textField4.setType("input");
                textField5.setId("effectiveTime");
                textField5.setType("input");
                textField6.setId("effectiveRemark");
                textField6.setType("input");


                textField1.setValue("SKU：" + itemBo.getSku() + "\n产品名称: " + itemBo.getSkuEncode());
                textField2.setValue(itemBo.getChannel() + "（" + itemBo.getUniversal() + "）");
                textField3.setValue(itemBo.getOriginalPriceStr());
                textField4.setValue(itemBo.getAdjustPriceStr());
                textField5.setValue(itemBo.getEffectiveTimeStr());
                textField6.setValue(itemBo.getEffectiveRemark());


                textFieldGoodsList.add(textField1);
                textFieldGoodsList.add(textField2);
                textFieldGoodsList.add(textField3);
                textFieldGoodsList.add(textField4);
                textFieldGoodsList.add(textField5);
                textFieldGoodsList.add(textField6);

                textFieldListGoodsList.add(textFieldGoodsList);
            });

            detailFieldGoods.setValue(textFieldListGoodsList);
            detailFieldList.add(detailFieldGoods);
        }

        return detailFieldList;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.MultiContentField> getMultiContentField(AdjustApproveBo adjustApproveBo) {
        return null;
    }

    @Override
    protected List<FeiShuWorkflowCreateDto.NodeApproverUserCode> getNodeApproverUserCodeList(AdjustApproveBo adjustApproveBo) {
        final List<String> nodeApproverUserCodeList = adjustApproveBo.getNodeApproverUserCodeList();
        if (CollectionUtils.isEmpty(nodeApproverUserCodeList)) {
            return null;
        }
        List<FeiShuWorkflowCreateDto.NodeApproverUserCode> detailFieldList = new ArrayList<>();
        final FeiShuWorkflowCreateDto.NodeApproverUserCode detailField = new FeiShuWorkflowCreateDto.NodeApproverUserCode();
        detailField.setKey("nodeId");
        detailField.setValue(nodeApproverUserCodeList);
        detailFieldList.add(detailField);
        return detailFieldList;
    }
}
