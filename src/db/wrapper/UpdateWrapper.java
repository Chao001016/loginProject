package db.wrapper;

import common.Common;
import db.DataBase;
import db.wrapper.map.ConditionMap;
import db.wrapper.map.ModifyMap;
import db.xml.XMLResultMapResolver;
import org.xml.sax.SAXException;
import pojo.Question;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UpdateWrapper implements BaseWrapper<Boolean> {
    private Class clazz;
    /**
     * 需要修改的列名
     */
    private ModifyMap modifyMap = new ModifyMap();
    /**
     * 修改的条件
     */
    private ConditionMap condition = new ConditionMap();

    public static void main(String[] args)  {
        UpdateWrapper updateWrapper = new UpdateWrapper(Question.class);
        Long id = Long.valueOf(3);
        updateWrapper.eq("id", id);
        updateWrapper.modify("content", "double型的取值范围是多少");
        updateWrapper.modify("answer", "-1.***e308 ~ 1.***e308");
        updateWrapper.modify("analysis", "阶码的取值范围为[-2 ^ 10, 2 ^ 10 -1]");
        Boolean isSuccess = updateWrapper.executeUpdate();
        System.out.println(isSuccess);
    }
    public UpdateWrapper(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public Boolean execute() {
        return this.executeUpdate();
    }

    public Boolean executeUpdate () {
        PreparedStatement pstt = this.generateSQL();
        int rs = 0;
        try {
            rs = pstt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs > 0;
    }

    public void modify (String key, Object val) {
        modifyMap.put(key, val);
    }

    public void eq (String key, Object val) {
        condition.put(key, val);
    }

    // TODO 模糊匹配
    public void like (String key, String val) {

    }

    public PreparedStatement generateSQL () {
        Connection conn = DataBase.getConn();
        XMLResultMapResolver xmlResultMapResolver = new XMLResultMapResolver();
        String tableName = xmlResultMapResolver.getTableName(clazz);
        // update question set column=key where
        String sql = "update " + tableName;
        sql += modifyMap.increaseSQL(clazz);
        sql += condition.increaseSQL(clazz);
        PreparedStatement pstt = null;
        try {
            pstt = conn.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int i = 1;
        i = modifyMap.setParameter(pstt, i);
        condition.setParameter(pstt, i);
        return pstt;
    }
}
