package com.hete.supply.scm.server.scm.process.converter;

import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderDescVo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderItemPrintVo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderMaterialPrintVo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderPrintByWmsVo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderPrintDeliveryOrderVo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderProcedureVo;
import com.hete.supply.scm.api.scm.entity.vo.SampleChildOrderInfoVo;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderCreateDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderEditDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderDescPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderItemPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderMaterialPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderProcedurePo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderDetailVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderItemVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderMaterialVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderPrintVo;
import com.hete.supply.scm.server.scm.sample.entity.bo.SampleProcessAndRawBo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleProcessAndRawVo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleProcessAndRawVo.SampleProcessAndRawVoBuilder;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleProcessDescVo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleProcessVo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleRawVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class ProcessOrderConverterImpl implements ProcessOrderConverter {

    @Override
    public ProcessOrderPo convert(ProcessOrderCreateDto dto) {
        if ( dto == null ) {
            return null;
        }

        ProcessOrderPo processOrderPo = new ProcessOrderPo();

        processOrderPo.setProcessOrderType( dto.getProcessOrderType() );
        processOrderPo.setPlatform( dto.getPlatform() );
        processOrderPo.setOrderNo( dto.getOrderNo() );
        processOrderPo.setCustomerName( dto.getCustomerName() );
        processOrderPo.setProcessOrderNote( dto.getProcessOrderNote() );
        processOrderPo.setDeliveryNote( dto.getDeliveryNote() );
        processOrderPo.setWarehouseCode( dto.getWarehouseCode() );
        processOrderPo.setWarehouseName( dto.getWarehouseName() );
        processOrderPo.setDeliveryWarehouseCode( dto.getDeliveryWarehouseCode() );
        processOrderPo.setDeliveryWarehouseName( dto.getDeliveryWarehouseName() );
        processOrderPo.setSpu( dto.getSpu() );
        processOrderPo.setDeliverDate( dto.getDeliverDate() );
        processOrderPo.setParentProcessOrderNo( dto.getParentProcessOrderNo() );
        processOrderPo.setProcessWaveId( dto.getProcessWaveId() );

        return processOrderPo;
    }

    @Override
    public ProcessOrderPo convert(ProcessOrderEditDto dto) {
        if ( dto == null ) {
            return null;
        }

        ProcessOrderPo processOrderPo = new ProcessOrderPo();

        processOrderPo.setVersion( dto.getVersion() );
        processOrderPo.setProcessOrderId( dto.getProcessOrderId() );
        processOrderPo.setProcessOrderType( dto.getProcessOrderType() );
        processOrderPo.setPlatform( dto.getPlatform() );
        processOrderPo.setOrderNo( dto.getOrderNo() );
        processOrderPo.setCustomerName( dto.getCustomerName() );
        processOrderPo.setProcessOrderNote( dto.getProcessOrderNote() );
        processOrderPo.setDeliveryNote( dto.getDeliveryNote() );
        processOrderPo.setWarehouseCode( dto.getWarehouseCode() );
        processOrderPo.setWarehouseName( dto.getWarehouseName() );
        processOrderPo.setDeliveryWarehouseCode( dto.getDeliveryWarehouseCode() );
        processOrderPo.setDeliveryWarehouseName( dto.getDeliveryWarehouseName() );
        processOrderPo.setSpu( dto.getSpu() );
        processOrderPo.setDeliverDate( dto.getDeliverDate() );

        return processOrderPo;
    }

    @Override
    public ProcessOrderDetailVo convert(ProcessOrderPo processOrderPo) {
        if ( processOrderPo == null ) {
            return null;
        }

        ProcessOrderDetailVo processOrderDetailVo = new ProcessOrderDetailVo();

        processOrderDetailVo.setProcessOrderId( processOrderPo.getProcessOrderId() );
        processOrderDetailVo.setProcessOrderNo( processOrderPo.getProcessOrderNo() );
        processOrderDetailVo.setWarehouseCode( processOrderPo.getWarehouseCode() );
        processOrderDetailVo.setWarehouseName( processOrderPo.getWarehouseName() );
        processOrderDetailVo.setDeliveryWarehouseCode( processOrderPo.getDeliveryWarehouseCode() );
        processOrderDetailVo.setDeliveryWarehouseName( processOrderPo.getDeliveryWarehouseName() );
        processOrderDetailVo.setOrderNo( processOrderPo.getOrderNo() );
        processOrderDetailVo.setCustomerName( processOrderPo.getCustomerName() );
        processOrderDetailVo.setProcessOrderStatus( processOrderPo.getProcessOrderStatus() );
        processOrderDetailVo.setProcessOrderType( processOrderPo.getProcessOrderType() );
        processOrderDetailVo.setSpu( processOrderPo.getSpu() );
        processOrderDetailVo.setProcessOrderNote( processOrderPo.getProcessOrderNote() );
        processOrderDetailVo.setDeliveryNote( processOrderPo.getDeliveryNote() );
        processOrderDetailVo.setCurrentProcessLabel( processOrderPo.getCurrentProcessLabel() );
        processOrderDetailVo.setMaterialBackStatus( processOrderPo.getMaterialBackStatus() );
        processOrderDetailVo.setPlatform( processOrderPo.getPlatform() );
        processOrderDetailVo.setTotalProcessNum( processOrderPo.getTotalProcessNum() );
        processOrderDetailVo.setTotalSkuNum( processOrderPo.getTotalSkuNum() );
        processOrderDetailVo.setTotalSettlePrice( processOrderPo.getTotalSettlePrice() );
        processOrderDetailVo.setCreateUsername( processOrderPo.getCreateUsername() );
        processOrderDetailVo.setCreateTime( processOrderPo.getCreateTime() );
        processOrderDetailVo.setProducedTime( processOrderPo.getProducedTime() );
        processOrderDetailVo.setCheckedTime( processOrderPo.getCheckedTime() );
        processOrderDetailVo.setReceiptTime( processOrderPo.getReceiptTime() );
        processOrderDetailVo.setStoredTime( processOrderPo.getStoredTime() );
        processOrderDetailVo.setDeliverDate( processOrderPo.getDeliverDate() );
        processOrderDetailVo.setVersion( processOrderPo.getVersion() );
        processOrderDetailVo.setAvailableProductNum( processOrderPo.getAvailableProductNum() );
        processOrderDetailVo.setParentProcessOrderNo( processOrderPo.getParentProcessOrderNo() );

        return processOrderDetailVo;
    }

    @Override
    public ProcessOrderItemVo convert(ProcessOrderItemPo processOrderItemPo) {
        if ( processOrderItemPo == null ) {
            return null;
        }

        ProcessOrderItemVo processOrderItemVo = new ProcessOrderItemVo();

        processOrderItemVo.setProcessOrderItemId( processOrderItemPo.getProcessOrderItemId() );
        processOrderItemVo.setSku( processOrderItemPo.getSku() );
        processOrderItemVo.setSkuBatchCode( processOrderItemPo.getSkuBatchCode() );
        processOrderItemVo.setVariantProperties( processOrderItemPo.getVariantProperties() );
        processOrderItemVo.setProcessNum( processOrderItemPo.getProcessNum() );
        processOrderItemVo.setPurchasePrice( processOrderItemPo.getPurchasePrice() );
        processOrderItemVo.setQualityGoodsCnt( processOrderItemPo.getQualityGoodsCnt() );
        processOrderItemVo.setDefectiveGoodsCnt( processOrderItemPo.getDefectiveGoodsCnt() );
        processOrderItemVo.setIsFirst( processOrderItemPo.getIsFirst() );
        processOrderItemVo.setVersion( processOrderItemPo.getVersion() );

        return processOrderItemVo;
    }

    @Override
    public ProcessOrderMaterialVo convert(ProcessOrderMaterialPo processOrderMaterialPo) {
        if ( processOrderMaterialPo == null ) {
            return null;
        }

        ProcessOrderMaterialVo processOrderMaterialVo = new ProcessOrderMaterialVo();

        processOrderMaterialVo.setProcessOrderMaterialId( processOrderMaterialPo.getProcessOrderMaterialId() );
        processOrderMaterialVo.setSku( processOrderMaterialPo.getSku() );
        processOrderMaterialVo.setSkuBatchCode( processOrderMaterialPo.getSkuBatchCode() );
        processOrderMaterialVo.setDeliveryNum( processOrderMaterialPo.getDeliveryNum() );
        processOrderMaterialVo.setBackNum( processOrderMaterialPo.getBackNum() );
        processOrderMaterialVo.setVersion( processOrderMaterialPo.getVersion() );

        return processOrderMaterialVo;
    }

    @Override
    public ProcessOrderDescVo convert(ProcessOrderDescPo processOrderDescPo) {
        if ( processOrderDescPo == null ) {
            return null;
        }

        ProcessOrderDescVo processOrderDescVo = new ProcessOrderDescVo();

        processOrderDescVo.setProcessOrderDescId( processOrderDescPo.getProcessOrderDescId() );
        processOrderDescVo.setProcessDescName( processOrderDescPo.getProcessDescName() );
        processOrderDescVo.setProcessDescValue( processOrderDescPo.getProcessDescValue() );
        processOrderDescVo.setVersion( processOrderDescPo.getVersion() );

        return processOrderDescVo;
    }

    @Override
    public ProcessOrderProcedureVo convert(ProcessOrderProcedurePo processOrderProcedurePo) {
        if ( processOrderProcedurePo == null ) {
            return null;
        }

        ProcessOrderProcedureVo processOrderProcedureVo = new ProcessOrderProcedureVo();

        processOrderProcedureVo.setProcessOrderProcedureId( processOrderProcedurePo.getProcessOrderProcedureId() );
        processOrderProcedureVo.setSort( processOrderProcedurePo.getSort() );
        processOrderProcedureVo.setProcessId( processOrderProcedurePo.getProcessId() );
        processOrderProcedureVo.setProcessCode( processOrderProcedurePo.getProcessCode() );
        processOrderProcedureVo.setProcessLabel( processOrderProcedurePo.getProcessLabel() );
        processOrderProcedureVo.setProcessName( processOrderProcedurePo.getProcessName() );
        processOrderProcedureVo.setCommission( processOrderProcedurePo.getCommission() );
        processOrderProcedureVo.setVersion( processOrderProcedurePo.getVersion() );

        return processOrderProcedureVo;
    }

    @Override
    public ProcessOrderPrintByWmsVo convert(ProcessOrderPrintVo processOrderPrintVo) {
        if ( processOrderPrintVo == null ) {
            return null;
        }

        ProcessOrderPrintByWmsVo processOrderPrintByWmsVo = new ProcessOrderPrintByWmsVo();

        processOrderPrintByWmsVo.setProcessOrderId( processOrderPrintVo.getProcessOrderId() );
        processOrderPrintByWmsVo.setProcessOrderNo( processOrderPrintVo.getProcessOrderNo() );
        processOrderPrintByWmsVo.setWarehouseCode( processOrderPrintVo.getWarehouseCode() );
        processOrderPrintByWmsVo.setWarehouseName( processOrderPrintVo.getWarehouseName() );
        processOrderPrintByWmsVo.setDeliveryWarehouseCode( processOrderPrintVo.getDeliveryWarehouseCode() );
        processOrderPrintByWmsVo.setDeliveryWarehouseName( processOrderPrintVo.getDeliveryWarehouseName() );
        processOrderPrintByWmsVo.setDeliveryNo( processOrderPrintVo.getDeliveryNo() );
        processOrderPrintByWmsVo.setProcessOrderType( processOrderPrintVo.getProcessOrderType() );
        processOrderPrintByWmsVo.setDeliverDate( processOrderPrintVo.getDeliverDate() );
        processOrderPrintByWmsVo.setProcessOrderStatus( processOrderPrintVo.getProcessOrderStatus() );
        processOrderPrintByWmsVo.setProcessOrderNote( processOrderPrintVo.getProcessOrderNote() );
        List<String> list = processOrderPrintVo.getFileCodeList();
        if ( list != null ) {
            processOrderPrintByWmsVo.setFileCodeList( new ArrayList<String>( list ) );
        }
        processOrderPrintByWmsVo.setCreateUsername( processOrderPrintVo.getCreateUsername() );
        processOrderPrintByWmsVo.setCreateTime( processOrderPrintVo.getCreateTime() );
        processOrderPrintByWmsVo.setPrintTime( processOrderPrintVo.getPrintTime() );
        processOrderPrintByWmsVo.setPickOrderNo( processOrderPrintVo.getPickOrderNo() );
        processOrderPrintByWmsVo.setRawWarehouseCode( processOrderPrintVo.getRawWarehouseCode() );
        processOrderPrintByWmsVo.setRawWarehouseName( processOrderPrintVo.getRawWarehouseName() );
        List<ProcessOrderItemPrintVo> list1 = processOrderPrintVo.getProcessOrderItems();
        if ( list1 != null ) {
            processOrderPrintByWmsVo.setProcessOrderItems( new ArrayList<ProcessOrderItemPrintVo>( list1 ) );
        }
        List<ProcessOrderMaterialPrintVo> list2 = processOrderPrintVo.getProcessOrderMaterials();
        if ( list2 != null ) {
            processOrderPrintByWmsVo.setProcessOrderMaterials( new ArrayList<ProcessOrderMaterialPrintVo>( list2 ) );
        }
        List<ProcessOrderProcedureVo> list3 = processOrderPrintVo.getProcessOrderProcedures();
        if ( list3 != null ) {
            processOrderPrintByWmsVo.setProcessOrderProcedures( new ArrayList<ProcessOrderProcedureVo>( list3 ) );
        }
        List<ProcessOrderDescVo> list4 = processOrderPrintVo.getProcessOrderDescs();
        if ( list4 != null ) {
            processOrderPrintByWmsVo.setProcessOrderDescs( new ArrayList<ProcessOrderDescVo>( list4 ) );
        }
        List<ProcessOrderPrintDeliveryOrderVo> list5 = processOrderPrintVo.getProcessOrderPrintDeliveryOrderList();
        if ( list5 != null ) {
            processOrderPrintByWmsVo.setProcessOrderPrintDeliveryOrderList( new ArrayList<ProcessOrderPrintDeliveryOrderVo>( list5 ) );
        }
        processOrderPrintByWmsVo.setVersion( processOrderPrintVo.getVersion() );
        List<String> list6 = processOrderPrintVo.getPickingCartStackCodeList();
        if ( list6 != null ) {
            processOrderPrintByWmsVo.setPickingCartStackCodeList( new ArrayList<String>( list6 ) );
        }

        return processOrderPrintByWmsVo;
    }

    @Override
    public SampleProcessAndRawVo convert(SampleProcessAndRawBo sampleProcessAndRawBo) {
        if ( sampleProcessAndRawBo == null ) {
            return null;
        }

        SampleProcessAndRawVoBuilder sampleProcessAndRawVo = SampleProcessAndRawVo.builder();

        sampleProcessAndRawVo.sampleParentOrderNo( sampleProcessAndRawBo.getSampleParentOrderNo() );
        sampleProcessAndRawVo.sampleChildOrderNo( sampleProcessAndRawBo.getSampleChildOrderNo() );
        sampleProcessAndRawVo.sampleProduceLabel( sampleProcessAndRawBo.getSampleProduceLabel() );
        sampleProcessAndRawVo.sampleTime( sampleProcessAndRawBo.getSampleTime() );
        sampleProcessAndRawVo.inventory( sampleProcessAndRawBo.getInventory() );
        List<String> list = sampleProcessAndRawBo.getFileCodeList();
        if ( list != null ) {
            sampleProcessAndRawVo.fileCodeList( new ArrayList<String>( list ) );
        }
        List<SampleRawVo> list1 = sampleProcessAndRawBo.getSampleRawBoList();
        if ( list1 != null ) {
            sampleProcessAndRawVo.sampleRawBoList( new ArrayList<SampleRawVo>( list1 ) );
        }
        List<SampleProcessDescVo> list2 = sampleProcessAndRawBo.getSampleProcessDescBoList();
        if ( list2 != null ) {
            sampleProcessAndRawVo.sampleProcessDescBoList( new ArrayList<SampleProcessDescVo>( list2 ) );
        }
        List<SampleProcessVo> list3 = sampleProcessAndRawBo.getSampleProcessBoList();
        if ( list3 != null ) {
            sampleProcessAndRawVo.sampleProcessBoList( new ArrayList<SampleProcessVo>( list3 ) );
        }
        List<SampleChildOrderInfoVo> list4 = sampleProcessAndRawBo.getSampleChildOrderInfoList();
        if ( list4 != null ) {
            sampleProcessAndRawVo.sampleChildOrderInfoList( new ArrayList<SampleChildOrderInfoVo>( list4 ) );
        }

        return sampleProcessAndRawVo.build();
    }
}
