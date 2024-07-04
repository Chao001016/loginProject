package servlet;

import common.BaseResponse;
import common.ErrorCode;
import common.ResultUtils;
import db.DataBase;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterServlet extends HttpServlet  {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, RuntimeException {
        resp.setContentType("application/type");
        resp.setCharacterEncoding("UTF-8");
        // 一.获取参数
        BufferedReader reader = req.getReader();
        String line;
        StringBuilder data = new StringBuilder();
        while((line = reader.readLine()) != null) {
            data.append(line);
        }
        JSONObject jobj = JSON.parseObject(data.toString());
        String account = (String) jobj.get("userAccount");
        String password = (String) jobj.get("password");
        String checkPassword = (String) jobj.get("checkPassword");
        // 二.业务逻辑
        // 1.空判断
        if (account == null || account == null || password == null) {
            BaseResponse resData = ResultUtils.error(ErrorCode.PARAM_ERROR, "账号密码和二次密码都不能为空");
            resp.getWriter().write(JSON.toJSONString(resData));
            return;
        }
        // 2.密码和二次密码是否相同
        if (!checkPassword.equals(password)) {
            BaseResponse resData = ResultUtils.error(ErrorCode.PARAM_ERROR, "密码和二次密码不相同");
            resp.getWriter().write(JSON.toJSONString(resData));
            return;
        }
        // 3.账号校验规则，不能包含特殊字符，长度不能小于6位
        if (account.length() < 6) {
            BaseResponse resData = ResultUtils.error(ErrorCode.PARAM_ERROR, "账号长度不能小于6位");
            resp.getWriter().write(JSON.toJSONString(resData));
            return;
        }
        String regEx = "[\\u00A0\\s\"`~!@#$%^&*()+=|{}':;',\\[\\].<>/&#63;~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(account);
        if (m == null) {
            BaseResponse resData = ResultUtils.error(ErrorCode.PARAM_ERROR, "账号不能包含特殊字符");
            resp.getWriter().write(JSON.toJSONString(resData));
            return;
        }
        // 4.密码校验规则，密码不能小于八位
        if (password.length() < 8) {
            BaseResponse resData = ResultUtils.error(ErrorCode.PARAM_ERROR, "密码不能小于八位");
            resp.getWriter().write(JSON.toJSONString(resData));
            return;
        }
        // 三.数据库交互
        // 1.账号是已否存在
        Connection conn = DataBase.getConn();
        String sql = "select * from user where userAccount=?";
        try {
            PreparedStatement pstt = conn.prepareStatement(sql);
            pstt.setString(1, account);
            ResultSet rs = pstt.executeQuery();
            if (rs.next()) {
                System.out.println(rs.getString("userAccount"));
                BaseResponse resData = ResultUtils.error(ErrorCode.PARAM_ERROR, "用户名已存在");
                resp.getWriter().write(JSON.toJSONString(resData));
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // 2.插入新用户
        sql = "insert into user(userAccount, userPassword) values(?,?)";
        try {
            PreparedStatement pstt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstt.setString(1, account);
            String encryptPwd = DigestUtils.md5Hex("yupi" + password);
            pstt.setString(2, encryptPwd);
            pstt.executeUpdate();
            ResultSet rs = pstt.getGeneratedKeys();
            if (rs.next()) { // 插入成功
                Long userId = rs.getLong(1);
                BaseResponse resData = ResultUtils.success(userId);
                resp.getWriter().write(JSON.toJSONString(resData));
            } else { // 插入失败
                BaseResponse resData = ResultUtils.error(ErrorCode.UNKNOWN_ERROR);
                resp.getWriter().write(JSON.toJSONString(resData));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
