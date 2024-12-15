package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderPurchaseDto;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderPurchasePo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderDetailVo.DeductOrderPurchaseVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class DeductOrderPurchaseConverterImpl implements DeductOrderPurchaseConverter {

    @Override
    public DeductOrderPurchaseVo convert(DeductOrderPurchasePo po) {
        if ( po == null ) {
            return null;
        }

        DeductOrderPurchaseVo deductOrderPurchaseVo = new DeductOrderPurchaseVo();

        deductOrderPurchaseVo.setDeductOrderPurchaseId( po.getDeductOrderPurchaseId() );
        deductOrderPurchaseVo.setDeductOrderPurchaseType( po.getDeductOrderPurchaseType() );
        deductOrderPurchaseVo.setBusinessNo( po.getBusinessNo() );
        deductOrderPurchaseVo.setSku( po.getSku() );
        deductOrderPurchaseVo.setSpu( po.getSpu() );
        deductOrderPurchaseVo.setSkuNum( po.getSkuNum() );
        deductOrderPurchaseVo.setDeductPrice( po.getDeductPrice() );
        deductOrderPurchaseVo.setSettlePrice( po.getSettlePrice() );
        deductOrderPurchaseVo.setDeductRemarks( po.getDeductRemarks() );
        deductOrderPurchaseVo.setSkuBatchCode( po.getSkuBatchCode() );
        deductOrderPurchaseVo.setSettleUnitPrice( po.getSettleUnitPrice() );

        return deductOrderPurchaseVo;
    }

    @Override
    public List<DeductOrderPurchaseVo> deductOrderPurchaseList(List<DeductOrderPurchasePo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<DeductOrderPurchaseVo> list = new ArrayList<DeductOrderPurchaseVo>( poList.size() );
        for ( DeductOrderPurchasePo deductOrderPurchasePo : poList ) {
            list.add( convert( deductOrderPurchasePo ) );
        }

        return list;
    }

    @Override
    public List<DeductOrderPurchasePo> create(List<DeductOrderPurchaseDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<DeductOrderPurchasePo> list = new ArrayList<DeductOrderPurchasePo>( dto.size() );
        for ( DeductOrderPurchaseDto deductOrderPurchaseDto : dto ) {
            list.add( deductOrderPurchaseDtoToDeductOrderPurchasePo( deductOrderPurchaseDto ) );
        }

        return list;
    }

    @Override
    public List<DeductOrderPurchasePo> edit(List<DeductOrderPurchaseDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<DeductOrderPurchasePo> list = new ArrayList<DeductOrderPurchasePo>( dto.size() );
        for ( DeductOrderPurchaseDto deductOrderPurchaseDto : dto ) {
            list.add( deductOrderPurchaseDtoToDeductOrderPurchasePo( deductOrderPurchaseDto ) );
        }

        return list;
    }

    protected DeductOrderPurchasePo deductOrderPurchaseDtoToDeductOrderPurchasePo(DeductOrderPurchaseDto deductOrderPurchaseDto) {
        if ( deductOrderPurchaseDto == null ) {
            return null;
        }

        DeductOrderPurchasePo deductOrderPurchasePo = new DeductOrderPurchasePo();

        deductOrderPurchasePo.setBusinessNo( deductOrderPurchaseDto.getBusinessNo() );
        deductOrderPurchasePo.setDeductOrderPurchaseType( deductOrderPurchaseDto.getDeductOrderPurchaseType() );
        deductOrderPurchasePo.setSku( deductOrderPurchaseDto.getSku() );
        deductOrderPurchasePo.setSpu( deductOrderPurchaseDto.getSpu() );
        deductOrderPurchasePo.setSkuNum( deductOrderPurchaseDto.getSkuNum() );
        deductOrderPurchasePo.setDeductPrice( deductOrderPurchaseDto.getDeductPrice() );
        deductOrderPurchasePo.setSettlePrice( deductOrderPurchaseDto.getSettlePrice() );
        deductOrderPurchasePo.setDeductRemarks( deductOrderPurchaseDto.getDeductRemarks() );
        deductOrderPurchasePo.setSkuBatchCode( deductOrderPurchaseDto.getSkuBatchCode() );
        deductOrderPurchasePo.setSettleUnitPrice( deductOrderPurchaseDto.getSettleUnitPrice() );

        return deductOrderPurchasePo;
    }
}
