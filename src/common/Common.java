package common;

import java.util.Base64;

public class Common {
    public static void main(String[] args) {
    }
    public static <T> String join(T[] arr, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length - 1; i++) {
            sb.append(arr[i] + separator);
        }
        sb.append(arr[arr.length - 1]);
        return sb.toString();
    }

    /**
     * 将Base64字符数组转换为解密字符串
     * @param bytes
     * @return
     */
    public static String resolveBytes(byte[] bytes) {
        if (bytes == null) return null;
        return new String(bytes);
    }
}
