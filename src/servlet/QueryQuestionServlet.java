package servlet;

import com.alibaba.fastjson.JSONObject;
import common.BaseResponse;
import common.Common;
import common.ErrorCode;
import common.ResultUtils;
import db.DataBase;
import pojo.Question;
import pojo.QuestionResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet(urlPatterns = "/queryQuestion", name = "queryQuestion")
public class QueryQuestionServlet extends CommonServlet<Question> {
    public BaseResponse service(JSONObject jsonObject, HttpServletRequest req, HttpServletResponse res) throws SQLException {
        // 1.获取参数
        Long id = jsonObject.getLong("id");
        String content = jsonObject.getString("content");
        Integer type = jsonObject.getInteger("type");
        Integer score = jsonObject.getInteger("score");
        String answer = jsonObject.getString("answer");
        Integer state = jsonObject.getInteger("state");
        String analysis = jsonObject.getString("analysis");
        String options = jsonObject.getString("options");
        String tag = jsonObject.getString("tag");
        // 2.业务逻辑
        // 查询选项不能存在为空的
        if (content == null && type == null && score == null &&
            answer == null && state == null && analysis == null &&
            options == null && tag == null && id == null) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR, "错误的请求参数");
        }
        // 3.数据库交互
        String sql = getQuerySql(jsonObject);
        Connection conn = DataBase.getConn();
        if (content != null) {
            sql = sql.replaceFirst("#", "content");
        }
        if (id != null) {
            sql = sql.replaceFirst("#", "id");
        }
        if (type != null) {
            sql = sql.replaceFirst("#", "type");
        }
        if (score != null) {
            sql = sql.replaceFirst("#", "score");
        }
        if (answer != null) {
            sql = sql.replaceFirst("#", "answer");
        }
        if (state != null) {
            sql = sql.replaceFirst("#", "state");
        }
        if (analysis != null) {
            sql = sql.replaceFirst("#", "analysis");
        }
        if (options != null) {
            sql = sql.replaceFirst("#", "options");
        }
        if (tag != null) {
            sql = sql.replaceFirst("#", "tag");
        }
        PreparedStatement pstt = conn.prepareStatement(sql);
        int parameterIndex = 1;
        if (content != null) {
            pstt.setString(parameterIndex++, content);
        }
        if (id != null) {
            pstt.setLong(parameterIndex++, id);
        }
        if (type != null) {
            pstt.setInt(parameterIndex++, type);
        }
        if (score != null) {
            pstt.setInt(parameterIndex++, score);
        }
        if (answer != null) {
            pstt.setString(parameterIndex++, answer);
        }
        if (state != null) {
            pstt.setInt(parameterIndex++, state);
        }
        if (analysis != null) {
            pstt.setString(parameterIndex++, analysis);
        }
        if (options != null) {
            pstt.setString(parameterIndex++, options);
        }
        if (tag != null) {
            pstt.setString(parameterIndex, tag);
        }
        ResultSet rs = pstt.executeQuery();
        ArrayList<QuestionResponse> questionResponseArrayList = new ArrayList<>();
        while (rs.next()) {
            QuestionResponse question = new QuestionResponse();
            question.setId(rs.getLong("id"));
            question.setCreator(rs.getLong("creator"));
            question.setContent(Common.resolveBytes((rs.getBytes("content"))));
            question.setType(rs.getInt("type"));
            question.setScore(rs.getInt("score"));
            question.setState(rs.getInt("state"));
            question.setAnswer(Common.resolveBytes(rs.getBytes("answer")));
            question.setAnalysis(Common.resolveBytes(rs.getBytes("analysis")));
            question.setUpdateTime(rs.getTimestamp("update_time"));
            question.setCreateTime(rs.getTimestamp("create_time"));
            questionResponseArrayList.add(question);
        }
         //
        // 4.返回参数
        return ResultUtils.success(questionResponseArrayList);
    }

    private static int getParamNum (JSONObject jsonObject) {
        AtomicInteger paramNum = new AtomicInteger();
        jsonObject.entrySet().forEach(info -> {
            if(info.getValue() != null) {
                paramNum.getAndIncrement();
            }
        });
        return paramNum.get();
    }

    private static String getQuerySql (JSONObject jsonObject) throws SQLException {
        int paramNum = getParamNum(jsonObject);
        String sql = "select * from question where #dynamicColumn";
        String[] param = new String[paramNum];
        Arrays.fill(param, "#=?");
        String dynamicColumn = Common.join(param," and ");
        sql = sql.replaceAll("#dynamicColumn", dynamicColumn);
        return sql;
    }

    @Override
    public BaseResponse service(Question question, HttpServletRequest req, HttpServletResponse res) throws SQLException {
        // 1.获取参数
        Long id = question.getId();
        String content = question.getContent().toString();
        Integer type = question.getType();
        Integer score = question.getScore();
        String answer = question.getAnswer().toString();
        Integer state = question.getState();
        String analysis = question.getAnalysis().toString();
        String options = question.getOptionId().toString();
        String tag = question.getTag();
        // 2.业务逻辑
        // 查询选项不能存在为空的
        if (content == null && type == null && score == null &&
                answer == null && state == null && analysis == null &&
                options == null && tag == null && id == null) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR, "错误的请求参数");
        }
        // 3.数据库交互
