package Common;

public class BaseResponse<T> {
    private Integer code;
    private String message;
    private String descriptor;
    private T data;

    public BaseResponse (Integer code, T data, String message, String descriptor) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse (Integer code, T data, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse (ErrorCode code) {
        this.code = code.getCode();
        this.message = code.getMessage();
    }
    public BaseResponse (ErrorCode code, T data) {
        this.code = code.getCode();
        this.message = code.getMessage();
        this.data = data;
    }
    public BaseResponse (ErrorCode code, String descriptor) {
        this.code = code.getCode();
        this.message = code.getMessage();
        this.descriptor = descriptor;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
