package com.hete.supply.scm.server.scm.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

/**
 * @author yanjiawei
 * @date 2023年08月18日 10:56
 */
@Data
@AllArgsConstructor
public class WorkDurationBo {
    private LocalDate date;
    private Duration duration;
}
