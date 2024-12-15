package com.hete.supply.scm.server.scm.process.entity.dto;

import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/6/20.
 */
@Data
public class ProcPdReportDto {

    private double timestamp;
    private String platForm;
    private int processNum;
}
