package servlet;

import Common.BaseResponse;
import Common.ErrorCode;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import DB.DataBase;
import com.alibaba.fastjson.JSON;
import POJO.User;
import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;

@WebListener
public class LoginServlet extends HttpServlet implements ServletRequestListener {
    public void doPost (HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/type");
        res.setCharacterEncoding("UTF-8");
        String line;
        StringBuilder data = new StringBuilder();
        BufferedReader reader = req.getReader();
        while ((line = reader.readLine()) != null) {
            data.append(line);
        }
        JSONObject jobj = JSON.parseObject(data.toString());
        String userAccount = (String) jobj.get("userAccount");
        String password = (String) jobj.get("password");
        String encryptPwd = DigestUtils.md5Hex("yupi" + password);
        // 1.校验数据是否合法
        //  账号不含包含特殊字符
        String regEx = "[\\u00A0\\s\"`~!@#$%^&*()+=|{}':;',\\[\\].<>/&#63;~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(userAccount);
        if (m == null) {
            // 账号不合法
            BaseResponse br = new BaseResponse(ErrorCode.PARAM_ERROR, "账号不合法");
            res.getWriter().write(JSON.toJSONString(br));
        }
        if (password.length() < 8) {
            // 密码不合法
            BaseResponse br = new BaseResponse(ErrorCode.PARAM_ERROR, "密码不合法");
            res.getWriter().write(JSON.toJSONString(br));
        }
        // 2.判断账号密码是否正确
        Connection conn = DataBase.getConn();
        String sql = "select * from user where userAccount=? and userPassword=?";
        PreparedStatement pstmt;
        User user = new User();
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userAccount);
            pstmt.setString(2, encryptPwd);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setAvatorUrl(rs.getString("avatorUrl"));
                user.setGender(rs.getInt("gender"));
                user.setUserRole(rs.getString("userRole"));
                user.setPhone(rs.getString("phone"));
                user.setEmail(rs.getString("email"));
                user.setUserStatus(rs.getInt("userStatus"));
                user.setUpdateTime(rs.getString("updateTime"));
                user.setCreateTime(rs.getString("createTime"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // 3.返回登陆成功结果
        BaseResponse br;
        if (user.getId() != null) {
            br = new BaseResponse(ErrorCode.SUCCESS, user);
        } else {
            br = new BaseResponse(ErrorCode.PARAM_ERROR, "账号或密码错误");
        }
        res.getWriter().write(JSON.toJSONString(br));
    }
    public void doGet (HttpServletRequest req, HttpServletResponse res) {
        System.out.println("hello servlet Get");
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        System.out.println("request destory");
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        System.out.println("request create");
    }
}
