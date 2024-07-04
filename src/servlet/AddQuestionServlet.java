package servlet;

import com.alibaba.fastjson.JSONObject;
import common.BaseResponse;
import common.Common;
import common.ErrorCode;
import common.ResultUtils;
import db.DataBase;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet(urlPatterns = "/addQuestion", name = "addQuestion")
public class AddQuestionServlet extends UserServlet {

    @Override
    public BaseResponse service(JSONObject jsonObject, HttpServletRequest req, HttpServletResponse res) throws SQLException {
        // 1.参数获取
        String content = jsonObject.getString("content");
        Integer type = jsonObject.getInteger("type");
        Integer score = jsonObject.getInteger("score");
        String answer = jsonObject.getString("answer");
        Integer state = jsonObject.getInteger("state");
        String analysis = jsonObject.getString("analysis");
        String options = jsonObject.getString("options");
        String tag = jsonObject.getString("tag");

        // 2.业务逻辑--参数校验
        if (type == null) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR, "题型不能为空");
        }
        if (content == null) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR, "题目内容不能为空");
        }
        // 3.数据库交互
        Connection conn = DataBase.getConn();
        int paramNum = getParamNum(jsonObject);
        String sql = getInsertSql(jsonObject, paramNum);
        sql = sql.replaceFirst("\\?", "content");
        sql = sql.replaceFirst("\\?", "type");
        if (score != null) {
            sql = sql.replaceFirst("\\?", "score");
        }
        if (answer != null) {
            sql = sql.replaceFirst("\\?", "answer");
        }
        if (state != null) {
            sql = sql.replaceFirst("\\?", "state");
        }
        if (analysis != null) {
            sql = sql.replaceFirst("\\?", "analysis");
        }
        if (options != null) {
            sql = sql.replaceFirst("\\?", "options");
        }
        if (tag != null) {
            sql = sql.replaceFirst("\\?", "tag");
        }
        PreparedStatement pstt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        pstt.setString(1, content);
        pstt.setInt(2, type);

        if (score != null) {
            pstt.setInt(3, score);
        }
        if (answer != null) {
            pstt.setString(4, answer);
        }
        if (state != null) {
            pstt.setInt(5, state);
        }
        if (analysis != null) {
            pstt.setString(6, analysis);
        }
        if (options != null) {
            pstt.setString(7, options);
        }
        if (tag != null) {
            pstt.setString(8, tag);
        }
        pstt.execute();
        int questionId;
        ResultSet rs = pstt.getGeneratedKeys();
        if (rs.next()) {
            questionId = rs.getInt(1);
            return ResultUtils.success(questionId);
        } else {
            return ResultUtils.error(ErrorCode.UNKNOWN_ERROR, "插入数据失败");
        }
    }

    public static void main(String[] args) throws SQLException {
        String a = "??";
        System.out.println(a.replaceFirst("\\?", "1"));
        Map map = new HashMap<String ,Object>();
        map.put("name", "lichao");
        map.put("age", "18");
        JSONObject jsonObject = new JSONObject(map);
        System.out.println(getInsertSql(jsonObject, 2));
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
    private static String getInsertSql (JSONObject jsonObject, int paramNum) throws SQLException {
        String sql = "insert into question(#dynamicColumn) values(#dynamicColumn)";
        Character[] param = new Character[paramNum];
        Arrays.fill(param, '?');
        String dynamicColumn = Common.join(param,",");
        sql = sql.replaceAll("#dynamicColumn", dynamicColumn);
        return sql;
    }
}
