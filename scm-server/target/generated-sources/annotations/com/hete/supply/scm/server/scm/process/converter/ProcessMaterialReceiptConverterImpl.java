package com.hete.supply.scm.server.scm.process.converter;

import com.hete.supply.scm.api.scm.entity.dto.ProcessMaterialReceiptCreateDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialReceiptItemPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialReceiptPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessMaterialReceiptDetailVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessMaterialReceiptItemVo;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class ProcessMaterialReceiptConverterImpl implements ProcessMaterialReceiptConverter {

    @Override
    public ProcessMaterialReceiptPo convert(ProcessMaterialReceiptCreateDto dto) {
        if ( dto == null ) {
            return null;
        }

        ProcessMaterialReceiptPo processMaterialReceiptPo = new ProcessMaterialReceiptPo();

        processMaterialReceiptPo.setProcessOrderNo( dto.getProcessOrderNo() );
        processMaterialReceiptPo.setRepairOrderNo( dto.getRepairOrderNo() );
        processMaterialReceiptPo.setDeliveryNo( dto.getDeliveryNo() );
        processMaterialReceiptPo.setDeliveryNum( dto.getDeliveryNum() );
        processMaterialReceiptPo.setDeliveryUser( dto.getDeliveryUser() );
        processMaterialReceiptPo.setDeliveryUsername( dto.getDeliveryUsername() );
        processMaterialReceiptPo.setDeliveryTime( dto.getDeliveryTime() );
        processMaterialReceiptPo.setDeliveryWarehouseCode( dto.getDeliveryWarehouseCode() );
        processMaterialReceiptPo.setDeliveryWarehouseName( dto.getDeliveryWarehouseName() );
        processMaterialReceiptPo.setDeliveryNote( dto.getDeliveryNote() );

        return processMaterialReceiptPo;
    }

    @Override
    public ProcessMaterialReceiptDetailVo convert(ProcessMaterialReceiptPo po) {
        if ( po == null ) {
            return null;
        }

        ProcessMaterialReceiptDetailVo processMaterialReceiptDetailVo = new ProcessMaterialReceiptDetailVo();

        processMaterialReceiptDetailVo.setProcessMaterialReceiptId( po.getProcessMaterialReceiptId() );
        processMaterialReceiptDetailVo.setProcessOrderNo( po.getProcessOrderNo() );
        processMaterialReceiptDetailVo.setRepairOrderNo( po.getRepairOrderNo() );
        processMaterialReceiptDetailVo.setMaterialReceiptType( po.getMaterialReceiptType() );
        processMaterialReceiptDetailVo.setDeliveryNo( po.getDeliveryNo() );
        processMaterialReceiptDetailVo.setProcessMaterialReceiptStatus( po.getProcessMaterialReceiptStatus() );
        processMaterialReceiptDetailVo.setDeliveryNum( po.getDeliveryNum() );
        processMaterialReceiptDetailVo.setDeliveryTime( po.getDeliveryTime() );
        processMaterialReceiptDetailVo.setReceiptUsername( po.getReceiptUsername() );
        processMaterialReceiptDetailVo.setReceiptTime( po.getReceiptTime() );
        processMaterialReceiptDetailVo.setDeliveryWarehouseCode( po.getDeliveryWarehouseCode() );
        processMaterialReceiptDetailVo.setDeliveryWarehouseName( po.getDeliveryWarehouseName() );
        processMaterialReceiptDetailVo.setDeliveryNote( po.getDeliveryNote() );
        processMaterialReceiptDetailVo.setPlaceOrderTime( po.getPlaceOrderTime() );
        processMaterialReceiptDetailVo.setPlaceOrderUsername( po.getPlaceOrderUsername() );
        processMaterialReceiptDetailVo.setPlatform( po.getPlatform() );
        processMaterialReceiptDetailVo.setVersion( po.getVersion() );

        return processMaterialReceiptDetailVo;
    }

    @Override
    public ProcessMaterialReceiptItemVo convert(ProcessMaterialReceiptItemPo po) {
        if ( po == null ) {
            return null;
        }

        ProcessMaterialReceiptItemVo processMaterialReceiptItemVo = new ProcessMaterialReceiptItemVo();

        processMaterialReceiptItemVo.setProcessMaterialReceiptItemId( po.getProcessMaterialReceiptItemId() );
        processMaterialReceiptItemVo.setProcessMaterialReceiptId( po.getProcessMaterialReceiptId() );
        processMaterialReceiptItemVo.setSku( po.getSku() );
        processMaterialReceiptItemVo.setSkuBatchCode( po.getSkuBatchCode() );
        processMaterialReceiptItemVo.setDeliveryNum( po.getDeliveryNum() );
        processMaterialReceiptItemVo.setReceiptNum( po.getReceiptNum() );
        processMaterialReceiptItemVo.setVersion( po.getVersion() );

        return processMaterialReceiptItemVo;
    }
}
