package com.hete.supply.scm.server.scm.process.converter;


import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialReceiptItemPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialReceiptPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessMaterialReceiptDetailVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessMaterialReceiptItemVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: RockyHuas
 * @date: 2022/11/4 10:01
 */
public class ProcessMaterialReceiptImplConverter {

    /**
     * po 转 detail vo
     *
     * @param processMaterialReceiptPo
     * @param processMaterialReceiptItemPos
     * @return
     */
    public static ProcessMaterialReceiptDetailVo poToDetailVo(ProcessMaterialReceiptPo processMaterialReceiptPo, List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos) {
        ProcessMaterialReceiptDetailVo processMaterialReceiptDetailVo = ProcessMaterialReceiptConverter.INSTANCE.convert(processMaterialReceiptPo);

        List<ProcessMaterialReceiptItemVo> receiptItemVos = Optional.ofNullable(processMaterialReceiptItemPos)
                .orElse(new ArrayList<>()).stream()
                .map(ProcessMaterialReceiptConverter.INSTANCE::convert)
                .collect(Collectors.toList());

        processMaterialReceiptDetailVo.setMaterialReceiptItems(receiptItemVos);

        return processMaterialReceiptDetailVo;
    }
}
