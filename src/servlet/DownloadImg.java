package servlet;

import com.alibaba.fastjson.JSONObject;
import common.BaseResponse;
import common.Common;
import common.ErrorCode;
import common.ResultUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Date;

@WebServlet(name="downloadImg", urlPatterns="/downloadImg")
public class DownloadImg extends CommonServlet<JSONObject> {
    public static void main(String[] args) {

    }
    @Override
    public BaseResponse service(JSONObject jsonObject, HttpServletRequest req, HttpServletResponse res) {
        String filename = jsonObject.getString("filename");
        String dir = req.getServletContext().getInitParameter("questionImgDir");
        res.setHeader("Content-Type", "application/octet-stream");
        res.setContentType("application/x-msdownload");
        res.setHeader("Content-Disposition", "attachment;filename=" + filename);
        File file = new File(dir, filename);
        try {
            InputStream in = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            ServletOutputStream sos = res.getOutputStream();
            while ((in.read(bytes)) != -1) {
                sos.write(bytes);
            }
            in.close();
            sos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
