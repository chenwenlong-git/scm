## 【样品采购子单关键信息推送】

- 样品采购子单：【${orderNo!''}】
- 状态：【${status!''}】
- 触发人：【${operatorUsername!''}】
- 触发时间：【${operatorTime!''}】
- 说明：

<#if status = "已接单">
    样品采购单已经由【${param1!''}】供应商接单，进入已接单状态，请及时跟进，核准真实情况！
</#if>
<#if status = "已拒绝">
    样品采购单已经被【${param1!''}】供应商拒绝接单，进入已拒绝状态，拒绝原因：【${param7!''}】，请及时跟进，核准真实情况！
</#if>
<#if status = "待收样">
    样品采购单已经由【${param2!''}】物流发货，发货总数为【${param3!''}】件，进入待收货状态，运单号为：【${param4!''}】，请及时跟进，核准真实情况！
</#if>
<#if status = "已选中">
    样品采购单已经打样成功，为sku：【${param7!''}】，请及时跟进，核准真实情况！
</#if>
<#if status = "打样失败">
    样品采购单进行了退样操作，生成样品退货单：【${param8!''}】，由【${param9!''}】物流承运，退件总数为【${param10!''}】，运单号为：【${param11!''}】，请及时跟进，核准真实情况！
</#if>
<#-- 超链接 -->
> 链接: [点击跳转](https://www.dingtalk.com/)