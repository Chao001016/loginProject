package servlet;

import com.alibaba.fastjson.JSONObject;
import common.BaseResponse;
import pojo.Question;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/addQuestionOption", name = "addQuestionOption")
public class AddQuestionOptionServlet extends CommonServlet<Question> {

    @Override
    public BaseResponse service(Question question, HttpServletRequest req, HttpServletResponse res) throws SQLException {
        return null;
    }
}
