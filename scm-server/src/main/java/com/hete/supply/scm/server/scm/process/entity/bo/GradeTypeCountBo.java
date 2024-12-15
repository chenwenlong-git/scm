package com.hete.supply.scm.server.scm.process.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.GradeType;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/3/14.
 */
@Data
public class GradeTypeCountBo {
    private GradeType gradeType;
    private int employeeCount;
}
