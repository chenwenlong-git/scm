package com.hete.supply.scm.server.scm.ibfs.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author weiwenxin
 * @date 2024/5/11 09:53
 */
@Getter
@AllArgsConstructor
public enum Currency implements IRemark {
    // 币种:RMB(人民币),USD(美元),
    RMB("CNY-人民币元", "CNY"),
    USD("USD-美元", "USD"),
    ;

    private final String remark;
    private final String currency;

    /**
     * 人民币与美金汇率
     */
    private static final BigDecimal CNY_USD_EXCHANGE_RATE = new BigDecimal("7.3");


    /**
     * 币种转换
     *
     * @param money          金额
     * @param currency       币种
     * @param targetCurrency 目标币种
     * @return
     */
    public static BigDecimal currencyExchange(BigDecimal money, Currency currency, Currency targetCurrency) {
        if (targetCurrency.equals(currency)) {
            return money;
        }
        // 美金转人民币，金额乘以汇率
        if (Currency.USD.equals(currency) && Currency.RMB.equals(targetCurrency)) {
            return money.multiply(CNY_USD_EXCHANGE_RATE).setScale(2, RoundingMode.HALF_UP);
        }
        // 人民币转美金，金额除以汇率
        if (Currency.RMB.equals(currency) && Currency.USD.equals(targetCurrency)) {
            return money.divide(CNY_USD_EXCHANGE_RATE, 2, RoundingMode.HALF_UP);
        }

        throw new BizException("不支持的汇率转换，金额币种:{}，目标币种:{}", currency.getRemark(), targetCurrency.getRemark());
    }

    /**
     * 转美元汇率
     *
     * @return
     */
    public BigDecimal toUsdExchangeRate() {
        if (this == RMB) {
            return BigDecimal.ONE.divide(CNY_USD_EXCHANGE_RATE, 2, RoundingMode.HALF_UP);
        }
        if (this == USD) {
            return BigDecimal.ONE;
        }
        throw new BizException("错误的汇率转换，请联系系统管理员");
    }

    /**
     * 转人民币汇率
     *
     * @return
     */
    public BigDecimal toRmbExchangeRate() {
        if (this == RMB) {
            return BigDecimal.ONE;
        }
        if (this == USD) {
            return CNY_USD_EXCHANGE_RATE;
        }
        throw new BizException("错误的汇率转换，请联系系统管理员");
    }
}
