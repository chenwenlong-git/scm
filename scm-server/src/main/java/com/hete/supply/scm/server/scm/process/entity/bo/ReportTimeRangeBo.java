package com.hete.supply.scm.server.scm.process.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yanjiawei
 * Created on 2024/3/14.
 */
@Data
@AllArgsConstructor
public class ReportTimeRangeBo {
    private LocalDateTime startUtc;
    private LocalDateTime endUtc;

}
