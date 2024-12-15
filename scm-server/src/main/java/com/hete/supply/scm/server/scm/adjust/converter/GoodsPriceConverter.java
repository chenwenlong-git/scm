package com.hete.supply.scm.server.scm.adjust.converter;

import com.hete.supply.scm.server.scm.adjust.entity.po.GoodsPricePo;
import com.hete.supply.scm.server.scm.adjust.entity.vo.GoodsPriceInfoVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/6/19 11:23
 */
public class GoodsPriceConverter {


    public static List<GoodsPriceInfoVo> listPoToListVo(List<GoodsPricePo> goodsPricePoList) {
        return Optional.ofNullable(goodsPricePoList)
                .orElse(new ArrayList<>())
                .stream()
                .map(goodsPricePo -> {
                    GoodsPriceInfoVo goodsPriceInfoVo = new GoodsPriceInfoVo();
                    goodsPriceInfoVo.setGoodsPriceId(goodsPricePo.getGoodsPriceId());
                    goodsPriceInfoVo.setVersion(goodsPricePo.getVersion());
                    goodsPriceInfoVo.setSupplierCode(goodsPricePo.getSupplierCode());
                    goodsPriceInfoVo.setChannelId(goodsPricePo.getChannelId());
                    goodsPriceInfoVo.setChannelName(goodsPricePo.getChannelName());
                    goodsPriceInfoVo.setChannelPrice(goodsPricePo.getChannelPrice());
                    goodsPriceInfoVo.setEffectiveTime(goodsPricePo.getEffectiveTime());
                    goodsPriceInfoVo.setEffectiveRemark(goodsPricePo.getEffectiveRemark());
                    goodsPriceInfoVo.setGoodsPriceUniversal(goodsPricePo.getGoodsPriceUniversal());
                    return goodsPriceInfoVo;
                }).collect(Collectors.toList());

    }


}
