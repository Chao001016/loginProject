package common;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

public class Common {
    public static void main(String[] args) {
    }

    /**
     * 将数组转化为以分隔符相隔的字符串
     * @param arr
     * @param separator
     * @return
     * @param <T>
     */
    public static <T> String join(T[] arr, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length - 1; i++) {
            sb.append(arr[i] + separator);
        }
        sb.append(arr[arr.length - 1]);
        return sb.toString();
    }
    // 将本地时间转化为SQL时间
   public static String convertToSQLDateTime (LocalDateTime localDateTime) {
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
   }
    // 将字符串分隔成转换为List
   public static ArrayList splitToArrayList(String str, String separator) {
        String[] strArr = str.split(separator);
       ArrayList arrayList = new ArrayList(Arrays.asList(strArr));
       return arrayList;
   }

   public static void createFile (InputStream in, String dirName, String filename) {
       File dir = new File(dirName);
       if (!dir.exists()) {
           dir.mkdirs();
       } else {
           System.out.println(dir.getPath() + "已经存在，无法创建新目录");
       }
       try {
           File targetFile = new File(dir, String.valueOf(filename));
           FileOutputStream fos = new FileOutputStream(targetFile);
           byte[] bytes = new byte[1024];
           while ((in.read(bytes)) != -1) {
               fos.write(bytes);
           }
           fos.close();
           in.close();
           targetFile.createNewFile();

       } catch (IOException e) {
           throw new RuntimeException(e);
       }
   }
}
