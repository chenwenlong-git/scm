package com.hete.supply.scm.server.scm.converter;

import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemRawPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProduceDataItemRawListVo;
import com.hete.supply.scm.server.scm.production.entity.vo.ProduceDataItemRawInfoVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class ProduceDataItemRawConverterImpl implements ProduceDataItemRawConverter {

    @Override
    public List<ProduceDataItemRawListVo> convert(List<ProduceDataItemRawPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<ProduceDataItemRawListVo> list = new ArrayList<ProduceDataItemRawListVo>( poList.size() );
        for ( ProduceDataItemRawPo produceDataItemRawPo : poList ) {
            list.add( produceDataItemRawPoToProduceDataItemRawListVo( produceDataItemRawPo ) );
        }

        return list;
    }

    @Override
    public List<ProduceDataItemRawInfoVo> convertInfoVo(List<ProduceDataItemRawPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<ProduceDataItemRawInfoVo> list = new ArrayList<ProduceDataItemRawInfoVo>( poList.size() );
        for ( ProduceDataItemRawPo produceDataItemRawPo : poList ) {
            list.add( produceDataItemRawPoToProduceDataItemRawInfoVo( produceDataItemRawPo ) );
        }

        return list;
    }

    protected ProduceDataItemRawListVo produceDataItemRawPoToProduceDataItemRawListVo(ProduceDataItemRawPo produceDataItemRawPo) {
        if ( produceDataItemRawPo == null ) {
            return null;
        }

        ProduceDataItemRawListVo produceDataItemRawListVo = new ProduceDataItemRawListVo();

        produceDataItemRawListVo.setMaterialType( produceDataItemRawPo.getMaterialType() );
        produceDataItemRawListVo.setSku( produceDataItemRawPo.getSku() );
        produceDataItemRawListVo.setSkuCnt( produceDataItemRawPo.getSkuCnt() );

        return produceDataItemRawListVo;
    }

    protected ProduceDataItemRawInfoVo produceDataItemRawPoToProduceDataItemRawInfoVo(ProduceDataItemRawPo produceDataItemRawPo) {
        if ( produceDataItemRawPo == null ) {
            return null;
        }

        ProduceDataItemRawInfoVo produceDataItemRawInfoVo = new ProduceDataItemRawInfoVo();

        produceDataItemRawInfoVo.setProduceDataItemRawId( produceDataItemRawPo.getProduceDataItemRawId() );
        produceDataItemRawInfoVo.setMaterialType( produceDataItemRawPo.getMaterialType() );
        produceDataItemRawInfoVo.setSku( produceDataItemRawPo.getSku() );
        produceDataItemRawInfoVo.setSkuCnt( produceDataItemRawPo.getSkuCnt() );

        return produceDataItemRawInfoVo;
    }
}
