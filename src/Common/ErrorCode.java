package Common;

import org.omg.CORBA.UNKNOWN;

public enum ErrorCode {
    SUCCESS(0, "ok"),
    PARAM_ERROR(40000, "参数错误"),
    EMPTY_DATA(40001, "请求数据为空"),
    NOT_LOGIN(40100, "未登录"),
    NO_RIGHT(40101, "没有权限"),
    UNKNOWN_ERROR(99999, "未知错误");
    private Integer code;
    private String message;

    ErrorCode (Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
