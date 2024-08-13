package servlet;

import com.alibaba.fastjson.JSONObject;
import com.sun.deploy.net.HttpRequest;
import common.BaseResponse;
import common.Common;
import common.ErrorCode;
import common.ResultUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Path;
import java.util.Date;
import java.util.regex.Pattern;

@MultipartConfig
@WebServlet(name="uploadImg", urlPatterns="/uploadImg")
public class UploadImg extends CommonServlet<JSONObject> {
    public static void main(String[] args) {
        File img = new File("C:/Users/70679/Desktop/bg.jpg");
        String ext = img.getName().substring(img.getName().lastIndexOf("."));
        try {
            InputStream in = new FileInputStream(img);
            Common.createFile(in, "D:/usr/local", new Date().getTime() + ext);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public BaseResponse service(JSONObject jsonObject, HttpServletRequest req, HttpServletResponse res) {
        Part part;
        String filename;
        String ext;
        String dir = req.getServletContext().getInitParameter("questionImgDir");
        String newName;
        try {
             part = req.getPart("file");
             filename = part.getSubmittedFileName();
             ext = filename.substring(filename.lastIndexOf("."));
             // 生成文件名称的机制：时间戳。这里等项目体积变大了需要改善
             newName = new Date().getTime() + ext;
        } catch (IOException e) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR, "获取上传文件失败");
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        try {
            InputStream in = part.getInputStream();

            Common.createFile(in, dir, newName);
        } catch (IOException e) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR, "获取文件流失败");
        }
        return ResultUtils.success(newName);
    }
}
