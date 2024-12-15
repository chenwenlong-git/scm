package com.hete.supply.scm.server.scm.qc.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.QcOrigin;
import com.hete.supply.scm.api.scm.entity.enums.QcOriginProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/3/6.
 */
@Data
public class QcOriginBo {
    private QcOrigin qcOrigin;
    private QcOriginProperty qcOriginProperty;
}
