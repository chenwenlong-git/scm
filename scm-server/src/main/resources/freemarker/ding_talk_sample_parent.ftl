## 【样品需求母单关键信息推送】

- 样品需求母单：【${orderNo!''}】
- 状态：【${status!''}】
- 触发人：【${operatorUsername!''}】
- 触发时间：【${operatorTime!''}】
- 说明：
<#if status = "已接单">
    样品需求母单的【${cnt1!''}】个样品子单都已经完成接单动作，总共需要打样【${cnt2!''}】件样品，请及时跟进，核准真实情况！
</#if>
<#if status = "待收样">
    样品需求母单的【${cnt3!''}】个样品子单都已经完成发货动作，总共需要发货【${cnt4!''}】件样品，请及时跟进，核准真实情况！
</#if>
<#if status = "已选中">
    样品需求母单的【${cnt5!''}】个样品子单都已经完成收货动作，总共需要收货【${cnt6!''}】件样品，其中选中【${cnt7!''}】件，请及时跟进，核准真实情况！
</#if>
<#if status = "打样失败">
    样品需求母单的【${cnt8!''}】个样品子单都打样失败，请及时跟进，核准真实情况！
</#if>
<#-- 超链接 -->
> 链接: [点击跳转](https://www.dingtalk.com/)