//        String sql = getQuerySql(jsonObject);
//        Connection conn = DataBase.getConn();
//        if (content != null) {
//            sql = sql.replaceFirst("#", "content");
//        }
//        if (id != null) {
//            sql = sql.replaceFirst("#", "id");
//        }
//        if (type != null) {
//            sql = sql.replaceFirst("#", "type");
//        }
//        if (score != null) {
//            sql = sql.replaceFirst("#", "score");
//        }
//        if (answer != null) {
//            sql = sql.replaceFirst("#", "answer");
//        }
//        if (state != null) {
//            sql = sql.replaceFirst("#", "state");
//        }
//        if (analysis != null) {
//            sql = sql.replaceFirst("#", "analysis");
//        }
//        if (options != null) {
//            sql = sql.replaceFirst("#", "options");
//        }
//        if (tag != null) {
//            sql = sql.replaceFirst("#", "tag");
//        }
//        PreparedStatement pstt = conn.prepareStatement(sql);
//        int parameterIndex = 1;
//        if (content != null) {
//            pstt.setString(parameterIndex++, content);
//        }
//        if (id != null) {
//            pstt.setLong(parameterIndex++, id);
//        }
//        if (type != null) {
//            pstt.setInt(parameterIndex++, type);
//        }
//        if (score != null) {
//            pstt.setInt(parameterIndex++, score);
//        }
//        if (answer != null) {
//            pstt.setString(parameterIndex++, answer);
//        }
//        if (state != null) {
//            pstt.setInt(parameterIndex++, state);
//        }
//        if (analysis != null) {
//            pstt.setString(parameterIndex++, analysis);
//        }
//        if (options != null) {
//            pstt.setString(parameterIndex++, options);
//        }
//        if (tag != null) {
//            pstt.setString(parameterIndex, tag);
//        }
//        ResultSet rs = pstt.executeQuery();
//        ArrayList<QuestionResponse> questionResponseArrayList = new ArrayList<>();
//        while (rs.next()) {
//            QuestionResponse question = new QuestionResponse();
//            question.setId(rs.getLong("id"));
//            question.setCreator(rs.getLong("creator"));
//            question.setContent(Common.resolveBytes((rs.getBytes("content"))));
//            question.setType(rs.getInt("type"));
//            question.setScore(rs.getInt("score"));
//            question.setState(rs.getInt("state"));
//            question.setAnswer(Common.resolveBytes(rs.getBytes("answer")));
//            question.setAnalysis(Common.resolveBytes(rs.getBytes("analysis")));
//            question.setUpdateTime(rs.getTimestamp("update_time"));
//            question.setCreateTime(rs.getTimestamp("create_time"));
//            questionResponseArrayList.add(question);
//        }
        //
        // 4.返回参数
//        return ResultUtils.success(questionResponseArrayList);
        return null;
    }
}
