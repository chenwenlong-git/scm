package com.hete.supply.scm.server.scm.feishu.entity.vo;

import lombok.Data;

/**
 * 飞书接口返回结果
 *
 * @author lihaifei
 **/
@Data
public class FeiShuResult<T> {

    protected Integer code;

    protected String msg;

    protected T data;

}
