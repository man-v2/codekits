package org.codekit.utils;

/**
 * 
* @ClassName: Result
* @Description: 返回接口调用返回结果
* @author Administrator
* @date 2018年7月27日 下午4:40:02
*
 */
public class Result {
    /**
     * 响应代码
     */
    private String code;

    /**
     * 响应结果描述
     */
    private String msg;

    /**
     * 返回的实际数据
     */
    private String data;

    /**
     * true 成功，false 失败
     */
    private Boolean success;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
