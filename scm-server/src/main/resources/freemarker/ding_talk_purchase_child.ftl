## 【采购子单关键信息推送】

- 采购子单：${orderNo!''}
- 状态：${status!''}
- 触发人：${operatorUsername!''}
- 触发时间：${operatorTime!''}
- 说明：

<#if status = "待排产">
    采购子单已经由${param1!''}供应商接单，进入已接单状态请及时跟进，核准真实情况！
</#if>
<#if status = "待收货">
    采购子单已经由${param2!''}物流发货，发货总数为${param3!''}件，并进入待收货状态，运单号是：${param4!''}，请及时跟进，核准真实情况！
</#if>
<#if status = "已入库">
    采购子单已经由${param5!''}仓库收货上架，上架总数为${param6!''}件，并进入已入库状态，请及时跟进，核准真实情况！
</#if>
<#if status = "已完结">
    采购子单已经由${param5!''}仓库收货上架，上架总数为${param6!''}件，并进入已入库状态，请及时跟进，核准真实情况！
</#if>
<#if status = "已退货">
    采购子单已经由${param7!''}仓库退货，退货单号为：${param8!''}，退货件数为：${param9!''}件，请及时跟进，核准真实情况！
</#if>
<#-- 超链接 -->
> 链接: [点击跳转](https://www.dingtalk.com/)