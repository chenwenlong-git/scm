package com.hete.supply.scm.server.scm.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.enums.ReportBizType;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.core.springsupport.SpringContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author yanjiawei
 * Created on 2024/3/15.
 */
@Component
public class ReportServerFactory {

    private static final Map<ReportBizType, IReportServer> reportServerMap
            = new HashMap<>(16);

    /**
     * 初始化策略映射，将不同的策略和筛选策略关联起来。
     */
    @PostConstruct
    public synchronized void initReportServerMap() {
        Map<String, IReportServer> beansOfType = SpringContext.getBeansOfType(IReportServer.class);

        beansOfType.forEach((beanName, reportServer) -> {
            ReportBizType reportBizType = reportServer.getReportBizType();
            reportServerMap.put(reportBizType, reportServer);
        });

    }

    public IReportServer getReportServer(ReportBizType reportBizType) {
        if (CollectionUtils.isEmpty(reportServerMap)) {
            initReportServerMap();
        }

        IReportServer reportServer = reportServerMap.get(reportBizType);
        if (Objects.isNull(reportServer)) {
            throw new ParamIllegalException("无法通过{}报表业务类型获取报表服务，请检查！", reportBizType);
        }
        return reportServer;
    }


}
