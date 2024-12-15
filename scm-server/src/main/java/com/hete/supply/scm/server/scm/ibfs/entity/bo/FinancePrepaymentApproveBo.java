package com.hete.supply.scm.server.scm.ibfs.entity.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/5/15 17:05
 */
@Data
@NoArgsConstructor
public class FinancePrepaymentApproveBo {

    private String recipientSubject;
    private String supplierCode;
    private String prepaymentMoney;
    private String prepaymentType;
    private String applyDate;
    private String dept;
    private String reason;
    private List<PrepaymentDetailApproveBo> prepaymentDetailApproveList;
    private List<String> fileCodeList;

}
