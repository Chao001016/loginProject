package servlet;

import common.BaseResponse;
import common.ResultUtils;
import db.wrapper.QueryWrapper;
import org.xml.sax.SAXException;
import pojo.Tag;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
@WebServlet(urlPatterns = "/queryTag", name = "queryTag")
public class QueryTag extends CommonServlet<Tag> {
    @Override
    public BaseResponse service(Tag tag, HttpServletRequest req, HttpServletResponse res) {
        // 1.获取参数
        // 2.业务逻辑
        // 3.数据库交互
        QueryWrapper queryWrapper = new QueryWrapper(tag);
        ArrayList<Tag> tagArrayList = queryWrapper.executeQuery();
        // 4.返回参数
        return ResultUtils.success(tagArrayList);
    }
}
