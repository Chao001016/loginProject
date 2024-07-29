package servlet;

import com.alibaba.fastjson.JSONObject;
import common.BaseResponse;
import common.Common;
import common.ErrorCode;
import common.ResultUtils;
import db.DataBase;
import db.wrapper.QueryWrapper;
import org.xml.sax.SAXException;
import pojo.Question;
import pojo.QuestionOption;
import pojo.QuestionResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet(urlPatterns = "/queryQuestionOption", name = "queryQuestionOption")
public class QueryQuestionOption extends CommonServlet<QuestionOption> {
    @Override
    public BaseResponse service(QuestionOption questionOption, HttpServletRequest req, HttpServletResponse res) {
        // 1.获取参数
        Long id = questionOption.getId();
        String content = questionOption.getContent();
        Long qid = questionOption.getQid();
        Long creator = questionOption.getCreator();
        String order = questionOption.getOrder();
        Integer score = questionOption.getScore();
        Date createTime = questionOption.getCreateTime();
        Date updateTime = questionOption.getUpdateTime();
        // 2.业务逻辑
        // 查询选项不能存在为空的
        if (id == null && content == null && qid == null &&
            creator == null && order == null && createTime == null &&
            updateTime == null && score == null) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR, "错误的请求参数");
        }
        // 3.数据库交互
        QueryWrapper queryWrapper = new QueryWrapper(QuestionOption.class);
        if (id != null) queryWrapper.eq("id", id);
        if (content != null) queryWrapper.eq("content", content);
        if (qid != null) queryWrapper.eq("qid", qid);
        if (creator != null) queryWrapper.eq("creator", creator);
        if (order != null) queryWrapper.eq("order", order);
        if (createTime != null) queryWrapper.eq("createTime", createTime);
        if (updateTime != null) queryWrapper.eq("updateTime", updateTime);
        if (score != null) queryWrapper.eq("score", score);
        ArrayList<QuestionOption> questionOptionArrayList = queryWrapper.execute();
        // 4.返回参数
        return ResultUtils.success(questionOptionArrayList);
    }
}
