package servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.BaseResponse;
import common.Common;
import common.ErrorCode;
import common.ResultUtils;
import db.DataBase;
import db.wrapper.UpdateWrapper;
import org.xml.sax.SAXException;
import pojo.Question;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@WebServlet(urlPatterns = "/updateQuestionById", name = "updateQuestionById")
public class UpdateQuestionById extends CommonServlet<Question> {
    @Override
    public BaseResponse service(Question question, HttpServletRequest req, HttpServletResponse res) {
        // 1.参数获取
        Long id = question.getId();
        String content = question.getContent();
        Integer type = question.getType();
        Integer score = question.getScore();
        String answer = question.getAnswer();
        Integer state = question.getState();
        String analysis = question.getAnalysis();
        String optionId = question.getOptionId();
        String tag = question.getTag();
        // 2.业务逻辑
        // id 不能为空
        if (id == null) return ResultUtils.error(ErrorCode.PARAM_ERROR, "id不能为空");
        if (content == null && type == null && score == null &&
                answer == null && state == null && analysis == null &&
                optionId == null && tag == null) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR, "错误的请求参数");
        }
        // 3.数据库交互
        UpdateWrapper updateWrapper = new UpdateWrapper(Question.class);
        updateWrapper.eq("id", id);
        if (content != null) updateWrapper.modify("content", content);
        if (type != null) updateWrapper.modify("type", type);
        if (score != null) updateWrapper.modify("score", score);
        if (answer != null) updateWrapper.modify("answer", answer);
        if (state != null) updateWrapper.modify("state", state);
        if (analysis != null) updateWrapper.modify("analysis", analysis);
        if (optionId != null) updateWrapper.modify("optionId", optionId);
        if (tag != null) updateWrapper.modify("tag", tag);
        updateWrapper.modify("updateTime", new Date());
        Boolean isSuccess = updateWrapper.executeUpdate();
        if (isSuccess) return ResultUtils.success(null);
        return ResultUtils.error(ErrorCode.UNKNOWN_ERROR, "未知错误");
    }
}
