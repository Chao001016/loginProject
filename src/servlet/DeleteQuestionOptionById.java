package servlet;

import common.BaseResponse;
import common.ErrorCode;
import common.ResultUtils;
import db.wrapper.DeleteWrapper;
import org.xml.sax.SAXException;
import pojo.QuestionOption;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class DeleteQuestionOptionById extends CommonServlet<QuestionOption> {
    @Override
    public BaseResponse service(QuestionOption questionOption, HttpServletRequest req, HttpServletResponse res) {
        // 1.获取参数
        Long id = questionOption.getId();
        // 2.业务逻辑
        if (id == null) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR, "问题编号不能为空");
        }
        // 3.数据库交互
        DeleteWrapper deleteWrapper = new DeleteWrapper(QuestionOption.class);
        deleteWrapper.eq("id", id);
        Integer affectedRows = deleteWrapper.execute();
        // 4.出参处理
        return ResultUtils.success(affectedRows);
    }
}
