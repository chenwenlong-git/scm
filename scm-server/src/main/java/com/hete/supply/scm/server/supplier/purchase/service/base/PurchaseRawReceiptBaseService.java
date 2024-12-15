package com.hete.supply.scm.server.supplier.purchase.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseRawReceiptSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.RawReceiptBizType;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseRawReceiptOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderItemPo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/5/31 14:28
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PurchaseRawReceiptBaseService {
    private final PurchaseRawReceiptOrderItemDao purchaseRawReceiptOrderItemDao;

    /**
     * 查采购原料收货单列表查询条件
     *
     * @author ChenWenLong
     * @date 2023/5/30 14:15
     */
    public PurchaseRawReceiptSearchDto getSearchRawReceiptWhere(PurchaseRawReceiptSearchDto dto) {

        if (CollectionUtils.isNotEmpty(dto.getSkuBatchCodeList())) {
            List<PurchaseRawReceiptOrderItemPo> rawItemPoList = purchaseRawReceiptOrderItemDao.getListBySkuBatchCodeList(dto.getSkuBatchCodeList());
            if (CollectionUtils.isEmpty(rawItemPoList)) {
                return null;
            }
            List<PurchaseRawReceiptOrderItemPo> rawItemPoLikeList = purchaseRawReceiptOrderItemDao.getListByLikeSkuBatchCode(dto.getSkuBatchCode());
            if (CollectionUtils.isEmpty(rawItemPoLikeList)) {
                return null;
            }
            List<String> purchaseRawReceiptOrderNoList = rawItemPoList.stream().map(PurchaseRawReceiptOrderItemPo::getPurchaseRawReceiptOrderNo)
                    .distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(dto.getPurchaseRawReceiptOrderNoList())) {
                dto.setPurchaseRawReceiptOrderNoList(purchaseRawReceiptOrderNoList);
            } else {
                purchaseRawReceiptOrderNoList.retainAll(dto.getPurchaseRawReceiptOrderNoList());
                dto.setPurchaseRawReceiptOrderNoList(purchaseRawReceiptOrderNoList);
            }
            if (CollectionUtils.isEmpty(dto.getPurchaseRawReceiptOrderNoList())) {
                return null;
            }
        }
        if (StringUtils.isNotBlank(dto.getSkuBatchCode())) {
            List<PurchaseRawReceiptOrderItemPo> rawItemPoLikeList = purchaseRawReceiptOrderItemDao.getListByLikeSkuBatchCode(dto.getSkuBatchCode());
            if (CollectionUtils.isEmpty(rawItemPoLikeList)) {
                return null;
            }
            List<String> purchaseRawReceiptOrderNoList = rawItemPoLikeList.stream().map(PurchaseRawReceiptOrderItemPo::getPurchaseRawReceiptOrderNo)
                    .distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(dto.getPurchaseRawReceiptOrderNoList())) {
                dto.setPurchaseRawReceiptOrderNoList(purchaseRawReceiptOrderNoList);
            } else {
                purchaseRawReceiptOrderNoList.retainAll(dto.getPurchaseRawReceiptOrderNoList());
                dto.setPurchaseRawReceiptOrderNoList(purchaseRawReceiptOrderNoList);
            }
            if (CollectionUtils.isEmpty(dto.getPurchaseRawReceiptOrderNoList())) {
                return null;
            }

        }

        // 兼容新旧枚举检索
        if (RawReceiptBizType.DEVELOP.equals(dto.getRawReceiptBizType())) {
            dto.setRawReceiptBizTypeList(RawReceiptBizType.getRelatedTypes());
            dto.setRawReceiptBizType(null);
        }

        return dto;
    }
}
