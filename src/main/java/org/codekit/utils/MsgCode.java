package org.codekit.utils;

public enum MsgCode {
	SUCCESS(0, "成功"),
	FAILED(1, "服务器不可用"),
	PARAMETER_FAILED(2, "参数不正确或参数格式不正确"),
	TOKEN_FAILED(3, "未授权的操作"),
	UPLOAD_OSS_FAILED(4, "文件上传阿里云OSS服务器失败"),
	FILE_NOT_EXISTS(5, "文件不存在"),
	UPLOAD_FAILED(9, "文件上传失败"),
	CONVER_FAILED(10, "文件转码失败"),
	QUES_NOTFOUND(11, "未找到题目"),
	PAPER_NOTFOUND(12, "未找到套卷"),
	QUES_CANNOT_DEL(13, "您无权删除该题目"),
	GET_ANALYSE_TOO_FAST(99, "获取单题解析频率过快"),
	NOT_FOUND(404, "未找到相关的服务"),
	SERVER_ERROR(500, "服务器内部错误");

    private int code;
    private String description;

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    MsgCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MsgCode getByCode(int code) {
        for (MsgCode codeEnum : MsgCode.values()) {
            if (codeEnum.getCode() == code) {
                return codeEnum;
            }
        }

        return null;
    }
}
