package com.hete.supply.scm.server.scm.feishu.client;

import com.hete.feign.config.retry.DefaultRetryerConfig;
import com.hete.supply.scm.server.scm.feishu.entity.dto.AppendDto;
import com.hete.supply.scm.server.scm.feishu.entity.dto.PrependDto;
import com.hete.supply.scm.server.scm.feishu.entity.vo.AppendVo;
import com.hete.supply.scm.server.scm.feishu.entity.vo.FeiShuResult;
import com.hete.supply.scm.server.scm.feishu.entity.vo.PrependVo;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author lihaifei
 * 飞书在线电子表格
 */
@RefreshScope
@Validated
@FeignClient(name = "feishu-api", url = "${feishu.api}", configuration = DefaultRetryerConfig.class)
public interface FeiShuExcelClient {


    /**
     * 向工作表指定范围插入数据
     */
    @PostMapping(value = "/sheets/v2/spreadsheets/{spreadsheetToken}/values_prepend", headers = "Content-Type=application/json; charset=utf-8")
    FeiShuResult<PrependVo> prepend(@RequestHeader("Authorization") String authorization,
                                    @NotBlank @PathVariable("spreadsheetToken") String spreadsheetToken,
                                    @Valid @RequestBody PrependDto dto);

    /**
     * 向工作表指定范围插入数据
     */
    @PostMapping(value = "/sheets/v2/spreadsheets/{spreadsheetToken}/values_append", headers = "Content-Type=application/json; charset=utf-8")
    FeiShuResult<AppendVo> append(@RequestHeader("Authorization") String authorization,
                                  @NotBlank @PathVariable("spreadsheetToken") String spreadsheetToken,
                                  @Valid @RequestBody AppendDto dto);

}

