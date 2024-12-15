package com.hete.supply.scm.server.scm.service.base;

/**
 * @author yanjiawei
 * Created on 2024/3/15.
 */
public interface IFeiShuOnlineSheet<T> {

    /**
     * 获取在线表格ID的方法
     */
    String getTableId();

    /**
     * 设置在线表格ID的方法
     */
    void setTableId(String tableId);

    String getAppToken();

    void setAppToken(String appToken);

    void doFeiShuPush(T feishuReportDto);
}
