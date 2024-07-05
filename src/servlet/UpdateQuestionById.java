package servlet;

import com.alibaba.fastjson.JSONObject;
import common.BaseResponse;
import common.Common;
import common.ErrorCode;
import common.ResultUtils;
import db.DataBase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet(urlPatterns = "/updateQuestionById", name = "updateQuestionById")
public class UpdateQuestionById extends CommonServlet {
    @Override
    public BaseResponse service(JSONObject jsonObject, HttpServletRequest req, HttpServletResponse res) throws SQLException {
        // 1.参数获取
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
        // id 不能为空
        if (id == null) return ResultUtils.error(ErrorCode.PARAM_ERROR, "id不能为空");
        if (content == null && type == null && score == null &&
                answer == null && state == null && analysis == null &&
                options == null && tag == null) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR, "错误的请求参数");
        }
        // 3.数据交互
        // 获取更新sql
        String sql = getUpdateSql(jsonObject);
        if (score != null) {
            sql = sql.replaceFirst("#", "score");
        }
        if (content != null) {
            sql = sql.replaceFirst("#", "content");
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
        Connection conn = DataBase.getConn();
        PreparedStatement pstt = conn.prepareStatement(sql);
        System.out.println(content);
        int parameterIndex = 1;
        if (score != null) {
            pstt.setInt(parameterIndex++, score);
        }
        if (content != null) {
            pstt.setString(parameterIndex++, content);
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
            pstt.setString(parameterIndex++, tag);
        }
        pstt.setLong(parameterIndex, id);
        int num = pstt.executeUpdate();
        if (num > 0) {
            return ResultUtils.success(null);
        } else {
            return ResultUtils.error(ErrorCode.UNKNOWN_ERROR, "更新失败");
        }
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

    public static String getUpdateSql (JSONObject jsonObject) {
        String sql = "update question set #dynamicKeyVal where id=?";
        int paramNum = getParamNum(jsonObject) - 1;
        String[] keyValueArr = new String[paramNum];
        Arrays.fill(keyValueArr, "#=?");
        String dynamicKeyVal = Common.join(keyValueArr, ",");
        sql = sql.replaceAll("#dynamicKeyVal", dynamicKeyVal);
        return sql;
    }
}
