package db.wrapper.map;

import common.Common;
import db.xml.XMLResultMapResolver;
import org.xml.sax.SAXException;
import pojo.Result;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

public class InsertMap extends BaseMap {
    /**
     * 得到形式 (type, content) values (?,?) 的字符串
     * @param clazz
     * @return
     */
    @Override
    public String increaseSQL(Class clazz) {
        // (type, content) values(?,?)
        StringBuilder sb = new StringBuilder();
        String[] columnArr = new String[this.size()];
        String[] placeHolder = new String[this.size()];
        int i = 0;
        Map<String, Result> resultMap = new XMLResultMapResolver().getXMLObjMap(clazz);
        for (Object key : this.keySet()) {
            Result result = resultMap.get(key);
            String property = result.getProperty();
            String column = result.getColumn();
            if (this.has(property)) {
                columnArr[i] = column;
                placeHolder[i++] = "?";
            }
        }

        if (this.size() > 0) {
            sb.append("(" + Common.join(columnArr, " , ") + ")");
            sb.append(" values(" + Common.join(placeHolder, " , ") + ")");
        }
        return sb.toString();
    }
}
