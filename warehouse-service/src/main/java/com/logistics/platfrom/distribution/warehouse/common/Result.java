package com.logistics.platfrom.distribution.warehouse.common;

import lombok.Data;
import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private Integer code; // 状态码
    private String msg; // 消息
    private T data; // 数据

    // 成功响应
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    // 失败响应
    public static <T> Result<T> fail(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }
}