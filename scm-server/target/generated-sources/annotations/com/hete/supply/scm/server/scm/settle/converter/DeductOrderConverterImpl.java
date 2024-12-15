package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderAddDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderEditDto;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderBillPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderDetailVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SettleDeductOrderVo.DeductOrderDetail;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:57+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class DeductOrderConverterImpl implements DeductOrderConverter {

    @Override
    public DeductOrderDetailVo convert(DeductOrderPo po) {
        if ( po == null ) {
            return null;
        }

        DeductOrderDetailVo deductOrderDetailVo = new DeductOrderDetailVo();

        deductOrderDetailVo.setDeductOrderId( po.getDeductOrderId() );
        deductOrderDetailVo.setVersion( po.getVersion() );
        deductOrderDetailVo.setDeductOrderNo( po.getDeductOrderNo() );
        deductOrderDetailVo.setDeductStatus( po.getDeductStatus() );
        deductOrderDetailVo.setDeductType( po.getDeductType() );
        deductOrderDetailVo.setSupplierCode( po.getSupplierCode() );
        deductOrderDetailVo.setSupplierName( po.getSupplierName() );
        deductOrderDetailVo.setDeductUser( po.getDeductUser() );
        deductOrderDetailVo.setDeductUsername( po.getDeductUsername() );
        deductOrderDetailVo.setDeductPrice( po.getDeductPrice() );
        deductOrderDetailVo.setSettleOrderNo( po.getSettleOrderNo() );
        deductOrderDetailVo.setAboutSettleTime( po.getAboutSettleTime() );
        deductOrderDetailVo.setConfirmRefuseRemarks( po.getConfirmRefuseRemarks() );
        deductOrderDetailVo.setExamineRefuseRemarks( po.getExamineRefuseRemarks() );
        deductOrderDetailVo.setCreateTime( po.getCreateTime() );
        deductOrderDetailVo.setCreateUsername( po.getCreateUsername() );
        deductOrderDetailVo.setSubmitTime( po.getSubmitTime() );
        deductOrderDetailVo.setSubmitUser( po.getSubmitUser() );
        deductOrderDetailVo.setSubmitUsername( po.getSubmitUsername() );
        deductOrderDetailVo.setConfirmTime( po.getConfirmTime() );
        deductOrderDetailVo.setConfirmUser( po.getConfirmUser() );
        deductOrderDetailVo.setConfirmUsername( po.getConfirmUsername() );
        deductOrderDetailVo.setExamineTime( po.getExamineTime() );
        deductOrderDetailVo.setExamineUser( po.getExamineUser() );
        deductOrderDetailVo.setExamineUsername( po.getExamineUsername() );
        deductOrderDetailVo.setPriceRefuseRemarks( po.getPriceRefuseRemarks() );
        deductOrderDetailVo.setPriceConfirmUser( po.getPriceConfirmUser() );
        deductOrderDetailVo.setPriceConfirmUsername( po.getPriceConfirmUsername() );
        deductOrderDetailVo.setPriceConfirmTime( po.getPriceConfirmTime() );
        deductOrderDetailVo.setHandleUser( po.getHandleUser() );
        deductOrderDetailVo.setHandleUsername( po.getHandleUsername() );

        return deductOrderDetailVo;
    }

    @Override
    public List<DeductOrderDetailVo> poToListDetail(List<DeductOrderPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<DeductOrderDetailVo> list = new ArrayList<DeductOrderDetailVo>( poList.size() );
        for ( DeductOrderPo deductOrderPo : poList ) {
            list.add( convert( deductOrderPo ) );
        }

        return list;
    }

    @Override
    public DeductOrderPo create(DeductOrderAddDto dto) {
        if ( dto == null ) {
            return null;
        }

        DeductOrderPo deductOrderPo = new DeductOrderPo();

        deductOrderPo.setDeductType( dto.getDeductType() );
        deductOrderPo.setSupplierCode( dto.getSupplierCode() );
        deductOrderPo.setDeductUser( dto.getDeductUser() );
        deductOrderPo.setDeductUsername( dto.getDeductUsername() );
        deductOrderPo.setAboutSettleTime( dto.getAboutSettleTime() );

        return deductOrderPo;
    }

    @Override
    public DeductOrderPo edit(DeductOrderEditDto dto) {
        if ( dto == null ) {
            return null;
        }

        DeductOrderPo deductOrderPo = new DeductOrderPo();

        deductOrderPo.setVersion( dto.getVersion() );
        deductOrderPo.setDeductOrderId( dto.getDeductOrderId() );
        deductOrderPo.setDeductType( dto.getDeductType() );
        deductOrderPo.setSupplierCode( dto.getSupplierCode() );
        deductOrderPo.setDeductUser( dto.getDeductUser() );
        deductOrderPo.setDeductUsername( dto.getDeductUsername() );
        deductOrderPo.setAboutSettleTime( dto.getAboutSettleTime() );

        return deductOrderPo;
    }

    @Override
    public List<DeductOrderDetail> deductOrderBillList(List<ProcessSettleOrderBillPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<DeductOrderDetail> list = new ArrayList<DeductOrderDetail>( poList.size() );
        for ( ProcessSettleOrderBillPo processSettleOrderBillPo : poList ) {
            list.add( processSettleOrderBillPoToDeductOrderDetail( processSettleOrderBillPo ) );
        }

        return list;
    }

    protected DeductOrderDetail processSettleOrderBillPoToDeductOrderDetail(ProcessSettleOrderBillPo processSettleOrderBillPo) {
        if ( processSettleOrderBillPo == null ) {
            return null;
        }

        DeductOrderDetail deductOrderDetail = new DeductOrderDetail();

        deductOrderDetail.setBusinessNo( processSettleOrderBillPo.getBusinessNo() );
        deductOrderDetail.setSupplementDeductType( processSettleOrderBillPo.getSupplementDeductType() );
        deductOrderDetail.setExamineTime( processSettleOrderBillPo.getExamineTime() );
        deductOrderDetail.setPrice( processSettleOrderBillPo.getPrice() );
        deductOrderDetail.setStatusName( processSettleOrderBillPo.getStatusName() );

        return deductOrderDetail;
    }
}
