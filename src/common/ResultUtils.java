package common;

public class ResultUtils {
    public static <T> BaseResponse<T> success (T data) {
        return new BaseResponse<T>(ErrorCode.SUCCESS, data);
    }

    public static BaseResponse error (ErrorCode errorCode, String descriptor) {
        return new BaseResponse(errorCode, descriptor);
    }

    public static BaseResponse error (ErrorCode errorCode) {
        return new BaseResponse(errorCode);
    }
}
