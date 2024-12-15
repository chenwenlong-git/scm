package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.ProcessSettleOrderDetailVo;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class ProcessSettleOrderConverterImpl implements ProcessSettleOrderConverter {

    @Override
    public ProcessSettleOrderDetailVo convert(ProcessSettleOrderPo po) {
        if ( po == null ) {
            return null;
        }

        ProcessSettleOrderDetailVo processSettleOrderDetailVo = new ProcessSettleOrderDetailVo();

        processSettleOrderDetailVo.setProcessSettleOrderId( po.getProcessSettleOrderId() );
        processSettleOrderDetailVo.setVersion( po.getVersion() );
        processSettleOrderDetailVo.setProcessSettleOrderNo( po.getProcessSettleOrderNo() );
        processSettleOrderDetailVo.setCreateUsername( po.getCreateUsername() );
        processSettleOrderDetailVo.setCreateTime( po.getCreateTime() );
        processSettleOrderDetailVo.setProcessSettleStatus( po.getProcessSettleStatus() );
        processSettleOrderDetailVo.setExamineUser( po.getExamineUser() );
        processSettleOrderDetailVo.setExamineUsername( po.getExamineUsername() );
        processSettleOrderDetailVo.setExamineTime( po.getExamineTime() );
        processSettleOrderDetailVo.setSettleUser( po.getSettleUser() );
        processSettleOrderDetailVo.setSettleUsername( po.getSettleUsername() );
        processSettleOrderDetailVo.setSettleTime( po.getSettleTime() );
        processSettleOrderDetailVo.setExamineRefuseRemarks( po.getExamineRefuseRemarks() );

        return processSettleOrderDetailVo;
    }
}
