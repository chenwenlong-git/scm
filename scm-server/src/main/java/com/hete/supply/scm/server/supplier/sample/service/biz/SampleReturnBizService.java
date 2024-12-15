package com.hete.supply.scm.server.supplier.sample.service.biz;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.SampleReturnDto;
import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.supplier.sample.converter.SupplierSampleConverter;
import com.hete.supply.scm.server.supplier.sample.dao.SampleReturnOrderDao;
import com.hete.supply.scm.server.supplier.sample.dao.SampleReturnOrderItemDao;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleReturnConfirmDto;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleReturnItemDto;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleReturnNoDto;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderPo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleReturnDetailVo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleReturnVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.core.holder.GlobalContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/17 20:09
 */
@Service
@RequiredArgsConstructor
@Validated
public class SampleReturnBizService {
    private final SampleReturnOrderDao sampleReturnOrderDao;
    private final SampleReturnOrderItemDao sampleReturnOrderItemDao;
    private final LogBaseService logBaseService;

    public CommonPageResult.PageInfo<SampleReturnVo> searchProductPurchase(SampleReturnDto dto) {
        List<String> sampleReturnOrderNoList = new ArrayList<>();

        if (StringUtils.isNotBlank(dto.getSampleChildOrderNo())) {
            List<SampleReturnOrderItemPo> sampleReturnOrderItemPoList = sampleReturnOrderItemDao.getListLikeChildOrderNo(dto.getSampleChildOrderNo());
            final List<String> itemReturnOrderNoList = sampleReturnOrderItemPoList.stream()
                    .map(SampleReturnOrderItemPo::getSampleReturnOrderNo)
                    .distinct()
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(itemReturnOrderNoList)) {
                return new CommonPageResult.PageInfo<>();
            }

            sampleReturnOrderNoList.addAll(itemReturnOrderNoList);
        }


        return sampleReturnOrderDao.searchProductPurchase(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto, sampleReturnOrderNoList);
    }

    public SampleReturnDetailVo sampleReturnDetail(SampleReturnNoDto dto) {
        SampleReturnOrderPo sampleReturnOrderPo = sampleReturnOrderDao.getOneByNo(dto.getSampleReturnOrderNo());
        if (null == sampleReturnOrderPo) {
            final List<SampleReturnOrderPo> sampleReturnOrderPoList = sampleReturnOrderDao.getListByTrackingNo(dto.getSampleReturnOrderNo());
            if (CollectionUtils.isEmpty(sampleReturnOrderPoList) || sampleReturnOrderPoList.size() > 1) {
                throw new ParamIllegalException("当前单号{}无法查找到样品退货单，获取样品退货单详情失败",
                        dto.getSampleReturnOrderNo());
            }
            sampleReturnOrderPo = sampleReturnOrderPoList.get(0);
        }
        Assert.notNull(sampleReturnOrderPo, () -> new BizException("查找不到样品退货单，打开详情失败"));
        List<SampleReturnOrderItemPo> sampleReturnOrderItemPoList = sampleReturnOrderItemDao.getListByReturnOrderNo(sampleReturnOrderPo.getSampleReturnOrderNo());
        Assert.notEmpty(sampleReturnOrderItemPoList, () -> new BizException("查找不到样品退货单，打开详情失败"));

        return SupplierSampleConverter.returnPoToDetailVo(sampleReturnOrderPo, sampleReturnOrderItemPoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void sampleReturnConfirm(SampleReturnConfirmDto dto) {
        this.checkReturnCnt(dto.getSampleReturnItemList());

        final SampleReturnOrderPo sampleReturnOrderPo = sampleReturnOrderDao.getByIdVersion(dto.getSampleReturnOrderId(), dto.getVersion());
        Assert.notNull(sampleReturnOrderPo, () -> new BizException("找不到样品退货单，退货失败"));
        final int receiptCnt = dto.getSampleReturnItemList().stream().mapToInt(SampleReturnItemDto::getReceiptCnt).sum();
        sampleReturnOrderPo.setReturnOrderStatus(ReceiptOrderStatus.RECEIPTED);
        sampleReturnOrderPo.setReceiptTime(LocalDateTime.now());
        sampleReturnOrderPo.setReceiptUser(GlobalContext.getUserKey());
        sampleReturnOrderPo.setReceiptUsername(GlobalContext.getUsername());
        sampleReturnOrderPo.setReceiptCnt(receiptCnt);
        sampleReturnOrderDao.updateByIdVersion(sampleReturnOrderPo);
        List<SampleReturnOrderItemPo> sampleReturnOrderItemPoList = SupplierSampleConverter.returnConfirmDtoListToPo(dto.getSampleReturnItemList());
        sampleReturnOrderItemDao.updateBatchByIdVersion(sampleReturnOrderItemPoList);

        logBaseService.simpleLog(LogBizModule.SUPPLIER_SAMPLE_RETURN_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                sampleReturnOrderPo.getSampleReturnOrderNo(), ReceiptOrderStatus.RECEIPTED.getRemark(), Collections.emptyList());
    }

    private void checkReturnCnt(List<SampleReturnItemDto> sampleReturnItemList) {
        sampleReturnItemList.forEach(item -> {
            if (item.getReceiptCnt() > item.getReturnCnt()) {
                throw new ParamIllegalException("收货数不能大于发货数,请修改填写的收货数后重新提交!");
            }
        });
    }
}
