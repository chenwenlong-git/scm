package com.hete.supply.scm.server.scm.develop.converter;

import com.hete.supply.scm.server.scm.develop.entity.po.DevelopReviewSampleOrderInfoPo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopReviewSampleInfoVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class DevelopReviewSampleOrderInfoConverterImpl implements DevelopReviewSampleOrderInfoConverter {

    @Override
    public List<DevelopReviewSampleInfoVo> convert(List<DevelopReviewSampleOrderInfoPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<DevelopReviewSampleInfoVo> list = new ArrayList<DevelopReviewSampleInfoVo>( poList.size() );
        for ( DevelopReviewSampleOrderInfoPo developReviewSampleOrderInfoPo : poList ) {
            list.add( developReviewSampleOrderInfoPoToDevelopReviewSampleInfoVo( developReviewSampleOrderInfoPo ) );
        }

        return list;
    }

    protected DevelopReviewSampleInfoVo developReviewSampleOrderInfoPoToDevelopReviewSampleInfoVo(DevelopReviewSampleOrderInfoPo developReviewSampleOrderInfoPo) {
        if ( developReviewSampleOrderInfoPo == null ) {
            return null;
        }

        DevelopReviewSampleInfoVo developReviewSampleInfoVo = new DevelopReviewSampleInfoVo();

        developReviewSampleInfoVo.setDevelopSampleOrderNo( developReviewSampleOrderInfoPo.getDevelopSampleOrderNo() );
        developReviewSampleInfoVo.setAttributeNameId( developReviewSampleOrderInfoPo.getAttributeNameId() );
        developReviewSampleInfoVo.setSampleInfoKey( developReviewSampleOrderInfoPo.getSampleInfoKey() );
        developReviewSampleInfoVo.setSampleInfoValue( developReviewSampleOrderInfoPo.getSampleInfoValue() );
        developReviewSampleInfoVo.setEvaluationOpinion( developReviewSampleOrderInfoPo.getEvaluationOpinion() );

        return developReviewSampleInfoVo;
    }
}
