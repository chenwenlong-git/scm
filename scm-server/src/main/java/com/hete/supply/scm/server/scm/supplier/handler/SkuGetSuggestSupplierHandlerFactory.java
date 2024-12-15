package com.hete.supply.scm.server.scm.supplier.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/8/5 17:06
 */
@Service
public class SkuGetSuggestSupplierHandlerFactory {

    private final ApplicationContext applicationContext;

    @Autowired
    public SkuGetSuggestSupplierHandlerFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 设置链式调用顺序
     *
     * @return AbstractSkuGetSuggestSupplierHandler
     */
    public AbstractSkuGetSuggestSupplierHandler createHandlerChain() {
        // 获取所有实现了 AbstractSkuGetSuggestSupplierHandler 的 Bean
        Map<String, AbstractSkuGetSuggestSupplierHandler> handlerMap = applicationContext.getBeansOfType(AbstractSkuGetSuggestSupplierHandler.class);

        // 排序处理器
        List<AbstractSkuGetSuggestSupplierHandler> sortedHandlers = handlerMap.values().stream()
                .sorted(Comparator.comparing(AbstractSkuGetSuggestSupplierHandler::sort))
                .collect(Collectors.toList());

        // 设置处理器链
        for (int i = 0; i < sortedHandlers.size() - 1; i++) {
            sortedHandlers.get(i).setNextHandler(sortedHandlers.get(i + 1));
        }

        return sortedHandlers.isEmpty() ? null : sortedHandlers.get(0);
    }

}
