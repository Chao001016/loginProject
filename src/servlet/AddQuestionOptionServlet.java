package servlet;

import com.alibaba.fastjson.JSONObject;
import common.BaseResponse;
import common.ErrorCode;
import common.ResultUtils;
import db.wrapper.InsertWrapper;
import org.xml.sax.SAXException;
import pojo.Question;
import pojo.QuestionOption;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/addQuestionOption", name = "addQuestionOption")
public class AddQuestionOptionServlet extends CommonServlet<QuestionOption> {

    @Override
    public BaseResponse service(QuestionOption questionOption, HttpServletRequest req, HttpServletResponse res) {
        // 1.获取参数
        Long qid = questionOption.getQid();
        String content = questionOption.getContent();
        // 2.业务逻辑
        if (qid == null || content == null) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR, "选项内容和题目编号不能为空");
        }
        // 3.数据库交互
        InsertWrapper insertWrapper = new InsertWrapper(questionOption);
        Long id = insertWrapper.execute();
        // 4.返回参数
        return ResultUtils.success(id);
    }
}
