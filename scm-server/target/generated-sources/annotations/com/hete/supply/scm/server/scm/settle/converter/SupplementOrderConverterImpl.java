package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.SupplementOrderAddDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.SupplementOrderEditDto;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderBillPo;
import com.hete.supply.scm.server.scm.settle.entity.po.SupplementOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SettleSupplementOrderVo.SupplementOrderDetail;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderDetailVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class SupplementOrderConverterImpl implements SupplementOrderConverter {

    @Override
    public SupplementOrderDetailVo convert(SupplementOrderPo po) {
        if ( po == null ) {
            return null;
        }

        SupplementOrderDetailVo supplementOrderDetailVo = new SupplementOrderDetailVo();

        supplementOrderDetailVo.setSupplementOrderId( po.getSupplementOrderId() );
        supplementOrderDetailVo.setVersion( po.getVersion() );
        supplementOrderDetailVo.setSupplementOrderNo( po.getSupplementOrderNo() );
        supplementOrderDetailVo.setSupplementStatus( po.getSupplementStatus() );
        supplementOrderDetailVo.setSupplementType( po.getSupplementType() );
        supplementOrderDetailVo.setSupplierName( po.getSupplierName() );
        supplementOrderDetailVo.setSupplierCode( po.getSupplierCode() );
        supplementOrderDetailVo.setSupplementUser( po.getSupplementUser() );
        supplementOrderDetailVo.setSupplementUsername( po.getSupplementUsername() );
        supplementOrderDetailVo.setSupplementPrice( po.getSupplementPrice() );
        supplementOrderDetailVo.setSettleOrderNo( po.getSettleOrderNo() );
        supplementOrderDetailVo.setAboutSettleTime( po.getAboutSettleTime() );
        supplementOrderDetailVo.setConfirmRefuseRemarks( po.getConfirmRefuseRemarks() );
        supplementOrderDetailVo.setExamineRefuseRemarks( po.getExamineRefuseRemarks() );
        supplementOrderDetailVo.setCreateTime( po.getCreateTime() );
        supplementOrderDetailVo.setCreateUsername( po.getCreateUsername() );
        supplementOrderDetailVo.setSubmitTime( po.getSubmitTime() );
        supplementOrderDetailVo.setSubmitUser( po.getSubmitUser() );
        supplementOrderDetailVo.setSubmitUsername( po.getSubmitUsername() );
        supplementOrderDetailVo.setConfirmTime( po.getConfirmTime() );
        supplementOrderDetailVo.setConfirmUser( po.getConfirmUser() );
        supplementOrderDetailVo.setConfirmUsername( po.getConfirmUsername() );
        supplementOrderDetailVo.setExamineTime( po.getExamineTime() );
        supplementOrderDetailVo.setExamineUser( po.getExamineUser() );
        supplementOrderDetailVo.setExamineUsername( po.getExamineUsername() );
        supplementOrderDetailVo.setPriceRefuseRemarks( po.getPriceRefuseRemarks() );
        supplementOrderDetailVo.setPriceConfirmUser( po.getPriceConfirmUser() );
        supplementOrderDetailVo.setPriceConfirmUsername( po.getPriceConfirmUsername() );
        supplementOrderDetailVo.setPriceConfirmTime( po.getPriceConfirmTime() );
        supplementOrderDetailVo.setHandleUser( po.getHandleUser() );
        supplementOrderDetailVo.setHandleUsername( po.getHandleUsername() );

        return supplementOrderDetailVo;
    }

    @Override
    public SupplementOrderPo create(SupplementOrderAddDto dto) {
        if ( dto == null ) {
            return null;
        }

        SupplementOrderPo supplementOrderPo = new SupplementOrderPo();

        supplementOrderPo.setSupplementType( dto.getSupplementType() );
        supplementOrderPo.setSupplierCode( dto.getSupplierCode() );
        supplementOrderPo.setSupplierName( dto.getSupplierName() );
        supplementOrderPo.setSupplementUser( dto.getSupplementUser() );
        supplementOrderPo.setSupplementUsername( dto.getSupplementUsername() );
        supplementOrderPo.setAboutSettleTime( dto.getAboutSettleTime() );

        return supplementOrderPo;
    }

    @Override
    public SupplementOrderPo edit(SupplementOrderEditDto dto) {
        if ( dto == null ) {
            return null;
        }

        SupplementOrderPo supplementOrderPo = new SupplementOrderPo();

        supplementOrderPo.setVersion( dto.getVersion() );
        supplementOrderPo.setSupplementOrderId( dto.getSupplementOrderId() );
        supplementOrderPo.setSupplementType( dto.getSupplementType() );
        supplementOrderPo.setSupplierCode( dto.getSupplierCode() );
        supplementOrderPo.setSupplementUser( dto.getSupplementUser() );
        supplementOrderPo.setSupplementUsername( dto.getSupplementUsername() );
        supplementOrderPo.setAboutSettleTime( dto.getAboutSettleTime() );

        return supplementOrderPo;
    }

    @Override
    public List<SupplementOrderDetail> supplementOrderBillList(List<ProcessSettleOrderBillPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<SupplementOrderDetail> list = new ArrayList<SupplementOrderDetail>( poList.size() );
        for ( ProcessSettleOrderBillPo processSettleOrderBillPo : poList ) {
            list.add( processSettleOrderBillPoToSupplementOrderDetail( processSettleOrderBillPo ) );
        }

        return list;
    }

    protected SupplementOrderDetail processSettleOrderBillPoToSupplementOrderDetail(ProcessSettleOrderBillPo processSettleOrderBillPo) {
        if ( processSettleOrderBillPo == null ) {
            return null;
        }

        SupplementOrderDetail supplementOrderDetail = new SupplementOrderDetail();

        supplementOrderDetail.setBusinessNo( processSettleOrderBillPo.getBusinessNo() );
        supplementOrderDetail.setSupplementDeductType( processSettleOrderBillPo.getSupplementDeductType() );
        supplementOrderDetail.setExamineTime( processSettleOrderBillPo.getExamineTime() );
        supplementOrderDetail.setPrice( processSettleOrderBillPo.getPrice() );
        supplementOrderDetail.setStatusName( processSettleOrderBillPo.getStatusName() );

        return supplementOrderDetail;
    }
}
