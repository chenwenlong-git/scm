package com.hete.supply.scm.server.supplier.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/17 16:55
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public enum SupplierPassType implements IRemark {
    // 供应商操作
    PASS("跳过"),
    BACK("回退"),
    ;

    private String remark;

}
