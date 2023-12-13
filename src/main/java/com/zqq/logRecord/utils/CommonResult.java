package com.zqq.logRecord.utils;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 公共返回对象
 * +----------------------------------------------------------------------
 * | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
 * +----------------------------------------------------------------------
 * | Copyright (c) 2016~2023 https://www.crmeb.com All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
 * +----------------------------------------------------------------------
 * | Author: CRMEB Team <admin@crmeb.com>
 * +----------------------------------------------------------------------
 */
@Data
public class CommonResult<T> implements Serializable {

    private static final long serialVersionUID = -6630747483482976634L;

    /**
     * 响应码
     */
    private Integer code;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 响应体
     */
    private T data;

    // ===========构造器开始，构造器私有，外部不可直接创建=========================================

    private CommonResult() {
        this.code = 200;
    }

    private CommonResult(T data) {
        this.code = 200;
        this.data = data;
    }

    private CommonResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private CommonResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ===========构造器结束，构造器私有，外部不可直接创建=========================================

    /**
     * 成功返回,没有结果
     */
    public static <T> CommonResult<T> success() {
        return new CommonResult<>();
    }

    /**
     * 成功返回,有返回结果
     *
     * @param data 获取的数据
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(data);
    }
    public static <T> CommonResult<T> success(Integer code,String message,T data){
        return new CommonResult<>(code,message,data);
    }

}
