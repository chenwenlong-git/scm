package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderDefectiveDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderDefectiveEditDto;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderDefectivePo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderDetailVo.DeductOrderDefectiveVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class DeductOrderDefectiveConverterImpl implements DeductOrderDefectiveConverter {

    @Override
    public List<DeductOrderDefectivePo> convert(List<DeductOrderDefectiveDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<DeductOrderDefectivePo> list = new ArrayList<DeductOrderDefectivePo>( dto.size() );
        for ( DeductOrderDefectiveDto deductOrderDefectiveDto : dto ) {
            list.add( deductOrderDefectiveDtoToDeductOrderDefectivePo( deductOrderDefectiveDto ) );
        }

        return list;
    }

    @Override
    public List<DeductOrderDefectivePo> edit(List<DeductOrderDefectiveEditDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<DeductOrderDefectivePo> list = new ArrayList<DeductOrderDefectivePo>( dto.size() );
        for ( DeductOrderDefectiveEditDto deductOrderDefectiveEditDto : dto ) {
            list.add( deductOrderDefectiveEditDtoToDeductOrderDefectivePo( deductOrderDefectiveEditDto ) );
        }

        return list;
    }

    @Override
    public List<DeductOrderDefectiveVo> deductOrderDefectiveList(List<DeductOrderDefectivePo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<DeductOrderDefectiveVo> list = new ArrayList<DeductOrderDefectiveVo>( poList.size() );
        for ( DeductOrderDefectivePo deductOrderDefectivePo : poList ) {
            list.add( deductOrderDefectivePoToDeductOrderDefectiveVo( deductOrderDefectivePo ) );
        }

        return list;
    }

    protected DeductOrderDefectivePo deductOrderDefectiveDtoToDeductOrderDefectivePo(DeductOrderDefectiveDto deductOrderDefectiveDto) {
        if ( deductOrderDefectiveDto == null ) {
            return null;
        }

        DeductOrderDefectivePo deductOrderDefectivePo = new DeductOrderDefectivePo();

        deductOrderDefectivePo.setBusinessNo( deductOrderDefectiveDto.getBusinessNo() );
        deductOrderDefectivePo.setSku( deductOrderDefectiveDto.getSku() );
        deductOrderDefectivePo.setSkuBatchCode( deductOrderDefectiveDto.getSkuBatchCode() );
        deductOrderDefectivePo.setDeductNum( deductOrderDefectiveDto.getDeductNum() );
        deductOrderDefectivePo.setSettlePrice( deductOrderDefectiveDto.getSettlePrice() );
        deductOrderDefectivePo.setDeductUnitPrice( deductOrderDefectiveDto.getDeductUnitPrice() );
        deductOrderDefectivePo.setDeductPrice( deductOrderDefectiveDto.getDeductPrice() );
        deductOrderDefectivePo.setDeductRemarks( deductOrderDefectiveDto.getDeductRemarks() );

        return deductOrderDefectivePo;
    }

    protected DeductOrderDefectivePo deductOrderDefectiveEditDtoToDeductOrderDefectivePo(DeductOrderDefectiveEditDto deductOrderDefectiveEditDto) {
        if ( deductOrderDefectiveEditDto == null ) {
            return null;
        }

        DeductOrderDefectivePo deductOrderDefectivePo = new DeductOrderDefectivePo();

        deductOrderDefectivePo.setVersion( deductOrderDefectiveEditDto.getVersion() );
        deductOrderDefectivePo.setDeductOrderDefectiveId( deductOrderDefectiveEditDto.getDeductOrderDefectiveId() );
        deductOrderDefectivePo.setDeductNum( deductOrderDefectiveEditDto.getDeductNum() );
        deductOrderDefectivePo.setDeductUnitPrice( deductOrderDefectiveEditDto.getDeductUnitPrice() );
        deductOrderDefectivePo.setDeductPrice( deductOrderDefectiveEditDto.getDeductPrice() );

        return deductOrderDefectivePo;
    }

    protected DeductOrderDefectiveVo deductOrderDefectivePoToDeductOrderDefectiveVo(DeductOrderDefectivePo deductOrderDefectivePo) {
        if ( deductOrderDefectivePo == null ) {
            return null;
        }

        DeductOrderDefectiveVo deductOrderDefectiveVo = new DeductOrderDefectiveVo();

        deductOrderDefectiveVo.setDeductOrderDefectiveId( deductOrderDefectivePo.getDeductOrderDefectiveId() );
        deductOrderDefectiveVo.setVersion( deductOrderDefectivePo.getVersion() );
        deductOrderDefectiveVo.setBusinessNo( deductOrderDefectivePo.getBusinessNo() );
        deductOrderDefectiveVo.setSku( deductOrderDefectivePo.getSku() );
        deductOrderDefectiveVo.setSkuBatchCode( deductOrderDefectivePo.getSkuBatchCode() );
        deductOrderDefectiveVo.setDeductNum( deductOrderDefectivePo.getDeductNum() );
        deductOrderDefectiveVo.setSettlePrice( deductOrderDefectivePo.getSettlePrice() );
        deductOrderDefectiveVo.setDeductUnitPrice( deductOrderDefectivePo.getDeductUnitPrice() );
        deductOrderDefectiveVo.setDeductPrice( deductOrderDefectivePo.getDeductPrice() );
        deductOrderDefectiveVo.setDeductRemarks( deductOrderDefectivePo.getDeductRemarks() );

        return deductOrderDefectiveVo;
    }
}
