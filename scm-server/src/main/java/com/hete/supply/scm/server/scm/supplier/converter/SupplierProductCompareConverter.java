package com.hete.supply.scm.server.scm.supplier.converter;

import com.hete.supply.scm.server.scm.adjust.converter.GoodsPriceConverter;
import com.hete.supply.scm.server.scm.adjust.entity.po.GoodsPricePo;
import com.hete.supply.scm.server.scm.production.entity.vo.SupplierProductCompareItemVo;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierProductCompareItemDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 供应商产品对照表转换器
 *
 * @author ChenWenLong
 * @date 2023/3/29 10:57
 */
public class SupplierProductCompareConverter {

    public static List<SupplierProductComparePo> itemDtoToPo(List<SupplierProductCompareItemDto> dtoList) {
        return Optional.ofNullable(dtoList)
                .orElse(new ArrayList<>())
                .stream()
                .map(supplierProductCompareItemDto -> {
                    SupplierProductComparePo supplierProductComparePo = new SupplierProductComparePo();
                    supplierProductComparePo.setSupplierProductCompareId(supplierProductCompareItemDto.getSupplierProductCompareId());
                    supplierProductComparePo.setVersion(supplierProductCompareItemDto.getVersion());
                    supplierProductComparePo.setSupplierCode(supplierProductCompareItemDto.getSupplierCode());
                    supplierProductComparePo.setSupplierProductName(supplierProductCompareItemDto.getSupplierProductName());
                    supplierProductComparePo.setSupplierProductCompareStatus(supplierProductCompareItemDto.getSupplierProductCompareStatus());
                    return supplierProductComparePo;
                }).collect(Collectors.toList());
    }

    public static List<SupplierProductCompareItemVo> poToItemVo(List<SupplierProductComparePo> poList,
                                                                List<GoodsPricePo> goodsPricePoList) {
        return Optional.ofNullable(poList)
                .orElse(new ArrayList<>())
                .stream()
                .map(supplierProductComparePo -> {
                    SupplierProductCompareItemVo supplierProductCompareItemVo = new SupplierProductCompareItemVo();
                    supplierProductCompareItemVo.setSupplierProductCompareId(supplierProductComparePo.getSupplierProductCompareId());
                    supplierProductCompareItemVo.setSupplierProductName(supplierProductComparePo.getSupplierProductName());
                    supplierProductCompareItemVo.setSupplierCode(supplierProductComparePo.getSupplierCode());
                    supplierProductCompareItemVo.setVersion(supplierProductComparePo.getVersion());
                    supplierProductCompareItemVo.setSku(supplierProductComparePo.getSku());
                    supplierProductCompareItemVo.setSupplierProductCompareStatus(supplierProductComparePo.getSupplierProductCompareStatus());
                    // 获取当前供应商对应的调价信息
                    List<GoodsPricePo> goodsPricePos = goodsPricePoList.stream()
                            .filter(goodsPricePo -> goodsPricePo.getSupplierCode().equals(supplierProductComparePo.getSupplierCode()))
                            .collect(Collectors.toList());
                    supplierProductCompareItemVo.setGoodsPriceInfoList(GoodsPriceConverter.listPoToListVo(goodsPricePos));
                    return supplierProductCompareItemVo;
                }).collect(Collectors.toList());
    }


}
